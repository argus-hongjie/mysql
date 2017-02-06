package fr.argus.socle.db;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import fr.argus.socle.util.IntegrationTest;

/**
 * © @author Mongi MIRAOUI 31 mai 2016
 */
public class DataSourceManagerTest extends IntegrationTest{

	/**
	 * Test method for
	 * {@link fr.argus.socle.db.DataSourceManager#getConnection()}.
	 */
	@Test
	public void testGetConnection() {
		assertNotNull(postgres.getConnection());
	}

	/**
	 * Test method for {@link fr.argus.socle.db.DataSourceManager#getInstance()}
	 * .
	 */
	@Test
	public void testGetInstance() {
		assertNotNull(postgres);
	}

}
