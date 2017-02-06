package fr.argus.socle.ws;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.db.DBClient;
import fr.argus.socle.thread.MainThread;
import fr.argus.socle.ws.model.Alive;
import fr.argus.socle.ws.model.Error;
import fr.argus.socle.ws.model.LogsQuery;
import fr.argus.socle.ws.model.Stop;;

/**
 * © @author Mongi MIRAOUI 2 mai 2016
 */

@Path("supervision")
public class SupervisionService {

	private static final Logger logger = LogManager.getLogger(SupervisionService.class);
	DBClient dbClient = new DBClient();

	/**
	 * Récupréer les logs depuis la base de données.
	 * 
	 * @param query
	 *            la requête au format xml content le nombre de logs à
	 *            récuprérer. (exp :<LOGS nb ="10"/>)
	 * @return la liste des logs récupérés.
	 */
	@POST
	@Path("/logs")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Object getLogs(LogsQuery query) {

		Object response = null;

		try {
			if (query.getNb() != null && query.getNb() > 1000)
				response = dbClient.getLogs(1000);
			else if (query.getNb() != null)
				response = dbClient.getLogs(query.getNb());
			else
				response = dbClient.getLogs(10);

		} catch (Exception e) {

			logger.error("Erreur lors de la récupération des logs : " + e.getMessage());
			Error error = new Error();
			response = error.createERROR("Erreur lors de la récupération des logs : " + e.getMessage());
		}

		return response;

	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Object dispatch(JAXBElement<String> query) {
		Object response = null;
		switch (query.getName().getLocalPart()) {
		case "ALIVE":
			response = isAlive();
			break;
		case "STOP":
			response = stopModule();
			break;
		case "RETURN_AUTO_TEST":
			response = getLastAutoTest();
			break;
		default:
			logger.error("Veuillez vérifier la requête.");
			Error error = new Error();
			response = error.createERROR("Veuillez vérifier la requête.");
			break;
		}
		return response;
	}

	/**
	 * Cette méthode permet de déterminer le statut du module.
	 * 
	 * @param query
	 *            la requête au format xml (<ALIVE/>)
	 * @return la date du système au format xml (<ALIVE>yyyy-MM-dd
	 *         HH24:Mi:ss</ALIVE> )
	 */

	public Object isAlive() {

		Object response = null;
		Alive alive = new Alive();
		response = alive.createALIVE(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
		return response;

	}

	/**
	 * Arrêter le module.
	 * 
	 * @param query
	 *            requête d'arrêt du module. (<STOP/>)
	 * @return la date du système au format xml (<STOP>yyyy-MM-dd
	 *         HH24:Mi:ss</STOP>
	 */

	public Object stopModule() {

		Object response = null;
		MainThread.shutdown();
		Stop stop = new Stop();
		response = stop.createSTOP(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
		return response;

	}

	/**
	 * récupérer le dernier auto test du module.
	 * 
	 * @param query
	 *            requête de récupération du dernier autotest.
	 * @return la dernier autotest du module.
	 */
	public Object getLastAutoTest() {
		Object response = null;
		response = dbClient.getAutoTest();
		return response;
	}

}
