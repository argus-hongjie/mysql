package fr.argus.socle.db;

import static fr.argus.socle.util.Helper.getProperty;
import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import fr.argus.socle.util.PropertyHandler;

/**
 * Cette classe permet la gestion de pool de connexions.
 * 
 * © @author Mongi MIRAOUI 29 avr. 2016
 */
public class DataSourceManager {

	private final PGPoolingDataSource source = new PGPoolingDataSource();
	private static final Logger logger = LogManager.getLogger(DataSourceManager.class);
	private final NamedParameterJdbcTemplate namedTemplate;
	private final JdbcTemplate jdbcTemplate;
	private final DataSourceTransactionManager txManager;

	private DataSourceManager() {
		init();
		namedTemplate = new NamedParameterJdbcTemplate(source);
		jdbcTemplate = new JdbcTemplate(source);
		txManager = new DataSourceTransactionManager(source);
        Flyway flyway = new Flyway();
        flyway.setDataSource(source);
        flyway.setBaselineOnMigrate(true);
        ofNullable(PropertyHandler.getInstance().getProperty("flyway.baselineVersion")).ifPresent(baselineVersion->flyway.setBaselineVersionAsString(baselineVersion));
        flyway.migrate();
	}

	private static class DataSourceManagerHolder {
		private static final DataSourceManager instance = new DataSourceManager();
	}

	/**
	 * Initialisation de pool de connexion.
	 */
	private void init() {
		try {
			source.setDataSourceName(getProperty("jdbc.datasource.name"));
			source.setServerName(getProperty("jdbc.host"));
			source.setDatabaseName(getProperty("jdbc.database.name"));
			source.setUser(getProperty("jdbc.username"));
			source.setPassword(getProperty("jdbc.password"));
			source.setMaxConnections(parseInt(getProperty("jdbc.pool.size")));
		} catch (NumberFormatException e) {
			logger.error("Une erreur s'est produite lors de l'initialisation du pool de connexion : " + e.getMessage());
		}
	}

	public NamedParameterJdbcTemplate getNamedTemplate() {
		return namedTemplate;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	/**
	 * récupérer une connecxion depuis le pool de connexion.
	 * 
	 * @return Une connexion avec la base de données.
	 */
	public Connection getConnection() {
		try {
			return source.getConnection();

		} catch (SQLException e) {
			logger.error("Une erreur s'est produite lors de la récupération de connection : " + e.getMessage());
			return null;
		}

	}

	public static DataSourceManager getInstance() {
		return DataSourceManagerHolder.instance;
	}

	public DataSourceTransactionManager getTxManager() {
		return txManager;
	}

}
