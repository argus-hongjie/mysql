/**
 * 
 */
package fr.argus.socle.thread;

import static fr.argus.socle.util.Constant.ID_TICKET_INEXISTANT;
import static fr.argus.socle.util.Helper.defaultIfEmptyOrNullOrException;
import static fr.argus.socle.util.Helper.defaultIfNullOrException;
import static fr.argus.socle.util.Helper.deleteTemporaryFile;
import static fr.argus.socle.util.Helper.generatetTcketSousLot;
import static fr.argus.socle.util.Helper.getCurrentDateToString;
import static fr.argus.socle.util.Helper.getFileName;
import static fr.argus.socle.util.Helper.getProperty;
import static fr.argus.socle.util.Helper.loggerGenerator;
import static fr.argus.socle.util.Helper.statusTradExpressManager;
import static fr.argus.socle.util.Pivot.createXmlFormatPivot;
import static fr.argus.socle.util.Signalements.PF_ERREUR_GENERATION_PIVOT;
import static fr.argus.socle.util.Signalements.PF_SOUS_LOTS_EN_COURS_;
import static fr.argus.socle.util.Signalements.PF_SOUS_LOTS_ERREUR_BASE_DE_DONNEES;
import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.db.DBClient;
import fr.argus.socle.model.ProcessTradExpress;
import fr.argus.socle.model.Ticket;
import fr.argus.socle.model.VariableContenu;
import fr.argus.socle.queue.TicketQueueManager;
import fr.argus.socle.thread.service.Monitorable;
import fr.argus.socle.util.Constant;
import fr.argus.socle.util.Helper;
import fr.argus.socle.util.Logs;
import fr.argus.socle.util.SFTPManager;
import fr.argus.socle.util.SSHFunctions;
import fr.argus.socle.util.Signalements;
import fr.argus.socle.ws.model.TicketQuery;

/**
 * © @author Mongi MIRAOUI 24 mai 2016
 */
public class ProcessTicketThread implements Callable<Boolean>, Monitorable {

	private static final Logger logger = LogManager.getLogger(ProcessTicketThread.class);
	protected TicketQuery ticket;
	protected String filePath = "";
	protected VariableContenu variableContenu;
	protected String bpIndex;
	protected String idTicket;
	protected ProcessTradExpress processTradExpress;
	private DBClient dBClient = new DBClient();
	private final String TICKET_FILE_ID ="ticket_id";
	private final String remoteDir ="/tmp/";
	private String filename = "";
	private final String PROCESS_TRADEXPRESS = "PROCESS_TRADEXPRESS";
	protected String fichier;
	protected Logs logs;
	protected Integer idLivraisonNumerique;
	protected boolean fillSuiviEtape = false;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Boolean call() throws Exception {

		ticket = TicketQueueManager.getInstance().getTicket();

		preExecute();

		if (ticket != null){
			if(this.variableContenu == null){
				this.variableContenu = VariableContenu.builder().build();
			}
			doJob(variableContenu);
		}

		postExecute();

		return true;
	}

