package fr.argus.socle.db;

import static fr.argus.socle.util.Helper.defaultIfEmptyOrNullOrException;
import static fr.argus.socle.util.Helper.defaultIfNullOrException;
import static fr.argus.socle.util.Helper.getComputerName;
import static fr.argus.socle.util.Helper.getLogin;
import static fr.argus.socle.util.Signalements.PF_CONTENU_PIVOT_ORUGINAL;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import fr.argus.socle.db.entity.ModaliteAppro;
import fr.argus.socle.db.entity.Resource;
import fr.argus.socle.db.mapper.LivraisonNumeriqueRowMapper;
import fr.argus.socle.db.mapper.ResourceRowMapper;
import fr.argus.socle.db.mapper.SousLotsRowMapper;
import fr.argus.socle.db.service.DBClientService;
import fr.argus.socle.db.service.IQueries;
import fr.argus.socle.model.Attribut;
import fr.argus.socle.model.Attributs;
import fr.argus.socle.model.Fichier;
import fr.argus.socle.model.LivraisonNumerique;
import fr.argus.socle.model.ProcessTradExpress;
import fr.argus.socle.model.Reception;
import fr.argus.socle.model.ReceptionContent;
import fr.argus.socle.thread.MainThread;
import fr.argus.socle.util.Constant;
import fr.argus.socle.util.Helper;
import fr.argus.socle.util.Logs;
import fr.argus.socle.ws.model.LogResponse;
import fr.argus.socle.ws.model.LogsResponse;
import fr.argus.socle.ws.model.ReturnAutoTest;
import fr.argus.socle.ws.model.TicketQuery;

/**
 * © @author mamadou.dansoko may 2016
 */
public class DBClient implements DBClientService,IQueries {

	private static final Logger logger = LogManager.getLogger(DBClient.class);

	private Connection connection;

