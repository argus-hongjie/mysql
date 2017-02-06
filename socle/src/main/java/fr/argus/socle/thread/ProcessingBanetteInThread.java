package fr.argus.socle.thread;

import static fr.argus.socle.util.Helper.addTicketToQueue;
import static fr.argus.socle.util.Helper.defaultIfEmptyOrNullOrException;
import static fr.argus.socle.util.Helper.getComputerName;
import static fr.argus.socle.util.Helper.getLogin;
import static fr.argus.socle.util.Helper.makeTicketFromFile;
import static java.time.LocalDateTime.now;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.db.ConfigModule;
import fr.argus.socle.db.DBClient;
import fr.argus.socle.db.entity.TypeModule;
import fr.argus.socle.queue.TicketQueueManager;
import fr.argus.socle.thread.service.Monitorable;
import fr.argus.socle.util.Helper;
import fr.argus.socle.ws.model.TicketQuery;

/**
 * © @author Mongi MIRAOUI 19 mai 2016
 */
public class ProcessingBanetteInThread implements Runnable, Monitorable {

	private static final Logger logger = LogManager.getLogger(ProcessingBanetteInThread.class);
	List<Path> listFilesInBanette;
	DBClient dbClient = new DBClient();

	private String banetteIn = "";
	private String banetteOut = "";
	private volatile boolean stop = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		preExecute();
		reloadConfig();
		initialize();
		identifyFilesInBanette();
		processBanette();
		postExecute();

	}

	/**
	 * 
	 */
	private void processBanette() {
		logger.info("Début de traitement de la banette d'entrée ...");
		try {
			if (listFilesInBanette != null) {

				for (Path file : listFilesInBanette) {
					if (stop)
						break;
					else
						processTicket(file);

				}
				// int cpus = Runtime.getRuntime().availableProcessors();
				// int nbThreads = cpus * 10;

				// while (listFilesInBanette.size() > 0) {
				// if (listFilesInBanette.size() <= nbThreads)
				// processTickets(listFilesInBanette);
				// else {
				// processTickets(listFilesInBanette.subList(0, nbThreads));
				// listFilesInBanette.subList(0, nbThreads).clear();
				// }
				// }

			} else
				logger.warn("Erreur de parcour de la banette d'entrée : listFilesInBanette is null");

		} catch (Exception e) {
			logger.error("Erreur de parcour de la banette d'entrée :" + e.getMessage());
		}

	}

	/**
	 * TODO : Lot 2 cette méthode permet d'améliorer la performance d'insertion
	 * dans la base de données.
	 * 
	 * @param listFilesInBanette
	 */
	private void processTickets(List<Path> listFilesInBanette) {
		List<TicketQuery> listTicketsToProcess = new ArrayList<>();
		listFilesInBanette.stream().forEach(file -> {
			listTicketsToProcess.add(makeTicketFromFile(file));
		});

		List<TicketQuery> listProcessedTickets = new ArrayList<>();
		String machine = getComputerName();
		listProcessedTickets = dbClient.getTicketsInProgress(machine);

		listProcessedTickets.stream().forEach(ticket -> {
			listTicketsToProcess.remove(ticket);
		});

		long result = dbClient.insertTickets(listTicketsToProcess);
		if (result > 0)
			TicketQueueManager.getInstance().addAllTickets(listTicketsToProcess);

	}

	/**
	 * @param file
	 */
	private void processTicket(Path file) {
		logger.info("début de traitement du fichier : " + file.toFile().getAbsolutePath());
		TicketQuery ticket = makeTicketFromFile(file);
		if (isTicketProcessed(file.toFile().getAbsolutePath())) {
			logger.warn("Ce fichier a été déjà traité :" + file.toFile().getAbsolutePath());
			return;
		}
		try {
			int result = saveTicket(ticket);
			if (result > 0)
				addToQueue(ticket);
			else
				logger.error("Erreur d'enregistrement du ticket dans la base de données.");

		} catch (Exception e) {

			logger.error("Erreur d'enregistrement du ticket dans la base de données : " + e.getMessage());
		}

		logger.info("fin de traitement du fichier : " + file.toFile().getAbsolutePath());

	}

	/**
	 * @param ticket
	 * @return
	 */
	private boolean isTicketProcessed(String filePath) {
		return dbClient.isTicketProcessed(filePath);
	}

	/**
	 * Ajouter le ticket à la pile à traiter.
	 * 
	 * @param ticket
	 *            ticket à ajourter à la pile.
	 */
	private void addToQueue(TicketQuery ticket) {
		addTicketToQueue(ticket);
	}

	/**
	 * Enregistrer le ticket dans ta table encrous.
	 * 
	 * @param ticket
	 *            ticket à enregistrer.
	 */
	private int saveTicket(TicketQuery ticket) throws SQLException {
		logger.info("Enregistrement du ticket dans la base de données ...");
		LocalDateTime date = now();
		String login = getLogin();
		String machine = getComputerName();
		return dbClient.saveTicketBanette(ticket, login, machine, date);

	}

	/**
	 * initiliaser les paramètres du module.
	 */
	private void initialize() {
		logger.info("Initiliasation des paramètres du module ...");

		try {
			TypeModule typesModule = ConfigModule.getInstance().getModule(MainThread.getModuleName());
			banetteIn = defaultIfEmptyOrNullOrException(null, ()->typesModule.getParametres().getBanetteEntree());
			banetteOut = defaultIfEmptyOrNullOrException(null, ()->typesModule.getParametres().getBanetteSortie());
		} catch (Exception e) {
			logger.error("Erreur d'initialisation deparamètres du module : " + e.getMessage());
		}

	}

	/**
	 * Identifier la matière dans la banette d'entrée.
	 */
	private void identifyFilesInBanette() {

		logger.info("Parcours de la banette d'entrée ...");
		try {
			if(banetteIn!= null && !banetteIn.isEmpty()) listFilesInBanette = Helper.listFilesFromDir(banetteIn);

		} catch (IOException e) {
			logger.error("Erreur de parcour de la banette d'entrée : " + e.getMessage() + "\n");
		}

	}

	/**
	 * rechargé la configuration du module.
	 * 
	 */
	private void reloadConfig() {
		logger.info("Rechargement de la configuration du module depuis la base de données ...");

		try {
			ConfigModule.reloadConfig();
		} catch (Exception e) {
			logger.error("Erreur de chargement de la configuration depuis la base de données : " + e.getMessage());

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
	 * 
	 */
	public void shutdown() {
		stop = true;
	}

}
