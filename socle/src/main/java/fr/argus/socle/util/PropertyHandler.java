package fr.argus.socle.util;

import static com.diffplug.common.base.Errors.rethrow;
import static fr.argus.socle.util.Constant.PROPERTY_FILE_PATH;
import static fr.argus.socle.util.Helper.firstOrDefault;
import static java.lang.String.valueOf;
import static java.lang.System.getenv;
import static java.nio.file.Files.isRegularFile;
import static java.nio.file.Paths.get;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * PropertyHandler est la classe qui permet de charger les propriétés depuis le
 * fichier de configuration config.xml. Ce Singleton est implémenté avec la
 * technique du Holder.
 * 
 * © @author Mongi MIRAOUI 28 avr. 2016
 */
public class PropertyHandler {

	private Properties properties = null;
	private Map<String, String> env = null;
	private Map<String, String> constant = null;
	private static final Logger logger = LogManager.getLogger(PropertyHandler.class);

	/**
	 * Constructeur privé.
	 */
	private PropertyHandler() {
		init();
	}

	/**
	 * Holder
	 */
	private static class PropertyHandlerHolder {
		private final static PropertyHandler instance = new PropertyHandler();
	}

	/**
	 * Point d'accès pour l'instance unique du singleton.
	 */
	public static PropertyHandler getInstance() {
		return PropertyHandlerHolder.instance;
	}

	/**
	 * Cette méthode permet de charger le fichier de configuration du projet.
	 */
	public void init() {
		try {
			InputStream fis = null;
			properties = new Properties();
			
			try {
				fis = new FileInputStream("./"+PROPERTY_FILE_PATH);
			} catch (Exception ex) {
				//nothing to do
			}

			if (fis == null) {
				fis = getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_PATH);
			} else {
				logger.warn("loaded resources from ./"+PROPERTY_FILE_PATH);
			}
			
			if (fis == null) {
				fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTY_FILE_PATH);
				logger.warn("load resources from "+Thread.currentThread().getContextClassLoader().getResource(PROPERTY_FILE_PATH).toURI());
			} else {
				logger.warn("load resources from "+getClass().getClassLoader().getResource(PROPERTY_FILE_PATH).toURI());
			}
			
			properties.loadFromXML(fis);
			fis.close();
			logger.warn("config file="+getProperty("config"));
			
			if (isRegularFile(get("./log4j2.xml"))) {
				((LoggerContext) LogManager.getContext(false)).setConfigLocation(get("./log4j2.xml").toUri());
			}
	          
			properties.putAll(System.getProperties());
			Map<String, String> map = properties.stringPropertyNames().stream().collect(Collectors.toMap(key->key.toLowerCase(), key->properties.getProperty(key)));
			properties.clear();
			properties.putAll(map);
			logger.warn("______________________________________");
			logger.warn("jdbc.host="+properties.getProperty("jdbc.host"));
			logger.warn("jdbc.database.name="+properties.getProperty("jdbc.database.name"));
			env = getenv().keySet().stream().collect(toMap(key->key.toLowerCase(), key->getenv(key)));
			constant = stream(Constant.class.getFields()).collect(toMap(field->field.getName().toLowerCase(), rethrow().wrapFunction(field->valueOf(field.get(null)))));
			logger.info("chargement du fichier properties avec succès.");
		} catch (FileNotFoundException e) {
			logger.error("Erreur de récupération du fichier properties : " + e.getMessage());
		} catch (InvalidPropertiesFormatException e) {
			logger.error("Erreur de récupération du fichier properties : " + e.getMessage());
		} catch (IOException e) {
			logger.error("Erreur de récupération du fichier properties : " + e.getMessage());
		} catch (Exception e) {
			logger.error("Erreur de init: " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Cette méthode retourne la valeur de la propriété passé en paramètre.
	 * 
	 * @param key
	 *            Le nom de la propriété.
	 * @return la valeur de la propriété passé en paramètre.
	 */
	public String getProperty(String key, String...defaultValue) {
		return ofNullable(properties).map(props->props.getProperty(key.toLowerCase())).filter(prop->!prop.startsWith("${")).orElse(firstOrDefault(defaultValue));
	}

	public String getEnvIgnoreCase(String key) {
		return env != null ? env.get(key.toLowerCase()) : null;
	}
	
	public String getConstantIgnoreCase(String key) {
		return constant != null ? constant.get(key.toLowerCase()) : null;
	}
	
}
