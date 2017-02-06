package fr.argus.socle.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.db.entity.Resource;
import fr.argus.socle.db.entity.Signalement;
import fr.argus.socle.db.entity.TypeModule;
import fr.argus.socle.db.mapper.ResourceRowMapper;
import fr.argus.socle.db.mapper.SignalementRowMapper;
import fr.argus.socle.db.mapper.TypeModuleRowMapper;
import fr.argus.socle.db.service.IQueries;
import fr.argus.socle.util.Helper;

/**
 * Cette classe permet de charger les paramètres de configuration du module
 * depuis la base de données.
 * 
 * © @author Mongi MIRAOUI 29 avr. 2016
 */
public class ConfigModule implements IQueries{

	private static final Logger logger = LogManager.getLogger(ConfigModule.class);
	private static List<Resource> resources;
	private static List<Signalement> signalements;
	private static List<TypeModule> typeModules;

	private static Connection connection;
	
	private static PreparedStatement statement;
	private static ResultSet resultSet;
	private static String machine;

	/**
	 * Implémentation du constructeur par la technique du holer. © @author Mongi
	 * MIRAOUI 19 mai 2016
	 */
	private static class ConfigModuleHolder {
		private static final ConfigModule instance = new ConfigModule();
	}

	/**
	 * récupérer une l'instance de la classe.
	 * 
	 * @return
	 */
	public static ConfigModule getInstance() {
		return ConfigModuleHolder.instance;
	}

	/**
	 * Charger la configuration du module depuis la base de données.
	 */
	public static void loadConfig() throws Exception {

		loadResources();
		loadSignalements();
		loadTypeModule();

		try {
			logger.info("<<<<<<<<<<<<====================  Liste des resources ====================>>>>>>>>>>> ");
		} catch (Exception e) {
			logger.error("Erreur de chargement des ressources : ",e.getMessage());
		}
		resources.stream().forEach((resource) -> {
			logger.info("ressource = " + resource.getNom());

		});

		logger.info("<<<<<<<<<<<<====================  Liste des signalements ====================>>>>>>>>>>> ");
		signalements.stream().forEach((signalement) -> {
			logger.info("signalement = " + signalement.getLibelle());

		});

		logger.info("<<<<<<<<<<<<====================  Liste des types de modules ====================>>>>>>>>>>> ");

		typeModules.stream().forEach((module) -> {
			logger.info("typeModule = " + module.getNom());
		});

	}

	/**
	 * charger la table resources.
	 */
	public static void loadResources() {

		resources = new ArrayList<Resource>();

		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement de la table resources : connection =null");
			return;
		}

		try {
			statement = connection.prepareStatement(SQL_SELECT_RESOURCES);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Object obj = Helper.getResultSetRowObject(new ResourceRowMapper(), resultSet);
				if (obj instanceof Resource)
					resources.add((Resource) obj);
			}

		} catch (SQLException e) {
			logger.error("Erreur de chargement de la table resources : " + e.getMessage());
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace(
						"Une exception inattendue s'est produite lors de la fermeture du resultset/statement/connection : ",
						ex);
			}
		}

	}

	/**
	 * charger la table signalements.
	 */
	public static void loadSignalements() {
		signalements = new ArrayList<Signalement>();
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement de la table signalement : connection =null");
			return;
		}

		try {
			statement = connection.prepareStatement(SQL_SELECT_SIGNALEMENTS);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Object obj = Helper.getResultSetRowObject(new SignalementRowMapper(), resultSet);
				if (obj instanceof Signalement)
					signalements.add((Signalement) obj);
			}

		} catch (SQLException e) {
			logger.error("Erreur de chargement de la table Signalement : " + e.getMessage());
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace("Une exception inattendue lors de la fermeture du resultset/statement/connection : ", ex);
			}
		}

	}

	/**
	 * charger la table Type_module.
	 */
	public static void loadTypeModule() {

		typeModules = new ArrayList<TypeModule>();
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de chargement de la table type_module : connection =null");
			return;
		}

		try {
			statement = connection.prepareStatement(SQL_SELECT_TYPE_MODULE);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Object obj = Helper.getResultSetRowObject(new TypeModuleRowMapper(), resultSet);
				if (obj instanceof TypeModule)
					typeModules.add((TypeModule) obj);
			}

		} catch (SQLException e) {
			logger.error("Erreur de chargement de la table resources : " + e.getMessage());
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				logger.error("Erreur de fermeture de la conenxion : " + e.getMessage());

			} catch (Throwable ex) {
				logger.trace("Une exception inattendue lors de la fermeture du resultset/statement/connection : ", ex);
			}
		}
	}

	/**
	 * Load the current machine name 
	 * @param idTypeModule
	 * @param machine
	 * @param login
	 * @param wsPort
	 * @return
	 */
	public static String loadMachine(int idTypeModule, String machine, String login, int wsPort) {
		PreparedStatement statement = null;
		String result = "";
		ResultSet resultSet = null;
		connection = DataSourceManager.getInstance().getConnection();
		if (connection == null) {
			logger.error("Erreur de recherche de module : connection =null");
		} else
			try {
				
				statement = connection.prepareStatement(SQL_SELECT_MODULE_MACHINE_NAME);
				statement.setInt(1, idTypeModule);
				statement.setString(2, machine);
				statement.setString(3, login);
				statement.setInt(4, wsPort);
				statement.setInt(5, wsPort);
				resultSet = statement.executeQuery();
				if( resultSet.next()){
					result = resultSet.getString("machine");
				}

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
					if (statement != null){
						statement.close();
					}
					if(resultSet!=null){
						resultSet.close();
					}
					if (connection != null){
						connection.close();
					}
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
	 * @return the resources
	 */
	public List<Resource> getResources() {
		return resources;
	}

	/**
	 * @return the signalement
	 */
	public List<Signalement> getSignalements() {
		return signalements;
	}

	/**
	 * @return the typeModule
	 */
	public List<TypeModule> getTypeModule() {
		return typeModules;
	}

	/**
	 * recharger de nouveau la configuration du module depuis la base de
	 * données.
	 * 
	 * @throws Exception
	 */
	public static void reloadConfig() throws Exception {

		resources.clear();
		signalements.clear();
		typeModules = null;// .clear();
		loadConfig();

	}

	/**
	 * Récupérer un module depuis la liste de module.
	 * 
	 * @return
	 */
	public TypeModule getModule(String moduleName) {
		return typeModules.stream().filter(module -> module.getNom().equals(moduleName)).findFirst().orElse(null);
	}

	/**
	 * @param idResourceAutoTestSortie
	 * @return
	 */
	public Resource getResourceById(Integer id) {
		return resources.stream().filter(resource -> resource.getId() == id).findFirst().orElse(null);
	}
	
	/**
	 * @return the machine
	 */
	public String getMachine() {
		return machine;
	}
	
}
