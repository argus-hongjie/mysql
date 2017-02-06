/**
 * 
 */
package fr.argus.socle.db;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import fr.argus.socle.util.IntegrationTest;

/**
 * © @author Mongi MIRAOUI 31 mai 2016
 */
public class ConfigModuleTest extends IntegrationTest{

	private static final Logger logger = LogManager.getLogger(ConfigModuleTest.class);

	/**
	 * Test method for {@link fr.argus.socle.db.ConfigModule#getInstance()}.
	 */
	@Test
	public void testGetInstance() {
		ConfigModule instance = ConfigModule.getInstance();
		assertNotNull(instance);
	}

	/**
	 * Test method for {@link fr.argus.socle.db.ConfigModule#loadConfig()}.
	 */
	@Test
	public void testLoadConfig() {
		try {
			ConfigModule.loadConfig();
			assertNotNull(ConfigModule.getInstance().getResources());
			assertNotNull(ConfigModule.getInstance().getSignalements());
			assertNotNull(ConfigModule.getInstance().getTypeModule());
		} catch (Exception e) {
			logger.info("Erreur de test de chargement de la configuration du mopdule : " + e.getMessage());
		}

	}

	/**
	 * Test method for {@link fr.argus.socle.db.ConfigModule#loadResources()}.
	 */
	@Test
	public void testLoadResources() {

		ConfigModule.loadResources();
		assertNotNull(ConfigModule.getInstance().getResources());
		assertTrue(ConfigModule.getInstance().getResources().size() > 0);
	}

	/**
	 * Test method for {@link fr.argus.socle.db.ConfigModule#loadSignalements()}
	 * .
	 */
	@Test
	public void testLoadSignalements() {

		ConfigModule.loadSignalements();
		assertNotNull(ConfigModule.getInstance().getSignalements());
		assertTrue(ConfigModule.getInstance().getSignalements().size() > 0);
	}

	/**
	 * Test method for {@link fr.argus.socle.db.ConfigModule#loadTypeModule()}.
	 */
	@Test
	public void testLoadTypeModule() {
		ConfigModule.loadTypeModule();
		assertNotNull(ConfigModule.getInstance().getTypeModule());
	}

	/**
	 * Test method for {@link fr.argus.socle.db.ConfigModule#reloadConfig()}.
	 */
	@Test
	public void testReloadConfig() {
		try {
			assertNotNull(ConfigModule.getInstance().getResources());
			assertNotNull(ConfigModule.getInstance().getSignalements());
			assertNotNull(ConfigModule.getInstance().getTypeModule());
		} catch (Exception e) {
			logger.info("Erreur de test de chargement de la configuration du mopdule : " + e.getMessage());
		}
	}

}
