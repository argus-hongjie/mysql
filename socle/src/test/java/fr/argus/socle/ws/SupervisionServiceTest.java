/**
 * 
 */
package fr.argus.socle.ws;

import static org.mockito.ArgumentMatchers.anyInt;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

import org.assertj.core.api.Assertions;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mockito;

import com.sun.jersey.api.client.ClientResponse;

import fr.argus.socle.db.DBClient;
import fr.argus.socle.util.Constant;
import fr.argus.socle.ws.model.Alive;
import fr.argus.socle.ws.model.LogsQuery;
import fr.argus.socle.ws.model.LogsResponse;
import fr.argus.socle.ws.model.ReturnAutoTest;
import fr.argus.socle.ws.model.ReturnAutoTestQuery;

/**
 * © @author Mongi MIRAOUI 17 mai 2016
 */
public class SupervisionServiceTest extends JerseyTest {

	private SupervisionService supervisionService;
	private DBClient dBClient;
	
	@Override
	protected Application configure(){
		supervisionService = new SupervisionService();
		dBClient = Mockito.mock(DBClient.class);
		supervisionService.dbClient = dBClient;
		
		ResourceConfig rc = new ResourceConfig();
		rc.registerInstances(supervisionService);
		rc.packages(Constant.WS_PACKAGE);
		return rc.getApplication();
	}

	/**
	 * Test method for
	 * {@link fr.argus.socle.ws.SupervisionService#getLogs(java.lang.String)}.
	 */
	@Test
	public void testGetLogs() {
		LogsQuery logsQuery = new LogsQuery();
		logsQuery.setNb(15);
		Mockito.doReturn(new LogsResponse()).when(dBClient).getLogs(anyInt());
		
		Response actual = target("/supervision/logs").request().post(Entity.entity(logsQuery, MediaType.APPLICATION_XML));
		
		Assertions.assertThat(actual.getStatusInfo().getStatusCode()).isEqualTo( ClientResponse.Status.OK.getStatusCode());
		Assertions.assertThat(actual.readEntity(String.class)).contains("<LOGS/>");
	}

	/**
	 * Test method for {@link fr.argus.socle.ws.SupervisionService#isAlive()}.
	 */
	@Test
	public void testIsAlive() {
		Alive alive = new Alive();
		JAXBElement<String> query = alive.createALIVE("");
		
		Response actual = target("/supervision").request().post(Entity.entity(query, MediaType.APPLICATION_XML));
		
		Assertions.assertThat(actual.getStatusInfo().getStatusCode()).isEqualTo(ClientResponse.Status.OK.getStatusCode());
		Assertions.assertThat(actual.readEntity(String.class)).contains("<ALIVE>");
	}

//	/**
//	 * Test method for {@link fr.argus.socle.ws.SupervisionService#stopModule()}
//	 * .
//	 */
//	@Test
//	public void testStopModule() {
//		Stop stop = new Stop();
//		JAXBElement<String> query = stop.createSTOP("");
//		
//		Response actual = target("/supervision").request().post(Entity.entity(query, MediaType.APPLICATION_XML));
//
//		Assertions.assertThat(actual.getStatusInfo().getStatusCode()).isEqualTo(ClientResponse.Status.OK.getStatusCode());
//		Assertions.assertThat(actual.readEntity(String.class)).contains("asdf");
//	}

	/**
	 * Test method for
	 * {@link fr.argus.socle.ws.SupervisionService#getLastAutoTest()}.
	 */
	@Test
	public void testGetLastAutoTest() {
		ReturnAutoTestQuery autoTest = new ReturnAutoTestQuery();
		JAXBElement<String> query = autoTest.createReturnAutoTest("");
		Mockito.doReturn(new ReturnAutoTest()).when(dBClient).getAutoTest();
		
		Response actual = target("/supervision").request().post(Entity.entity(query, MediaType.APPLICATION_XML));
		
		Assertions.assertThat(actual.getStatusInfo().getStatusCode()).isEqualTo(ClientResponse.Status.OK.getStatusCode());
		Assertions.assertThat(actual.readEntity(String.class)).contains("<RETURN_AUTO_TEST/>");
	}

}
