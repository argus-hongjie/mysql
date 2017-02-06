package fr.argus.socle.thread;

import static fr.argus.socle.util.Helper.getCurrentDateToString;
import static fr.argus.socle.util.Helper.loggerGenerator;

import java.net.URI;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import fr.argus.socle.db.ConfigModule;
import fr.argus.socle.db.DBClient;
import fr.argus.socle.db.entity.TypeModule;
import fr.argus.socle.queue.TicketQueueManager;
import fr.argus.socle.thread.service.Monitorable;
import fr.argus.socle.util.Constant;
import fr.argus.socle.util.Helper;
import fr.argus.socle.util.Logs;
import fr.argus.socle.ws.model.TicketQuery;

/**
 * © @author Mongi MIRAOUI 28 avr. 2016
 */
public class MainThread implements Runnable, Monitorable {

	private static final Logger logger = LogManager.getLogger(MainThread.class);
	private static ScheduledExecutorService monitorExecutor = Executors.newScheduledThreadPool(4);
	private int supervisionDelaySeconds;
	private int scrutationBanetteDelay;
	private int scrutationPileDelay;
	private int autotestDelay;
	private int nombreThreadsMax = 1;

	private MonitoringThread monitoringThread;
	private static ProcessingBanetteInThread processingBanetteInThread;
	private static ProcessingQueueThread processingQueueThread;
	private AutoTestThread autoTestThread;
	static DBClient dbClient = new DBClient();
	List<TicketQuery> ticketInProgress;
	static HttpServer httpServer;
	static HttpServer httpServerSupervision;
	static TypeModule currentModule;
	private static String moduleName;
	private static String classNameModule;

	public MainThread() {
	}

	/**
	 * @param moduleName
	 * @param instances
	 */
	public MainThread(String moduleName) {
		MainThread.moduleName = moduleName;
	}
	
	public MainThread(String moduleName, String classNameModule) {
		MainThread.moduleName = moduleName;
		MainThread.classNameModule =classNameModule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		logger.info("Démarrage du module ...");
		Logs logs = loggerGenerator("Démarrage du module "+moduleName,true);
		traceModule(logs, true);
		// preExecute();
		configModule();
		initialize();
		if (!isRunning()) {
			subscribeModule();
			checkTicketsInProgress();
			launchThreads();
			logs.setDateFin(getCurrentDateToString(Constant.FORMAT_YYYY_MM_DD_HH_MM_SS));
			traceModule(logs, false);
		} else
			notifySupport();

	}

	/**
	 * Notification du support : envoie d'email.
	 */
	private void notifySupport() {
		logger.info("notification du spport ... ");

		try {
			String mailSmtpHost = currentModule.getParametres().getMailSmtpHost();
			String mailTo = currentModule.getParametres().getSupportMailTo();
			String mailFrom = currentModule.getParametres().getSupportMailFrom();
			String mailSubject = currentModule.getParametres().getSupportMailSubject();
			String mailText = "Module en cours d'exécution : \n Machine : " + Helper.getComputerName() + "\n login : "
					+ Helper.getLogin() + "\n id du Module :" + currentModule.getId() + "\n nom du Module "
					+ currentModule.getNom();

			Helper.sendEmail(mailTo, mailFrom, mailSubject, mailText, mailSmtpHost);
		} catch (Exception e) {
			logger.error(" erreur de notification du spport : " + e.getMessage());

		}

	}

	/**
	 * 
	 * Tester si le module est en cours d'exécution.
	 * 
	 * @return
	 */
	private boolean isRunning() {
		logger.info("vérifier si le module est déjà en cours d'exécution ... ");

		try {
			String login = Helper.getLogin();
			String machine = Helper.getComputerName();

			int idTypeModule = currentModule.getId();
			int wsPort = currentModule.getParametres().getPortWS();
			return dbClient.isModuleRunning(idTypeModule, machine, login, wsPort);
		} catch (Exception e) {
			logger.error("Erreur de vérification si le module est en cours d'exécution : " + e.getMessage());
			return false;

		}
	}