	/**
	 * Récuprer les derniers logs depuis la base de données.
	 * 
	 * @param nb
	 *            nombre de logs à récupérer.
	 * @return les derniers logs.
	 */
	public LogsResponse getLogs(int nb) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LogsResponse result = new LogsResponse();
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement des logs : connection =null");
			return null;
		}

		try {
			statement = connection.prepareStatement(SQL_SELECT_LOGS);
			statement.setInt(1, nb);
			resultSet = statement.executeQuery();

			int i = 0;
			while (resultSet.next()) {
				LogResponse logResponse = new LogResponse();
				Timestamp dateLog = resultSet.getTimestamp("date_log");
				logResponse.setDate(new SimpleDateFormat(Constant.FORMAT_YYYY_MM_DD_HH_MM_SS).format(dateLog));
				logResponse.setValue(++i + " : " + resultSet.getString("description"));
				result.getLogs().add(logResponse);
			}
			
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la récupération des logs : " + e.getMessage());			
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}

		return result;

	}

	/**
	 * @return
	 */
	public ReturnAutoTest getAutoTest() {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ReturnAutoTest result = new ReturnAutoTest();
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement du dernier auto test : connection =null");
			return null;
		}

		try {
			statement = connection.prepareStatement(SQL_SELECT_AUTO_TEST);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				Timestamp dateDebut = resultSet.getTimestamp("date_debut");
				Timestamp dateFin = resultSet.getTimestamp("date_fin");
				result.setSTART(new SimpleDateFormat(Constant.FORMAT_YYYY_MM_DD_HH_MM_SS).format(dateDebut));
				result.setEND(new SimpleDateFormat(Constant.FORMAT_YYYY_MM_DD_HH_MM_SS).format(dateFin));
				result.setSTATE(resultSet.getString("libelle"));
				result.setDURATION((dateFin.getTime() - dateDebut.getTime()) / 1000);
			}
		
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la récupération des logs : " + e.getMessage());			
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();

			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}

		return result;
	}

	/**
	 * @param ticketQuery
	 * @param date
	 * @param machine
	 * @param login
	 */
	public int saveTicket(TicketQuery ticketQuery, String login, String machine, LocalDateTime date)
			throws SQLException {
		PreparedStatement statement = null;
		int result = 0;

		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement du dernier auto test : connection =null");
		}

		try {
			statement = connection.prepareStatement(SQL_INSERT_TICKET_WS);
			statement.setInt(1, ticketQuery.getIdOCR());
			statement.setInt(2, ticketQuery.getIdProduit());
			statement.setString(3, ticketQuery.getType());
			statement.setInt(4, ticketQuery.getPriority());
			SQLXML xmlVar = connection.createSQLXML();
			xmlVar.setString(ticketQuery.getContent());
			statement.setSQLXML(5, xmlVar);
			statement.setString(6, login);
			statement.setString(7, machine);
			if (ConfigModule.getInstance().getTypeModule() != null){
				statement.setInt(8,ConfigModule.getInstance().getModule(MainThread.getModuleName()).getId());
			}else{
				// TODO: ajouter ds la base par le module
				statement.setInt(6, getModuleIdByModuleName(Helper.getProperty("module.name")));
			}
			statement.setTimestamp(9, Timestamp.valueOf(date));
			statement.setString(10, ticketQuery.getModeApprovisionnement());
			statement.setInt(11, Integer.parseInt(defaultIfEmptyOrNullOrException("0", ()->ticketQuery.getIdLivraison())));
			result = statement.executeUpdate();

		} catch (SQLException e) {
			logger.error("Erreur lors de l'insertion du ticket dans la table encours : " + e.getMessage());
			throw (e);
		}

		finally {
			try {				
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}

		return result;

	}
	
	/**
	 * Retrieves the module id by his name
	 * @param name
	 * @return
	 */
	public Integer getModuleIdByModuleName(String name){
		//String name = Helper.getProperty("module.name");
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Integer result =-1;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement de type module : connection =null");
			return result;
		}

		try {
			statement = connection.prepareStatement(SQL_SELECT_TYPE_MODULE_ID_BY_NAME);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				result = resultSet.getInt("id");
			}
		
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la récupération du module : " + e.getMessage());			
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();

			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}

		return result;
	}

	/**
	 * @param ticketQuery
	 * @param date
	 * @param machine
	 * @param login
	 */
	public int saveTicketBanette(TicketQuery ticketQuery, String login, String machine, LocalDateTime date)
			throws SQLException {

		// return 1 ;
		PreparedStatement statement = null;
		int result = 0;

		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement du dernier auto test : connection =null");
		}

		try {
			statement = connection.prepareStatement(SQL_INSERT_TICKET_BANETTE);
			statement.setString(1, ticketQuery.getType());
			statement.setInt(2, ticketQuery.getPriority());
			SQLXML xmlVar = connection.createSQLXML();
			xmlVar.setString(ticketQuery.getContent());
			statement.setSQLXML(3, xmlVar);
			statement.setString(4, login);
			statement.setString(5, machine);
			if (ConfigModule.getInstance().getTypeModule() != null){
				statement.setInt(6,ConfigModule.getInstance().getModule(MainThread.getModuleName()).getId());
			}else{			
				statement.setInt(6, getModuleIdByModuleName(Helper.getProperty("module.name")));
			}
			statement.setTimestamp(7, Timestamp.valueOf(date));
			statement.setString(8, ticketQuery.getModeApprovisionnement());
			statement.setInt(9, Integer.parseInt(defaultIfEmptyOrNullOrException("0", ()->ticketQuery.getIdLivraison())));
			result = statement.executeUpdate();

		} catch (SQLException e) {
			logger.error("Erreur lors de l'insertion du ticket dans la table encours : " + e.getMessage());
			throw (e);
		}

		finally {
			try {
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}

		return result;

	}

	/**
	 * @param filePath
	 * @return
	 */
	public boolean isTicketProcessed(String filePath) {
		// return false;
		PreparedStatement statement = null;
		boolean result = true;
		ResultSet resultSet = null;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de recherche du fichier  <" + filePath + ">  : connection =null");
		} else
			try {
				statement = connection.prepareStatement(SQL_SLECT_FILE_PATH);
				statement.setString(1, filePath.trim());
				resultSet = statement.executeQuery();
				//
				result = resultSet.next();

			} catch (SQLException e) {
				logger.error("Erreur de recherche du fichier  <" + filePath + ">  : " + e.getMessage());
				try {
					resultSet.close();
				} catch (SQLException e1) {
					logger.error("Erreur de fermeture de resultSet : "+e1.getMessage());
				}
			}

			finally {
				try {
					if (resultSet != null) resultSet.close();
					if (statement != null) statement.close();
					if (connection != null) connection.close();
				} catch (SQLException e) {
					logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
				} catch (Throwable ex) {
					logger.trace(
							"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
							ex);
				}
			}

		return result;

	}

	/**
	 * @param idTypeModule
	 * @param machine
	 * @param login
	 * @param portWebService
	 * @param date
	 */
	public int subscribeModule(Integer idTypeModule, String machine, String login, Integer portWebService,
			Integer portSupervision, LocalDateTime dateLastProcess, LocalDateTime date) throws SQLException {
		PreparedStatement statement = null;
		int result = 0;

		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur d'inscription du module dans la base de données : connection =null");
			return -1;
		}

		try {
			statement = connection.prepareStatement(SQL_INSERT_MODULE);
			statement.setInt(1, idTypeModule);
			statement.setString(2, machine);
			statement.setString(3, login);
			statement.setInt(4, portWebService);
			statement.setInt(5, portSupervision);
			statement.setTimestamp(6, Timestamp.valueOf(dateLastProcess));
			statement.setTimestamp(7, Timestamp.valueOf(date));
			result = statement.executeUpdate();

		} catch (SQLException e) {
			logger.error("Erreur lors de l'insertion du module dans la table production.modules : " + e.getMessage());
			throw (e);
		}

		finally {
			try {				
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}

		return result;

	}

	/**
	 * Rechercher la liste des tickets ,bloqués en cours de traitement, depuis
	 * la base de données.
	 * 
	 * @return
	 */
	public List<TicketQuery> getTicketsInProgress(String machine) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<TicketQuery> result = new ArrayList<>();
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de récupération de ticket bloqué : connection =null");
		} else
			try {
				statement = connection.prepareStatement(SQL_SELECT_TICKETS_EN_COURS);
				statement.setString(1, machine);
				resultSet = statement.executeQuery();

				while (resultSet.next()) {

					SQLXML ticketContent = resultSet.getSQLXML("contenu_ticket");
					TicketQuery ticket = (TicketQuery) Helper.unmarshallFromString(TicketQuery.class,
							ticketContent.getString());
					result.add(ticket);

				}

			} catch (SQLException e) {
				logger.error("Erreur de récupération de ticket bloqué  : " + e.getMessage());
				try {
					resultSet.close();
				} catch (SQLException e1) {
					logger.error("Erreur de fermeture de resultSet : "+e1.getMessage());
				}
			}

			finally {
				try {
					if (resultSet != null) resultSet.close();
					if (statement != null) statement.close();
					if (connection != null) connection.close();
				} catch (SQLException e) {
					logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
				} catch (Throwable ex) {
					logger.trace(
							"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
							ex);
				}
			}

		return result;
	}

	/**
	 * @param tickets
	 */
	public long insertTickets(List<TicketQuery> tickets) {
		long result = -1;
		StringBuilder sb = new StringBuilder();
		connection = DataSourceManager.getInstance().getConnection();
		LocalDateTime date = LocalDateTime.now();
		int typeModule = ConfigModule.getInstance().getModule(MainThread.getModuleName()).getId();
		try {
			CopyManager cpManager = ((PGConnection) connection).getCopyAPI();
			PushbackReader reader = new PushbackReader(new StringReader(""), 100000);
			tickets.stream().forEach(ticket -> {
				try {
					sb.append("E'").append(Matcher.quoteReplacement(ticket.getContent())).append("',")
							.append(ticket.getType()).append("',").append(ticket.getPriority()).append(",'")
							.append(Helper.getLogin()).append("','").append(Helper.getComputerName()).append("',")
							.append(typeModule).append(",'").append(Timestamp.valueOf(date)).append("'").append("\n");
				} catch (Exception e) {
					logger.error("Erreur d'insertion des tickets dans la table encours : " + e.getMessage());

				}
			});

			reader.unread(sb.toString().toCharArray());
			logger.error(sb.toString());
			result = cpManager.copyIn(SQL_INSERT_TICKET_BANETTE_BATCH, reader);
			sb.delete(0, sb.length());

		} catch (SQLException e) {
			logger.error("Erreur d'insertion de tickets dans la table encours : " + e.getMessage());
		} catch (IOException e) {
			logger.error("Erreur d'insertion de tickets dans la table encours : " + e.getMessage());
		} finally {
			try {
				if (connection != null) connection.close();
				
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
			} catch (Throwable ex) {
				logger.trace("Une exception inattendue s'est produite lors de la fermeture de la connection : ", ex);
			}
		}

		return result;
	}

	/**
	 * @param idTypeModule
	 * @param machine
	 * @param login
	 * @param wsPort
	 * @return
	 */
	public boolean isModuleRunning(int idTypeModule, String machine, String login, int wsPort) {
		PreparedStatement statement = null;
		boolean result = true;
		ResultSet resultSet = null;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de recherche de module : connection =null");
		} else
			try {
				statement = connection.prepareStatement(SQL_SELECT_MODULE);
				statement.setInt(1, idTypeModule);
				statement.setString(2, machine);
				statement.setString(3, login);
				statement.setInt(4, wsPort);
				resultSet = statement.executeQuery();
				result = resultSet.next();

			} catch (SQLException e) {
				if(resultSet!=null){
					try {
						resultSet.close();
					} catch (SQLException e1) {
						logger.error("Erreur de fermeture de resultSet : "+e1.getMessage());
					}
				}
				logger.error("Erreur de recherche du module : " + e.getMessage());
			}

			finally {
				try {
					if (resultSet != null) resultSet.close();
					if (statement != null) statement.close();
					if (connection != null) connection.close();
				} catch (SQLException e) {
					logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
				} catch (Throwable ex) {
					logger.trace(
							"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
							ex);
				}
			}

		return result;
	}

	/**
	 * @param idTypeModule
	 * @param machine
	 * @param login
	 * @param wsPort
	 */
	public boolean unSubscribeModule(Integer idTypeModule, String machine, String login, int wsPort) {
		PreparedStatement statement = null;
		boolean result = true;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de suppression du module : connection =null");
		} else
			try {
				statement = connection.prepareStatement(SQL_DELETE_MODULE);
				statement.setInt(1, idTypeModule);
				statement.setString(2, machine);
				statement.setString(3, login);
				statement.setInt(4, wsPort);
				int resultExecution = statement.executeUpdate();
				result = resultExecution > 0;

			} catch (SQLException e) {
				logger.error("Erreur de suppression du module : " + e.getMessage());
			}

			finally {
				try {					
					if (statement != null) statement.close();
					if (connection != null) connection.close();
				} catch (SQLException e) {
					logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
				} catch (Throwable ex) {
					logger.trace(
							"Une exception inattendue s'est produite lors de la fermeture du statement/connection : ",
							ex);
				}
			}

		return result;

	}
		
	/**
	 * 
	 * @param logs
	 * @param flag
	 * @return
	 */
	public int addTraceIntoLogs(Logs logs ,boolean flag) throws SQLException{
		PreparedStatement statement = null;
		int result = 0;
		connection = DataSourceManager.getInstance().getConnection();
		ResultSet resultSet = null;
		try {
			if (connection == null) {
				logger.error("Erreur de chargement du dernier auto test : connection =null");
			}else{
				if(flag){
					PreparedStatement ps =  connection.prepareStatement(SQL_SEQ_LOGS);			
					resultSet = ps.executeQuery();
					int seq_logs_encours=0;
					if(resultSet.next()) {
						seq_logs_encours = resultSet.getInt("seq");
					}
					
					logs.setId(seq_logs_encours);
					
					if(ps !=null){
						ps.close();
					}
					
					statement =  connection.prepareStatement(SQL_INSERT_TRACE_LOGS);
					statement.setInt(1, seq_logs_encours);
					statement.setString(2, logs.getIdOcr());
					statement.setString(3, logs.getIdProduit());
					statement.setString(4, logs.getLogin());
					statement.setString(5, logs.getMachine());
					statement.setString(6, logs.getDescription());		
					statement.setTimestamp(7, Timestamp.valueOf(logs.getDateDebut()));
					statement.setString(8, logs.getModule());
					result = statement.executeUpdate();
				}else{
					statement =  connection.prepareStatement(SQL_UPDATE_TRACE_LOGS);
					statement.setTimestamp(1, Timestamp.valueOf(logs.getDateFin()));
					statement.setInt(2, logs.getId());
					result = statement.executeUpdate();
				}
			}
		} catch (SQLException e) {
			logger.error("Erreur lors de l'insertion du ticket dans la table encours : " + e.getMessage());			
			throw e;
		}
		finally {
			try {

				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}
		return result;
	}
	
	/**
	 * Update status for each step in to process
	 * @param filename
	 * @param status
	 * @return
	 * @throws SQLException
	 */
	public int updateStatus(Integer idLivraison,String status){
		PreparedStatement statement = null;			
		int result = 0;
		connection = DataSourceManager.getInstance().getConnection();
		if(connection == null){
			return -1;
		}
		try {
			statement = connection.prepareStatement(SQL_UPDATE_STATUS_LIVRAISON_NUMERIQUE);
			statement.setString(1, status);
			statement.setInt(2, idLivraison);
			result = statement.executeUpdate();
		} catch (SQLException e) {
			logger.error("Erreur d'update du status dans la table livraison_numerique : " + e.getMessage());			
		}
		finally {
			try {
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
			} catch (Throwable ex) {
				logger.trace("Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",ex);
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param logs
	 * @return
	 * @throws SQLException
	 */
	public int traceLoggerInfos(Logs logs){
		PreparedStatement statement = null;
		int result = 0;
		connection = DataSourceManager.getInstance().getConnection();		
		try {
			if (connection == null) {
				logger.error("Erreur de chargement du dernier auto test : connection =null");
			}else{						
				statement =  connection.prepareStatement(SQL_INSERT_TRACE_LOGS_DATE_FIN);				
				statement.setString(1, logs.getIdOcr());
				statement.setString(2, logs.getIdProduit());
				statement.setString(3, logs.getLogin());
				statement.setString(4, logs.getMachine());
				statement.setString(5, logs.getDescription());		
				statement.setTimestamp(6, Timestamp.valueOf(logs.getDateDebut()));
				statement.setTimestamp(7, Timestamp.valueOf(logs.getDateFin()));
				statement.setString(8, logs.getModule());
				result = statement.executeUpdate();				
			}
		} catch (SQLException e) {
			logger.error("Erreur lors de l'insertion du ticket dans la table logs : " + e.getMessage());	
			
		}
		finally {
			try {
				
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}
		return result;
	}
	
	/**
	 * Retrieve all resources by module name
	 * @param moduleName : Nom du module (ex. attributs)
	 * @return la liste des ressources pour un module donné
	 * @throws SQLException
	 */
	public List<Resource> getResources(String moduleName, String resourceType) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResourceRowMapper resourceRowMapper = new ResourceRowMapper();
		List<Resource> listResources = new ArrayList<Resource>();

        connection = DataSourceManager.getInstance().getConnection();
		
		try{
			if (connection == null){
				logger.error("Erreur de connection à la base de données ");
			}else{		
				statement = connection.prepareStatement(SQL_SELECT_REQUETE_RESSOURCES);
				statement.setString(1, resourceType);
				statement.setString(2, moduleName);
				resultSet = statement.executeQuery();
				listResources = resourceRowMapper.mapListRow(resultSet);
			}
			if(resultSet != null){
				resultSet.close();
			}
		}catch(SQLException e){
			logger.error("Erreur lors de l'exécution de la requête select sur la table ressources : " +e.getMessage());
			if (resultSet!=null){
					resultSet.close();
				}
		}
		finally{
			try{
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			}catch(Exception e){
				logger.info("Erreur de fermeture de Statement ou Connection ou ResultSet : ", e.getMessage());
			}
		}
		return listResources;
	}
	
	/**
	 * Retrieve the Livraison_numrique by id 
	 * @param idLivNum
	 * @return la livraison_numerique (Reception) correspondant à l'identifiant idLivNum 
	 * @throws SQLException
	 */
	public Reception getReception (Integer idLivNum) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Reception reception = new Reception();
		
        connection = DataSourceManager.getInstance().getConnection();
        
		try{
			if (connection == null){
				logger.error("Erreur de connection à la base de données ");
			}else{		
				statement = connection.prepareStatement(SQL_SELECT_CONTENU_PIVOT_LIVRAISON_NUMERIQUE_BY_ID);
				statement.setInt(1, idLivNum);
				resultSet = statement.executeQuery();
				
				if((resultSet != null) && (resultSet.next()) ){
						SQLXML contenuXML = resultSet.getSQLXML("contenu_pivot");
						reception = (Reception) Helper.unmarshallFromString(Reception.class,
								contenuXML.getString());
				}
			}
			
		}catch(SQLException e){
			logger.error("Erreur lors de l'exécution de la requête select sur la table livraison_numerique : " +e.getMessage());
			if (resultSet!=null){
					try {
						resultSet.close();
					} catch (Exception e1) {
						logger.error("Erreur de fermeture du ResultSet : " +e.getMessage());
					}
				}
		}finally{
			 try{
				 if (resultSet != null) resultSet.close();
				 if (statement != null) statement.close();
				 if (connection != null) connection.close();
			    
			} catch (SQLException e) {
					logger.error("Erreur de fermeture sur connection ou statement ou resultSet : "+e.getMessage());
				}
		}
		return reception;
	}
	
	/**
	 * Retrieve the content of livraison_numrique by id livraison
	 * @param idLivNum
	 * @return les fichiers de livraison_numerique_contenu (ReceptionContenu) correspondant à l'identifiant idLivNum  
	 * @throws SQLException
	 */
	public ReceptionContent getReceptionContent(Integer idLivNum) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ReceptionContent receptionContent = new ReceptionContent();
		
        connection = DataSourceManager.getInstance().getConnection();

        try{
			if (connection == null){
				logger.error("Erreur de connection à la base de données ");
			}else{		
				statement = connection.prepareStatement(SQL_SELECT_CONTENU_PIVOT_LIVRAISON_NUMERIQUE_CONTENU_BY_IDLIVNUM);
				statement.setInt(1, idLivNum);
				resultSet = statement.executeQuery();
				
				if(resultSet != null){
					while(resultSet.next()){
						SQLXML contenuXML = resultSet.getSQLXML("contenu_pivot");
						Fichier fichier = (Fichier) Helper.unmarshallFromString(Fichier.class,
									contenuXML.getString());
						String status = resultSet.getString("status");
						String typeContenu = resultSet.getString("type_contenu"); 
						fichier.setStatus(status);
						fichier.setTypeContenu(typeContenu);
						receptionContent.getFICHIERS().getFICHIER().add(fichier);
					}
				}
			}
			if(resultSet != null){
				resultSet.close();
			}
		}catch(Exception e){
			logger.error("Erreur lors de l'exécution de la requête select sur la table livraison_numerique_contenu : " +e.getMessage());
			if (resultSet!=null){
					try {
						resultSet.close();
					} catch (Exception e1) {
						logger.error("Erreur de fermeture du ResultSet : " +e.getMessage());
						e1.printStackTrace();
					}
				}
		}finally{
			 try{
				 if (resultSet != null) resultSet.close();
				 if (statement != null) statement.close();
				 if (connection != null) connection.close();
			} catch (SQLException e) {
					logger.error("Erreur de fermeture sur connection ou statement ou resultSet : "+e.getMessage());
				}
		}
		return receptionContent;
	}

	/**
	 * Retrieve the Attribut by id livraison
	 * @param idLivNum
	 * @return la liste des attribut nom/valeur de la livraison idLivNum
	 * @throws SQLException
	 */
	public Attributs getAttributs(Integer idLivNum) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Attributs attributs = new Attributs();
		
        connection = DataSourceManager.getInstance().getConnection();		
		
        try{
			if (connection == null){
				logger.error("Erreur de connection à la base de données ");
			}else{		
				statement = connection.prepareStatement(SQL_SELECT_LIBRAISON_NUMERIQUE_ATTRIBUTS);
				statement.setInt(1, idLivNum);
				resultSet = statement.executeQuery();
				
				if(resultSet != null){
					while(resultSet.next()){
						Attribut attribut = new Attribut();
						String name = resultSet.getString("nom");
						String valeur = resultSet.getString("valeur");
						attribut.setName(name);
						attribut.setValeur(valeur);
						attributs.getAttributs().add(attribut);
					}
				}
			}
			if(resultSet != null){
				resultSet.close();
			}        	
        }catch(SQLException e){
        	logger.error("Error sur l'exécution de la requête select sur la table livraison_numerique_attribut : ", e.getMessage());
			if (resultSet!=null){
				try {
					resultSet.close();
				} catch (Exception e1) {
					logger.error("Erreur de fermeture du ResultSet : " +e.getMessage());
				}
			}
        }finally{
        	if (resultSet != null) resultSet.close();
			if (statement != null) statement.close();
			if (connection != null) connection.close();
        }
		return attributs;
	}
	/**
	 * Retrieve the ressource by module name
	 * @param moduleName
	 * @return
	 * @throws SQLException
	 */
	public Resource getResource(String moduleName,String ressourceName) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResourceRowMapper resourceRowMapper = new ResourceRowMapper();
		Resource resource = new Resource();

        connection = DataSourceManager.getInstance().getConnection();
		
		try{
			if (connection == null){
				logger.error("Erreur de connection à la base de données ");
			}else{		
				statement = connection.prepareStatement(SQL_SELECT_REQUEST_RESSOURCE_BY_MODULE_NAME_);
				statement.setString(1, ressourceName);
				statement.setString(2, moduleName);
				resultSet = statement.executeQuery();
				resource = resourceRowMapper.mapRow(resultSet);
			}
			if(resultSet != null){
				resultSet.close();
			}
		}catch(SQLException e){
			logger.error("Erreur lors de l'exécution de la requête select sur la table ressources : " +e.getMessage());
			if (resultSet!=null){
					resultSet.close();
				}
		}
		finally{
			try{
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			}catch(Exception e){
				logger.info("Erreur de fermeture de Statement ou Connection ou ResultSet : ", e.getMessage());
			}
		}
		return resource;
	}

	public ProcessTradExpress getProcessTradExpress(String ressourceName, String moduleName) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
        ProcessTradExpress processTradExpress = new ProcessTradExpress();
		
        connection = DataSourceManager.getInstance().getConnection();
		
		try{
			if (connection == null){
				logger.error("Erreur de connection à la base de données ");
			}else{		
				statement = connection.prepareStatement(SQL_SELECT_PROCESS_TRADEXPRESS);
				statement.setString(1, ressourceName);
				statement.setString(2, moduleName);
				resultSet = statement.executeQuery();
				
				if(resultSet != null && resultSet.next()){
						byte[] contenu = resultSet.getBytes("contenu");
						processTradExpress = (ProcessTradExpress) Helper.unmarshallFromString(ProcessTradExpress.class,
								new String(contenu));
				}
			}
			if(resultSet != null){
				resultSet.close();
			}
		}catch(SQLException e){
			logger.error("Erreur lors de l'exécution de la requête select sur la table ressources : " +e.getMessage());
			if (resultSet!=null){
					try {
						resultSet.close();
					} catch (SQLException e1) {
                        logger.error("Erreur lors de la fermeture du resultSet : ",e1.getMessage());
					}
				}
		}
		finally{
			try{
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			}catch(Exception e){
				logger.info("Erreur de fermeture de Statement ou Connection ou ResultSet : ", e.getMessage());
			}
		}
		return processTradExpress;
	}
	
	/**
	 * Retrieve the Livraison numerique
	 * @param idLivraison
	 * @return
	 */
	public LivraisonNumerique getLivraisonNumerique(Integer idLivraison){
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		LivraisonNumerique livraison = null;
		
        connection = DataSourceManager.getInstance().getConnection();
        
		try{
			if (connection == null){
				logger.error("Erreur de connection à la base de données ");
			}else{		
				statement = connection.prepareStatement(SQL_SELECT_LIVRAISON_NUMERIQUE_BY_ID);
				statement.setInt(1, idLivraison);
				resultSet = statement.executeQuery();
				LivraisonNumeriqueRowMapper romMapper = new LivraisonNumeriqueRowMapper();
				livraison = romMapper.mapRow(resultSet);
			}
			
		}catch(SQLException e){
			logger.error("Erreur lors de l'exécution de la requête select sur la table livraison_numerique : " +e.getMessage());
			if (resultSet!=null){
					try {
						resultSet.close();
					} catch (Exception e1) {
						logger.error("Erreur de fermeture du ResultSet : " +e.getMessage());
					}
				}
		}finally{
			 try{
				 if (resultSet != null) resultSet.close();
				 if (statement != null) statement.close();
				 if (connection != null) connection.close();
			} catch (SQLException e) {
					logger.error("Erreur de fermeture sur connection ou statement ou resultSet : "+e.getMessage());
				}
		}
		return livraison;	
	}
	
	/**
	 * Retrieve infos from xml reception format
	 * @param nomFichier
	 * @return
	 */
	public LivraisonNumerique getLivraisonNumeriqueContenuByIdLivraison(Integer idLivraison){
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		LivraisonNumerique livraison = null;
		
        connection = DataSourceManager.getInstance().getConnection();
        
		try{
			if (connection == null){
				logger.error("Erreur de connection à la base de données ");
			}else{		
				statement = connection.prepareStatement(SQL_SELECT_COUVERTURE_ID_LIVRAISON_NUMERIQUE_CONTENU_BY_ID_LIVRAISON_NUMERIQUE);
				statement.setInt(1, idLivraison);
				resultSet = statement.executeQuery();
				
				LivraisonNumeriqueRowMapper romMapper = new LivraisonNumeriqueRowMapper();
				livraison = romMapper.mapRowCoverPage(resultSet);				
			}
			
		}catch(SQLException e){
			logger.error("Erreur lors de l'exécution de la requête select sur la table livraison_numerique : " +e.getMessage());
			if (resultSet!=null){
					try {
						resultSet.close();
					} catch (Exception e1) {
						logger.error("Erreur de fermeture du ResultSet : " +e.getMessage());
					}
				}
		}finally{
			 try{
				 if (resultSet != null) resultSet.close();
				 if (statement != null) statement.close();
				 if (connection != null) connection.close();
			    
			} catch (SQLException e) {
					logger.error("Erreur de fermeture sur connection ou statement ou resultSet : "+e.getMessage());
				}
		}
		return livraison;	
	}	
	/**
	 * Retrieve the content of resource by resource name
	 * @param resourceName
	 * @return
	 */
	public String getContenuResourceByResourceName(String resourceName){
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String result = null;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement des logs : connection =null");
			return null;
		}

		try {
			statement = connection.prepareStatement(SQL_SELECT_CONTENU_RESSOURCE_BY_RESSOURCE_NAME);
			statement.setString(1, resourceName);			
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				byte[] contenu = resultSet.getBytes("contenu");
				result = new String(contenu);
			}
			
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la récupération de la priorité de scindement : " + e.getMessage());		
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}

		return result;
	}
	
	/** Get value of "key.computername" firstly, if not existed, then get value of "key", if not existed, return defaultValue 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getBestResourceIgnoreCase(String key, String...defaultValue) {
		return defaultIfNullOrException(defaultValue.length>0 ? defaultValue[0] : null, 
				()->DataSourceManager.getInstance().getNamedTemplate().queryForObject(SQL_BEST_RESSOURCE_BY_NOM_CASE_IGNORE,  
						new MapSqlParameterSource().addValue("key", key).addValue("machine", getComputerName()), 
						(rs, rowNum) -> new String(rs.getBytes("contenu"))));
	}
	/**
	 * Update reception with the new attribute
	 * @param idLivraison
	 * @param reception
	 * @throws SQLException 
	 */
	public int updateReception(Integer idLivraison, Reception reception,String uniqueName) {
		int result = 0;
		PreparedStatement statement = null;
		connection = DataSourceManager.getInstance().getConnection();
		if(connection == null){
			return -1;
		}
		try {
			String query = SQL_UPDATE_LIVRAISON_NUMERIQUE_XML;		
			if(uniqueName != null){
				query = SQL_UPDATE_LIVRAISON_NUMERIQUE_XML_BY_UNIQUE_NAME;
			}
			statement = connection.prepareStatement(query);			
			SQLXML xmlVar = connection.createSQLXML();			
			xmlVar.setString(reception.getContent());
			statement.setSQLXML(1, xmlVar);
			
			if(idLivraison != null){
				statement.setInt(2, idLivraison);
			}else{
				statement.setString(2, uniqueName);
			}
			
			result = statement.executeUpdate();
		} catch (SQLException e) {
			logger.error("Erreur d'update du contnu pivot dans la table livraison_numerique : " + e.getMessage());			
		}
		finally {
			try {				
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
			} catch (Throwable ex) {
				logger.trace("Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",ex);
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param attributName
	 * @param attributContent
	 * @param idLivraisonNumerique
	 * @return le résultat de l'exécution de la requête Update ou Insert dans la table livraison_numerique_attribut
	 */
	public int addAttribut(Integer idLivraisonNumerique, Attribut attr) {
		PreparedStatement statement1 = null;
		PreparedStatement statement = null;
		int result = 0;
		ResultSet resultSet = null;
		connection = DataSourceManager.getInstance().getConnection();
		
		try{
			if (connection == null){
				logger.error("Erreur de connection à la base de données ");
			}else{
				statement1 = connection.prepareStatement(SQL_SELECT_LIVRAISON_NUMERIQUE_ATTRIBUT);
				statement1.setInt(1, idLivraisonNumerique);
				statement1.setString(2, attr.getName());
				resultSet = statement1.executeQuery();
				if (resultSet.next()){
					statement = connection.prepareStatement(SQL_UPDATE_LIVRAISON_NUMERIQUE_ATTRIBUT);
					statement.setString(1, attr.getValeur());
					statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
					statement.setInt(3, idLivraisonNumerique);
					statement.setString(4, attr.getName());
					result = statement.executeUpdate();
				}else{
					statement = connection.prepareStatement(SQL_INSERT_LIVRAISON_NUMERIQUE_ATTRIBUT);
					statement.setInt(1, idLivraisonNumerique);
					statement.setString(2, attr.getName());
					statement.setString(3, attr.getValeur());
					statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
					result = statement.executeUpdate();
				}
			}
			if(resultSet != null){
				resultSet.close();
			}
		}catch(Exception e){
			logger.info("Erreur lors de l'insertion dans la table livraison_numerique_attribut : ", e.getMessage());;
		}finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					logger.info("Erreur lors de la fermeture de la connection "+ connection +" : ", e.getMessage());
				}
			}
			if(statement != null){
				try {
					statement.close();
				} catch (SQLException e) {
					logger.info("Erreur lors de la fermeture du statement "+ statement +" : ", e.getMessage());
				}
			}
			if(statement1 != null){
				try{
					statement1.close();
				}catch(Exception e){
					logger.info("Erreur lors de la fermeture du statement "+ statement1 +" : ", e.getMessage());
				}
			}
		}
       return result;
	}
	
	/**
	 * Retrieves all sous_lots by id lot_pere
	 * @param idLotPer
	 * @return
	 */
	public List<Reception> getAllSousLotsOfLotPere(Integer idLotPer){
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		connection = DataSourceManager.getInstance().getConnection();
		List<Reception> sousLots = new ArrayList<Reception>();
		try{
			if (connection == null){
				logger.error("Erreur de connection à la base de données ");
			}else{		
				statement = connection.prepareStatement(SQL_SELECT_ALL_SOUS_LOTS_BY_ID_LIVRAISON_NUMERIQUE);
				statement.setInt(1, idLotPer);
				resultSet = statement.executeQuery();
				LivraisonNumeriqueRowMapper romMapper = new LivraisonNumeriqueRowMapper();
				sousLots = romMapper.mapRowContenuPivotSousLots(resultSet);
			}
			
		}catch(SQLException e){
			logger.error("Erreur lors de l'exécution de la requête select sur la table livraison_numerique : " +e.getMessage());
			if (resultSet!=null){
					try {
						resultSet.close();
					} catch (Exception e1) {
						logger.error("Erreur de fermeture du ResultSet : " +e.getMessage());
					}
				}
		}finally{
			 try{
				 if (resultSet != null) resultSet.close();
				 if (statement != null) statement.close();
				 if (connection != null) connection.close();
			    
			} catch (SQLException e) {
					logger.error("Erreur de fermeture sur connection ou statement ou resultSet : "+e.getMessage());
				}
		}
		return sousLots;
	}
	
	
	/**
	 * @param reception
	 * @param receptionContent
	 * @param seqStatement
	 * @return
	 * @throws SQLException
	 */
	public void generateIdForLivraisonNumeriqueContenu(Reception reception, ReceptionContent receptionContent,
			PreparedStatement seqStatement) throws SQLException {
		ResultSet resultSet;
		int nbreContenuNumerique = receptionContent.getFICHIERS().getFICHIER().size();
		seqStatement.setInt(1, nbreContenuNumerique);
		resultSet = seqStatement.executeQuery();
			
		int index = 0;
		/**Mise à jour des ids livraison dans chaque contenu numerique*/
		while (resultSet.next()) {
			if(index < nbreContenuNumerique){
				reception.getFILES().getFILE().get(index).setIdContenu(String.valueOf(resultSet.getInt("seq")));
				receptionContent.getFICHIERS().getFICHIER().get(index).setIdLivraison(String.valueOf(resultSet.getInt("seq")));
				index++;
			}else{
				break;
			}					
		}	
		if(resultSet!=null){
			resultSet.close();
		}	
	}
	
	/**
	 * Massive integration of CONTENU NUMERIQUE in the database
	 * @param fichiers
	 * @param idliv_num
	 * @return
	 * @throws Exception 
	 */
	public int insertionMassiveContenuNumerique(Connection connection,List<Fichier> fichiers, Integer idliv_num) {
	
		StringBuilder sb = new StringBuilder();
		long result = 0;
		try {
						
			CopyManager cpManager = ((PGConnection) connection).getCopyAPI();	
	
			fichiers.stream().forEach(fichier -> {
				try {				
					String xml = Matcher.quoteReplacement(Helper.removeInvalidXMLCharacters(fichier.getContent()));	
					
					String delimiter ="\t";	
				
					sb.append(fichier.getIdLivraison()).append(delimiter)
					  .append(idliv_num).append(delimiter)
					  .append(fichier.getNom()).append(delimiter) 							
					  .append(PF_CONTENU_PIVOT_ORUGINAL.getCode()).append(delimiter) 
					  .append(fichier.getIdLivraison()).append(delimiter) 		
					  .append(xml).append(delimiter)
					  .append(fichier.getType()).append(delimiter)
					  .append(Constant.PAGE).append("\n");//TODO A fair type contenu couverture,page,....
					
				} catch (Exception e) {
					logger.error("Erreur d'insertion des fichiers dans la table livraison_numerique_contenu : " + e.getMessage());

				}
			});
						
			logger.info(sb.toString());
		
			CopyIn cpIn = cpManager.copyIn(SQL_INSERT_LIVRAISON_CONTENU_NUMERIQUE_BATCH);
			cpIn.writeToCopy(sb.toString().getBytes(Constant.ENCODER_TYPE_UTF_8), 0, sb.length());
			cpIn.endCopy();
			result = cpIn.getHandledRowCount();
			sb.delete(0, sb.length());			
						
		} catch (SQLException | UnsupportedEncodingException e) {
			logger.error("Erreur d'insertion massive info sur le fichiers dans la table livraison_numerique_contenu |ou  encodage non pris en charge: " + e.getMessage());						
		} 
		return (int)result;
	}	
	
	/**
	 * Retrieve id lot pere
	 * @param idLivraison
	 * @return
	 */
	public Integer getIdLotPereByIdSousLot(Integer idLivraison){
		
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Integer result = 0;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement des logs : connection =null");
			return null;
		}

		try {
			statement = connection.prepareStatement(SQL_SELECT_ID_LOT_PERE);
			statement.setInt(1, idLivraison);			
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				result = resultSet.getInt("id_pere");
			}
			
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la récupération de la priorité de scindement : " + e.getMessage());		
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}
		return result;		
	}
	
	/**
	 * 
	 * @param idLivraison
	 * @return
	 */
	public String getLivraisonUniqueNameByIdLivraison(Integer idLivraison){
		
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String result = null;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement des logs : connection =null");
			return result;
		}

		try {
			statement = connection.prepareStatement(SQL_SELECT_UNIQUE_NAME);
			statement.setInt(1, idLivraison);			
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				result = resultSet.getString("nom");
			}
			
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la récupération de la priorité de scindement : " + e.getMessage());		
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}
		return result;
		
	}
	
	/**
	 * Delete ticket from table after treatement by current module
	 * @param idLivraison
	 * @param moduleName
	 * @return
	 */
	public boolean deleteTicketByModuleAfterTreatement(Integer idLivraison,String moduleName){
		
		PreparedStatement statement = null;
		boolean result = true;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de suppression du ticket en attente de traitement par le module : connection =null");
		} else
			try {
				statement = connection.prepareStatement(SQL_DELETE_TICKETS_EN_COURS_APRES_TRAITEMENT_PAR_MODULE);
				statement.setInt(1, idLivraison);
				statement.setString(2, moduleName);				
				int resultExecution = statement.executeUpdate();
				result = resultExecution > 0;

			} catch (SQLException e) {
				logger.error("Erreur de suppression du ticket en attente de traitement par le module : " + e.getMessage());
			}

			finally {
				try {					
					if (statement != null) statement.close();
					if (connection != null) connection.close();
				} catch (SQLException e) {
					logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());
				} catch (Throwable ex) {
					logger.trace(
							"Une exception inattendue s'est produite lors de la fermeture du statement/connection : ",
							ex);
				}
			}

		return result;
	}

	/**
	 * 
	 * @param QUERY
	 * @param scindQte
	 * @param idLivraison
	 * @param status
	 * @return
	 */
	public LinkedHashMap <Integer, List<String>> getScindementCriteres(final String QUERY,Integer scindQte, Integer idLivraison, String status){
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		LinkedHashMap<Integer, List<String>> sousLots = null;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement des logs : connection =null");
			return null;
		}
	
		try {
			statement = connection.prepareStatement(QUERY);
			statement.setInt(1, scindQte);
			statement.setInt(2, idLivraison);	
			statement.setString(3, status);
			resultSet = statement.executeQuery();

			SousLotsRowMapper romMapper = new SousLotsRowMapper();
			sousLots = romMapper.mapRow(resultSet);
			
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la récupération de la priorité de scindement : " + e.getMessage());		
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}

		return sousLots;
	}
	
	public String getLinkPublicationCover(Integer idLivraison){
		
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String result = null;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement des logs : connection =null");
			return null;
		}

		try {
			statement = connection.prepareStatement(SQL_SELECT_COUVERTURE_BY_ID_LIVRAISON);
			statement.setInt(1, idLivraison);			
			resultSet = statement.executeQuery();

			if (resultSet.next()) {			
				result = resultSet.getString("link_cover");
			}
			
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la récupération de la couverture de la page : " + e.getMessage());		
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace("Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",ex);
			}
		}

		return result;
	}
	
	/**
	 * Retrieves ID_GPS by id Livraison
	 * @param idLivraison
	 * @return
	 */
	public ModaliteAppro getModaliteApproByIdLivraison(Integer idSource){
		return DataSourceManager.getInstance().getNamedTemplate().queryForObject(SQL_SELECT_MODALITE_APPRO_BY_ID_SOURCE, new MapSqlParameterSource("id_source", idSource), ModaliteAppro.rowMapper);
	}
	
	/**
	 * Verify if modalite_source_appro exists
	 * @param idSource
	 * @return
	 */
	public boolean existsModaliteApproByIdLivraison(Integer idSource){
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		boolean exists = false;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement des logs : connection =null");
			return false;
		}

		try {
			statement = connection.prepareStatement(SQL_SELECT_MODALITE_APPRO_BY_ID_SOURCE_EXISTS);
			statement.setInt(1, idSource);			
			resultSet = statement.executeQuery();

			if (resultSet.next()) {			
				exists = true;
			}
			
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la récupération de la modalite appro : " + e.getMessage());		
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace("Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",ex);
			}
		}		
		return exists;
	}
	/**
	 * Insert data into table suivi_stape
	 * @param idLivraison
	 * @param module
	 * @param retourXml
	 * @return
	 */
	public int addSuiviEtape(Integer idLivraison,String module,Integer id_parent){	
		PreparedStatement statement = null;		
		int result = 0;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement des logs : connection =null");
			return 0;
		}

		try {
			statement = connection.prepareStatement(SQL_INSERT_SUIVI_ETAPE);
			statement.setString(1, module);
			statement.setString(2, getLogin());
			statement.setString(3, getComputerName());			
			statement.setInt(4, idLivraison);	
			statement.setInt(5, id_parent);
			result = statement.executeUpdate();
			
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors d'ajout dans la table suivi étape : " + e.getMessage());		
		} finally {
			try {				
				if (statement != null) statement.close();
				if (connection != null) connection.close();
				
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace("Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",ex);
			}
		}

		return result;		
	}
	/**
	 * Add attribut into table suiv_etape_attribut
	 * @param idLivraison
	 * @param nom
	 * @param valeur
	 * @return
	 */
	public int addSuiviEtapeAttribut(Integer idLivraison,String module,String nom,String valeur,Integer quantite){	
		PreparedStatement statement = null;		
		int result = 0;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement des logs : connection =null");
			return 0;
		}

		try {
			statement = connection.prepareStatement(SQL_INSERT_SUIVI_ETAPE_ATTRIBUT);
			statement.setInt(1, idLivraison);
			statement.setString(2, module);
			statement.setString(3, nom);
			statement.setString(4, valeur);			
			statement.setInt(5, quantite);				
			result = statement.executeUpdate();
			
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors d'ajout dans la table suivi étape  : " + e.getMessage());		
		} finally {
			try {				
				if (statement != null) statement.close();
				if (connection != null) connection.close();
				
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace("Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",ex);
			}
		}

		return result;					
	}
	/**
	 * 
	 * @param idLivraison
	 * @param module
	 * @param idocr
	 * @param retourXml
	 * @param code_erreur
	 * @param libelle_erreur
	 * @param commentaire
	 * @return
	 */
	public int updateSuiviEtape(Integer idLivraison,String module,String idocr,String retourXml,String code_erreur,String libelle_erreur,String commentaire){
		PreparedStatement statement = null;		
		int result = 0;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement des logs : connection =null");
			return 0;
		}

		try {
			statement = connection.prepareStatement(SQL_UPDATE_SUIVI_ETAPE);
			statement.setString(1, idocr);					
			SQLXML xmlVar = connection.createSQLXML();			
			xmlVar.setString(retourXml);
			statement.setSQLXML(2, xmlVar);
			statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
			statement.setString(4, code_erreur);			
			statement.setString(5, libelle_erreur);
			statement.setString(6, commentaire);
			statement.setInt(7, idLivraison);
			statement.setString(8, module);
			result = statement.executeUpdate();
			
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la mise à jour de la table suivi étape  : " + e.getMessage());		
		} finally {
			try {				
				if (statement != null) statement.close();
				if (connection != null) connection.close();
				
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace("Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",ex);
			}
		}

		return result;					
	}
	
	/**
	 * 
	 * @param idLivraison
	 * @param moduleParent
	 * @return
	 */
	public Integer getIdParentModule(String module){			
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Integer result = null;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement des logs : connection =null");
			return null;
		}

		try {
			statement = connection.prepareStatement(SQL_SELECT_ID_SUIVI_ETAPE_ID_PARENT);		
			statement.setString(1, module);
			resultSet = statement.executeQuery();

			if (resultSet.next()) result = resultSet.getInt("id");
			
		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la récupération id module parent : " + e.getMessage());		
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace("Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",ex);
			}
		}

		return result;
	}
}