	/**
	 * 
	 */
	protected void doJob(VariableContenu varContenu) {

		int queueSize = TicketQueueManager.getInstance().getSize();

		logger.info("new queue size = " + queueSize);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.argus.socle.thread.Monitorable#preExecute()
	 */
	@Override
	public void preExecute() {

		// TicketQuery ticket = TicketQueueManager.getInstance().getTicket();
		filePath = defaultIfEmptyOrNullOrException("", ()->ticket.getFiles().getFILE().get(0).getValue());
		if (!filePath.isEmpty()){
			logger.info("Traitement du ticket :" + filePath);
		}
		if(ticket != null){
			idLivraisonNumerique = defaultIfNullOrException(ID_TICKET_INEXISTANT, () -> parseInt(ticket.getIdLivraison()));
			statusTradExpressManager(ticket,getProperty("traitement.encours") ,dBClient, ofNullable(ticket.getIdPere()).map(pere->pere+"").orElse(""));				
			dBClient.updateStatus(idLivraisonNumerique, Signalements.valueOf(getProperty("traitement.encours")).getCode());
			variableContenu.setIdPere(ofNullable(ticket.getIdPere()).map(pere->pere+"").orElse(""));
			
			fichier = defaultIfEmptyOrNullOrException("", ()-> ticket.getFiles().getFILE().stream().findFirst().get().getNomFichier());
			logs = loggerGenerator(ticket,"Début de traitement du ticket "+fichier+" par le module "+getProperty("module.name"),true);
			try {
				dBClient.addTraceIntoLogs(logs, true);
			} catch (SQLException e) {
				logger.error("Erreur d'ajout dans logs des traces "+fichier+" : ", e);
			}
			
			Integer idparent = defaultIfNullOrException(0,()->dBClient.getIdParentModule(getProperty("module.name")));
			dBClient.addSuiviEtape(idLivraisonNumerique, getProperty("module.name"),idparent);
		}
		
		MonitoringThread.register(this, Instant.now());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.argus.socle.thread.Monitorable#postExecute()
	 */
	@Override
	public void postExecute() {
		if (!filePath.isEmpty()){
			logger.info("fin de traitement du fichier :" + filePath);
		}

		if (ticket != null){
			String tmpDir = System.getProperty("java.io.tmpdir");			
			if(variableContenu != null  && variableContenu.getListeTicketsFilsSousLots() != null){// pour le cas module souslots		
				variableContenu.getListeTicketsFilsSousLots().stream().forEach(ticket ->{
					bpIndex = ticket.getBpIndex();	
					idTicket = ticket.getId();					
					java.io.File temp = generateFileTicket(tmpDir,idTicket);
					String pathXML = getFileName(temp);
					generatetTcketSousLot(pathXML,ticket.getContent());
					executeCommand(filename, pathXML,PROCESS_TRADEXPRESS);
					deleteTemporaryFile(temp);
				});
								
			}else{
				bpIndex = ticket.getBpIndex();	
				idTicket = ticket.getIdLivraison();		
				java.io.File temp = generateFileTicket(tmpDir,idTicket);
				String pathXML = getFileName(temp);				
				Ticket pere = generateTicketparent();				
				generatetTcketSousLot(pathXML,pere.getContent());
				executeCommand(filename, pathXML,PROCESS_TRADEXPRESS);
				deleteTemporaryFile(temp);
			}
			
			deleteEncoursTicket();
			addSuiviEtapeByModule();
			
			try {
				logs.setDateFin(getCurrentDateToString(Constant.FORMAT_YYYY_MM_DD_HH_MM_SS));
				dBClient.addTraceIntoLogs(logs, false);
			} catch (SQLException e) {
				logger.error("Erreur de mise à jour des logs des traces "+fichier+" :", e);
			}
			
		}		
		
		MonitoringThread.unRegister(this);
	}

	/**
	 * Delete ticket into encours table 
	 */
	protected void deleteEncoursTicket() {
		if(variableContenu.getLibelleSignalement() == null || variableContenu.getLibelleSignalement().isEmpty()){
			Integer idLivraison = idTicket !=  null ? Integer.parseInt(idTicket) : ID_TICKET_INEXISTANT;
			boolean result = dBClient.deleteTicketByModuleAfterTreatement(idLivraison, MainThread.getModuleName());
			if(!result){
				logger.error("Erreur de suppression du ticket : "+idTicket+" en attente de traitement par le module : "+MainThread.getModuleName());
			}
		}
	}

	/**
	 * Adding suivi étape for each module into suivi_etape table
	 */
	protected void addSuiviEtapeByModule() {
		try {
			 variableContenu.setRetour(createXmlFormatPivot(idLivraisonNumerique, dBClient).getContent());			
		} catch (IOException | URISyntaxException | SQLException e1) {
			dBClient.traceLoggerInfos(Helper.loggerGenerator(ticket,"Erreur lors de la génération du format pivot du fichier "+fichier+" : "+  Arrays.toString(e1.getStackTrace()), false));
            logger.error("Erreur lors de la génération du format pivot "+ idLivraisonNumerique +" : ", e1.getMessage());
            variableContenu.setCodeSignalement(PF_ERREUR_GENERATION_PIVOT.getCode());
            variableContenu.setLibelleSignalement(PF_ERREUR_GENERATION_PIVOT.name());
		}
		
		
		
		dBClient.updateSuiviEtape(idLivraisonNumerique, getProperty("module.name"),
				defaultIfEmptyOrNullOrException("", ()->variableContenu.getIdOcr()),variableContenu.getRetour(),
				defaultIfEmptyOrNullOrException("", ()->variableContenu.getCodeSignalement()), 
				defaultIfEmptyOrNullOrException("", ()->variableContenu.getLibelleSignalement()), "Suivi étape module");
		
		if (fillSuiviEtape) dBClient.addSuiviEtapeAttribut(idLivraisonNumerique, getProperty("module.name"), variableContenu.getNomAttribut(), variableContenu.getValeurAttribut(), variableContenu.getQuantiteAttribut());
	}

	/**
	 * Ticket parent generator
	 * @return
	 */
	protected Ticket generateTicketparent() {
		Ticket pere= Ticket.builder()
				.id(idTicket).idProduit(defaultIfEmptyOrNullOrException("", ()->ticket.getIdProduit()+""))
				.idOCR(defaultIfEmptyOrNullOrException("", ()->ticket.getIdOCR()+""))
				.priority(ticket.getPriority()+"").type(ticket.getType())
				.modeApprovisionnement(defaultIfEmptyOrNullOrException("", ()->ticket.getModeApprovisionnement()))
				.bpIndex(defaultIfEmptyOrNullOrException("", ()->bpIndex))
		.nomFichier(defaultIfEmptyOrNullOrException("", ()->ticket.getFiles().getFILE().get(0).getNomFichier()))
		.tailleFichier(defaultIfEmptyOrNullOrException("", ()->ticket.getFiles().getFILE().get(0).getTailleFichier()))
		.cheminFichier(defaultIfEmptyOrNullOrException("", ()->ticket.getFiles().getFILE().get(0).getValue()))
		.dateDipo(defaultIfEmptyOrNullOrException("", ()->ticket.getFiles().getFILE().get(0).getDateMiseADisposition()))
		.idPere(ofNullable(ticket.getIdPere()).map(pereid->pereid+"").orElse(""))
		.status(defaultIfEmptyOrNullOrException("", ()->variableContenu.getLibelleSignalement())).build();
		return pere;
	}

	
	
	/**
	 * @param filename
	 * @param pathXML
	 */
	private void executeCommand(String filename, String pathXML,String processtradExpress) {
		processTradExpress = dBClient.getProcessTradExpress(processtradExpress,MainThread.getModuleName()); 
		String ipMachine = processTradExpress.getIp();
		String command = processTradExpress.getCommand()+" "+remoteDir+filename;
		String login = processTradExpress.getLogin();
		String password = processTradExpress.getPassword();

		SFTPManager.upload(login, password, ipMachine, pathXML, remoteDir,filename);
  
		try {			
			SSHFunctions.SSHClient(ipMachine,command,login,password);
		} catch (Exception e) {
			logger.error("Erreur lors de l'ancement du process TradExpress : ", e.getMessage());
		}
	}

	/**
	 * @param tmpDir
	 * @return
	 */
	private java.io.File generateFileTicket(String tmpDir,String idTicket) {
		filename =TICKET_FILE_ID+"_"+idTicket+".xml";	
		java.io.File temp = new java.io.File(tmpDir, filename);						
		//temp.deleteOnExit();		
		return temp;
	}

	/**
	 * 
	 */
	public static void shutdown() {
		// TODO

	}
		
	protected void setStatus(Signalements status, Signalements errDb) {
		variableContenu.setCodeSignalement(status.getCode());
		variableContenu.setLibelleSignalement(status.name());
		if(dBClient.updateStatus(idLivraisonNumerique, status.getCode()) < 0){							
			dBClient.traceLoggerInfos(loggerGenerator(ticket, "Le status du fichier "+fichier+" n'a être mise à jour ", false));
			logger.error("Erreur de mise à jour du status lors de la constitution de sous lots du ticket "+fichier);
			variableContenu.setCodeSignalement(errDb.getCode());
			variableContenu.setLibelleSignalement(errDb.name());
		}
	}

}