	/**
	 * récupérer les tickets inscrites dans la base pour cette machine et qui ne
	 * sont pas encore traités.
	 */
	private void checkTicketsInProgress() {
		logger.info(
				"récupérer les tickets inscrites dans la base pour cette machine et qui ne sont pas encore traités ... ");

		try {
			String machine = Helper.getComputerName();
			ticketInProgress = dbClient.getTicketsInProgress(machine);
			if (ticketInProgress != null && !ticketInProgress.isEmpty())
				TicketQueueManager.getInstance().addAllTickets(ticketInProgress);
		} catch (Exception e) {
			logger.error("Erreur de récupération de tickets encours : " + e.getMessage());

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.argus.socle.thread.Monitorable#preExecute()
	 */
	@Override
	public void preExecute() {
		MonitoringThread.register(this, Instant.now());
	}

	private void initialize() {
		logger.info("Initialisation des paramètres du module ... ");

		try {
			currentModule = ConfigModule.getInstance().getModule(moduleName);
			if(currentModule != null){
				supervisionDelaySeconds = currentModule.getParametres() != null ? currentModule.getParametres().getDurationSupervisionSeconds()    : 0;
				scrutationBanetteDelay  = currentModule.getParametres() != null ? currentModule.getParametres().getScrutationBanetteDelaySeconds() : 0;
				scrutationPileDelay     = currentModule.getParametres() != null ? currentModule.getParametres().getScrutationPileDelaySeconds()    : 0;
				autotestDelay           = currentModule.getParametres() != null ? currentModule.getParametres().getAutotestDelaySeconds()          : 0;
				nombreThreadsMax        = currentModule.getParametres() != null ? currentModule.getParametres().getNombreThreadsMax()              : 1;
			}

			monitoringThread = new MonitoringThread();
			processingBanetteInThread = new ProcessingBanetteInThread();
			processingQueueThread = new ProcessingQueueThread(classNameModule,nombreThreadsMax);
			autoTestThread = new AutoTestThread();
		} catch (NumberFormatException e) {
			logger.error("Erreur d'initialisation des paramètres du module : " + e.getMessage());

		}

	}

	/**
	 * charger la configuration du module depuis la base de données.
	 */
	private void configModule() {
		logger.info("chargement de la configuration du module ... ");
		try {
			ConfigModule.loadConfig();
		} catch (Exception e) {
			logger.error("Erreur de chargement de la configuration depuis la base de données : " + e.getMessage());
		}

	}

	/**
	 * Inscription du module dans la table des modules.
	 */
	private void subscribeModule() {
		logger.info("Inscription du module dans la table production.modules....");
		try {
			String machine = Helper.getComputerName();
			String login = Helper.getLogin();

			Integer idTypeModule    = currentModule != null ? currentModule.getId() : 0;
			String ipPortWebService = (currentModule != null &&  currentModule.getParametres()!= null) ? currentModule.getParametres().getIpPortWS() : "0";
			Integer porSupervision  = (currentModule != null &&  currentModule.getParametres()!= null) ? currentModule.getParametres().getPortSupervision() : 0;
			LocalDateTime date = LocalDateTime.now();
			LocalDateTime dateLastProcess = LocalDateTime.now();

			//String portWS = ipPortWebService.substring(ipPortWebService.indexOf(':') + 1, ipPortWebService.length());
			int portWebService = Integer.valueOf(ipPortWebService);
			dbClient.subscribeModule(idTypeModule, machine, login, portWebService, porSupervision, dateLastProcess,
					date);
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de l'inscription du module dans la table production.modules : "
					+ e.getMessage());
		}

	}

	/**
	 * Supprimer le module de la table des modules.
	 */
	private static void unSubscribeModule() {

		logger.info("suppression du module de la table production.modules....");
		try {
			String login = Helper.getLogin();
			String machine = Helper.getComputerName();
			Integer idTypeModule = currentModule != null ? currentModule.getId() : 0;
			int wsPort = currentModule.getParametres().getPortSupervision();
			dbClient.unSubscribeModule(idTypeModule, machine, login, wsPort);
		} catch (Exception e) {
			logger.error("Erreur de vérification si le module est en cours d'exécution : " + e.getMessage());
		}

	}

	/**
	 * Démarrer les différents threads ainsi que le serveur de webservices.
	 */
	private void launchThreads() {

		logger.info("lancement des différents threads du module ...");
		// 1 . Démarrer le thread applicatif
		// Thread mainThread = new Thread(new MainThread());
		// mainThread.setName("MainThread");
		// mainThread.start();

		// 2. lancer le thread de gestion de time-out des objets
		monitorExecutor.scheduleAtFixedRate(monitoringThread, 0, supervisionDelaySeconds, TimeUnit.SECONDS);

		// Lancer les deux webservices :
		// 3. webservice de consommations de tickets.
		httpServer= startServer(currentModule.getParametres().getPortWS());
		// 4. webservice de supervision.
		httpServerSupervision = startServer(currentModule.getParametres().getPortSupervision());

		// 5.a - Lancer le thread de traitement de la banette d'entrée
		monitorExecutor.scheduleWithFixedDelay(processingBanetteInThread, 0, scrutationBanetteDelay, TimeUnit.SECONDS);

		// 5.b - Lancer le thread de traitement de la pile "à traiter"
		monitorExecutor.scheduleWithFixedDelay(processingQueueThread, 0, scrutationPileDelay, TimeUnit.SECONDS);

		// 6. Démarrer le thread autotest
		monitorExecutor.scheduleWithFixedDelay(autoTestThread, 0, autotestDelay, TimeUnit.SECONDS);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.argus.socle.thread.Monitorable#postExecute()
	 */
	@Override
	public void postExecute() {
		MonitoringThread.unRegister(this);
	}

	/**
	 * Arrêter le thread en cours d'exécution.
	 */
	public static void shutdown() {

		logger.info("Arrêt du serveur http ...");
		Logs logs = loggerGenerator("Arrêt du module "+moduleName,true);
		traceModule(logs, true);
		unSubscribeModule();

		processingBanetteInThread.shutdown();
		processingQueueThread.shutdown();

		monitorExecutor.shutdown();
		logger.info("Arrêt des différents modules ...");
		while (!monitorExecutor.isTerminated()) {
			try {
				logger.info("Module pas encore terminé ...");
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				logger.error("Erreur d'arret du module :" + e.getMessage());
			}
		}

		// try {
		// monitorExecutor.awaitTermination(5, TimeUnit.SECONDS);
		// if(!monitorExecutor.isTerminated())
		// monitorExecutor.shutdownNow();
		// } catch (InterruptedException e) {
		// logger.error("Erreur d'arret du module :" + e.getMessage());
		// }

		httpServer.shutdown(5, TimeUnit.SECONDS);
		httpServerSupervision.shutdown(5, TimeUnit.SECONDS);
		
		logs.setDateFin(getCurrentDateToString(Constant.FORMAT_YYYY_MM_DD_HH_MM_SS));
		traceModule(logs, false);
	}

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
	 * application.
	 * 
	 * @return Grizzly HTTP server.
	 */
	public HttpServer startServer(int wsPort) {
		logger.info("lancement du serveur de webservices...");
		HttpServer server = null;
		try {
			final ResourceConfig rc = new ResourceConfig().packages(Constant.WS_PACKAGE);
			String login = Helper.getLogin();
			String machine = Helper.getComputerName();
			int idTypeModule = currentModule.getId();
			//int wsPort = currentModule.getParametres().getPortWS();
			String machineName = ConfigModule.loadMachine(idTypeModule, machine, login, wsPort);
			StringBuffer sb = new StringBuffer();
			sb.append(machineName).append(":").append(wsPort);
			String uri = MessageFormat.format(Constant.BASE_URI,sb.toString());
			server = GrizzlyHttpServerFactory.createHttpServer(URI.create(uri), rc);			
			 
		} catch (Exception e) {
			logger.error("Erreur de lancement du serveur de webservice sur le port:"+wsPort+" \n" + e.getMessage());
		}
		return server ;
	}

	public static String getModuleName() {
		return moduleName;
	}
	
	/**
	 * 
	 * @param logs
	 * @param isStarting
	 */
	public static void traceModule(Logs logs, boolean isStarting){		
		try {
			dbClient.addTraceIntoLogs(logs, isStarting);
		} catch (SQLException e) {
			logger.error("Erreur d'ajout dans logs la trace de démarrage du module "+moduleName+" : ", e);
		}
	}

}
