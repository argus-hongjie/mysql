package fr.argus.socle.db;

import static fr.argus.socle.util.Helper.getComputerName;
import static jersey.repackaged.com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.SQLException;
import java.time.LocalDateTime;

import java.sql.SQLException;
import java.util.stream.IntStream;

import org.junit.Test;

import fr.argus.socle.util.IntegrationTest;
import fr.argus.socle.ws.model.LogsResponse;
import fr.argus.socle.ws.model.TicketQuery;
import jersey.repackaged.com.google.common.collect.ImmutableMap;

/**
 * © @author Mongi MIRAOUI 31 mai 2016
 */
public class DBClientTest extends IntegrationTest{

	private static DBClient client = new DBClient();

	/**
	 * Test method for {@link fr.argus.socle.db.DBClient#getLogs(int)}.
	 * @throws SQLException 
	 */
	@Test
	public void testGetLogs() throws SQLException {
		IntStream.range(0, 10).forEach(i->{
			postgres.getNamedTemplate().update("INSERT INTO production.logs(description) VALUES (:description)", ImmutableMap.of("description", "testGetLogs"));
		});
		postgresTxMgr.commit(postgresStatus);
		
		LogsResponse response = client.getLogs(7);
		
		assertNotNull(response);
		assertTrue(response.getLogs().size() == 7);
		postgres.getNamedTemplate().update("DELETE FROM production.logs WHERE description=:description", ImmutableMap.of("description", "testGetLogs"));
	}

	/**
	 * Test method for {@link fr.argus.socle.db.DBClient#getAutoTest()}.
	 */
	@Test
	public void testGetAutoTest() {
		assertNotNull(client.getAutoTest());
	}

	// /**
	// * Test method for
	// * {@link
	// fr.argus.socle.db.DBClient#saveTicket(fr.argus.socle.ws.model.TicketQuery,
	// java.lang.String, java.lang.String, java.time.LocalDateTime)}
	// * .
	// */
	// @Test
	// public void testSaveTicket() {
	// fail("Not yet implemented");
	// }
	//
	// /**
	// * Test method for
	// * {@link
	// fr.argus.socle.db.DBClient#saveTicketBanette(fr.argus.socle.ws.model.TicketQuery,
	// java.lang.String, java.lang.String, java.time.LocalDateTime)}
	// * .
	// */
	// @Test
	// public void testSaveTicketBanette() {
	// fail("Not yet implemented");
	// }

	/**
	 * Test method for
	 * {@link fr.argus.socle.db.DBClient#isTicketProcessed(java.lang.String)}.
	 */
	@Test
	public void testIsTicketProcessed() {
		assertFalse(client.isTicketProcessed("un chemin de fichier qui n'existe pas ... "));
	}
	
	@Test
	public void testGetBestResourceIgnoreCase(){
		postgres.getNamedTemplate().update("INSERT INTO referentiel.ressources(id, nom, contenu, id_type_module) VALUES (-3, 'key1', 'val1', (select id from referentiel.type_module where nom = 'SOUS_LOTS'))", of());
		postgres.getNamedTemplate().update("INSERT INTO referentiel.ressources(id, nom, contenu, id_type_module) VALUES (-4, 'key1.'||:machine, 'val2', (select id from referentiel.type_module where nom = 'SOUS_LOTS'))", of("machine", getComputerName()));
        assertThat(client.getBestResourceIgnoreCase("key1")).isEqualTo("val2");
        assertThat(client.getBestResourceIgnoreCase("KEY1")).isEqualTo("val2");
        
        postgres.getNamedTemplate().update("INSERT INTO referentiel.ressources(id, nom, contenu, id_type_module) VALUES (-5, 'key2', 'val3', (select id from referentiel.type_module where nom = 'SOUS_LOTS'))", of());
		postgres.getNamedTemplate().update("INSERT INTO referentiel.ressources(id, nom, contenu, id_type_module) VALUES (-6, 'key2.'||:machine, '', (select id from referentiel.type_module where nom = 'SOUS_LOTS'))", of("machine", getComputerName()));
        assertThat(client.getBestResourceIgnoreCase("Key2")).isEmpty();
        
		postgres.getNamedTemplate().update("INSERT INTO referentiel.ressources(id, nom, contenu, id_type_module) VALUES (-7, 'key3', 'val4', (select id from referentiel.type_module where nom = 'SOUS_LOTS'))", of());
        assertThat(client.getBestResourceIgnoreCase("keY3")).isEqualTo("val4");
        
        assertThat(client.getBestResourceIgnoreCase("kEy5", "default")).isEqualTo("default");

	}
	
	@Test
	public void testSuiviEtape(){
		/*DBClient dBclient =  mock(DBClient.class);
		Integer idParent = dBclient.getIdParentModule("ATTRIBUTS");
		assertTrue(idParent == 0);
		assertFalse(dBclient.addSuiviEtape(1112,"DEDOUBLONNEMENT",idParent) > 0);		
		assertFalse(dBclient.updateSuiviEtape(1112,"DEDOUBLONNEMENT","11111111","<doc></doc>","201","ERREUR","Test comm") > 0);
		//doThrow(new SQLException("sql error")).when(dBclient).addSuiviEtape(any(Integer.class), any(String.class), any(Integer.class));
		assertTrue(dBclient.addSuiviEtapeAttribut(1112, "DEDOUBLONNEMENT", "attr", "test_att", 2) > 0);
		 verify(dBclient).addSuiviEtape(any(Integer.class), any(String.class), any(Integer.class));*/
		
	}

}
