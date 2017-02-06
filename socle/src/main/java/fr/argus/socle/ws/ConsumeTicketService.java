package fr.argus.socle.ws;

import static fr.argus.socle.util.Constant.ID_TICKET_INEXISTANT;
import static fr.argus.socle.util.Helper.defaultIfNullOrException;
import static java.lang.Integer.parseInt;

import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.db.DBClient;
import fr.argus.socle.util.Helper;
import fr.argus.socle.ws.model.Error;
import fr.argus.socle.ws.model.TicketQuery;
import fr.argus.socle.ws.model.TicketResponse;

/**
 * © @author Mongi MIRAOUI 2 mai 2016
 */

@Path("dispatch")
public class ConsumeTicketService {

	DBClient dbClient = new DBClient();
	private static final Logger logger = LogManager.getLogger(ConsumeTicketService.class);

	/**
	 * recevoir les ordres de consommation de ticket.
	 * 
	 * @param ticketQuery
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Object consumeXML(TicketQuery ticketQuery) {

		Object response = null;

		try {
			if (ticketQuery != null) {
				int resultSave = saveTicket(ticketQuery);
				
				if (resultSave > 0) {
					addToQueue(ticketQuery);
					TicketResponse ticketResponse = new TicketResponse();
					response = ticketResponse.createTICKET(ticketQuery.getId().toString());
				} else {
					logger.error("Un problème est survenu lors de l'insertion du ticket dans la table encours.");
					Error error = new Error();
					response = error.createERROR(
							"Un problème est survenu lors de l'insertion du ticket dans la table encours.");
				}
			}
		} catch (SQLException e) {
			logger.error(
					"Un problème est survenu lors de l'insertion du ticket dans la table encours:" + e.getMessage());
			Error error = new Error();
			response = error.createERROR(
					"Un problème est survenu lors de l'insertion du ticket dans la table encours:" + e.getMessage());

		} catch (Exception e) {

			logger.error("Un problème est survenu lors de la consommation du ticket :" + e.getMessage());
			Error error = new Error();
			response = error
					.createERROR("Un problème est survenu lors de la consommation du ticket :" + e.getMessage());

		}
		return response;
	}

	/**
	 * Enregistrer le ticket dans la table de des demandes de traitements..
	 * 
	 * @param ticketQuery
	 *            ticket à sauvegarder.
	 */
	private int saveTicket(TicketQuery ticketQuery) throws SQLException {
		LocalDateTime date = LocalDateTime.now();
		String login = System.getProperty("user.name");
		String machine = Helper.getComputerName();

		Integer idLivraisonNumerique = defaultIfNullOrException(ID_TICKET_INEXISTANT, ()->parseInt(ticketQuery.getIdLivraison()));	
		Integer idPere = dbClient.getIdLotPereByIdSousLot(idLivraisonNumerique);
		ticketQuery.setIdPere(idPere != null && idPere > 0 ? idPere :null);

		return dbClient.saveTicket(ticketQuery, login, machine, date);
	}

	/**
	 * Mettre le ticket en attente dans la pile générale du module sui sera
	 * dépilé selon les priorités.
	 * 
	 * @param ticketQuery
	 *            ticket à empiler.
	 */
	private void addToQueue(TicketQuery ticket) {
		Helper.addTicketToQueue(ticket);
	}

	/**
	 * @return the dbClient
	 */
	public DBClient getDbClient() {
		return dbClient;
	}

	/**
	 * @param dbClient the dbClient to set
	 */
	public void setDbClient(DBClient dbClient) {
		this.dbClient = dbClient;
	}
	
}
