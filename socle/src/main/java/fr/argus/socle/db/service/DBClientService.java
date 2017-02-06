/**
 * 
 */
package fr.argus.socle.db.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import fr.argus.socle.ws.model.LogsResponse;
import fr.argus.socle.ws.model.ReturnAutoTest;
import fr.argus.socle.ws.model.TicketQuery;

/**
 * @author mamadou.dansoko
 *
 */
public interface DBClientService {
	/**
	 * Renvoie la liste de logs selon la limite nb
	 * @param nb
	 * @return
	 */
	public LogsResponse getLogs(int nb);
	/**
	 * Renvoie le statut de test des différents process
	 * @return
	 */
	public ReturnAutoTest getAutoTest();
	/**
	 * Sauvegarde un ticket dans la base
	 * @param ticketQuery
	 * @param login
	 * @param machine
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public int saveTicket(TicketQuery ticketQuery, String login, String machine, LocalDateTime date)throws SQLException;
	/**
	 * Sauvegarde un ticket dans la banette
	 * @param ticketQuery
	 * @param login
	 * @param machine
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public int saveTicketBanette(TicketQuery ticketQuery, String login, String machine, LocalDateTime date)throws SQLException; 
	/**
	 * Vérifie si un ticket a été traité ou encours de traitement
	 * @param filePath
	 * @return
	 */
	public boolean isTicketProcessed(String filePath);
	/**
	 * Souscription d'un module en début de traitement
	 * @param idTypeModule
	 * @param machine
	 * @param login
	 * @param portWebService
	 * @param portSupervision
	 * @param dateLastProcess
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public int subscribeModule(Integer idTypeModule, String machine, String login, Integer portWebService,Integer portSupervision, LocalDateTime dateLastProcess, LocalDateTime date) throws SQLException;
	/**
	 * Renvoie la liste des tickets encours de traitement
	 * @param machine
	 * @return
	 */
	public List<TicketQuery> getTicketsInProgress(String machine);
	/**
	 * Enregistrement des tickets 
	 * @param tickets
	 * @return
	 */
	public long insertTickets(List<TicketQuery> tickets);
	/**
	 * Vérifie si le module tourne
	 * @param idTypeModule
	 * @param machine
	 * @param login
	 * @param wsPort
	 * @return
	 */
	public boolean isModuleRunning(int idTypeModule, String machine, String login, int wsPort);
	/**
	 * Désabonnement du module en fin de traitement
	 * @param idTypeModule
	 * @param machine
	 * @param login
	 * @param wsPort
	 * @return
	 */
	public boolean unSubscribeModule(Integer idTypeModule, String machine, String login, int wsPort);

}
