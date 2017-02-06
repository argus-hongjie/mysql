package fr.argus.socle.ws;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.sun.jersey.api.client.ClientResponse;

import fr.argus.socle.db.DBClient;
import fr.argus.socle.queue.TicketQueueManager;
import fr.argus.socle.util.Constant;
import fr.argus.socle.ws.model.File;
import fr.argus.socle.ws.model.Files;
import fr.argus.socle.ws.model.TicketQuery;

/**
 * © @author Mongi MIRAOUI 17 mai 2016
 */
public class ConsumeTicketServiceTest extends JerseyTest{

	 private DBClient dBclient;
	 private ConsumeTicketService consumeTicketService;

    @Override
    protected Application configure() {
    	consumeTicketService = new ConsumeTicketService();
    	//consumeTicketService = Mockito.spy(consumeTicketService);
    	dBclient =  mock(DBClient.class);
    	consumeTicketService.dbClient = dBclient;
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.registerInstances(consumeTicketService);
        resourceConfig.packages(Constant.WS_PACKAGE);
        return resourceConfig.getApplication();
    }
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		TicketQueueManager.getInstance().clear();
	}
	
    @Test
    public void should_post_to_dispatch() throws SQLException {
    	TicketQuery ticket = new TicketQuery();
		ticket.setId(123);
		ticket.setIdProduit(456);
		ticket.setIdOCR(789);
		ticket.setPriority(1);
		ticket.setType("auto_test");
		Files files = new Files();
		for (int i = 1; i < 10; ++i) {
			File file = new File();
			file.setValue("C:\\banette_entree\\fichier1 - Copie (" + i + ").tar");
			files.getFILE().add(file);
		}
		ticket.setFiles(files);
		doReturn(2).when(dBclient).saveTicket(any(TicketQuery.class), any(String.class), any(String.class), any(LocalDateTime.class));

        Response actual = target("/dispatch").request().post(entity(ticket, APPLICATION_XML));

        assertThat(actual.getStatusInfo().getStatusCode()).isEqualTo(ClientResponse.Status.OK.getStatusCode());
        ArgumentCaptor<TicketQuery> captor = ArgumentCaptor.forClass(TicketQuery.class);
        verify(dBclient).saveTicket(captor.capture(), any(String.class), any(String.class), any(LocalDateTime.class));
        TicketQuery value = captor.getValue();
        assertThat(value.getId()).isEqualTo(ticket.getId());
        String ticketReponse = actual.readEntity(String.class);
        assertThat(ticketReponse).contains("<TICKET>"+ticket.getId()+"</TICKET>");
    }
    
    
    @Test
    public void should_post_to_dispatch_when_insert_ticket_into_encour_error() throws SQLException {
    	TicketQuery ticket = new TicketQuery();
		ticket.setId(123);
		ticket.setIdProduit(456);
		ticket.setIdOCR(789);
		ticket.setPriority(1);
		ticket.setType("auto_test");
		Files files = new Files();
		for (int i = 1; i < 10; ++i) {
			File file = new File();
			file.setValue("C:\\banette_entree\\fichier1 - Copie (" + i + ").tar");
			files.getFILE().add(file);
		}
		ticket.setFiles(files);
		doReturn(0).when(dBclient).saveTicket(any(TicketQuery.class), any(String.class), any(String.class), any(LocalDateTime.class));

		Response actual = target("/dispatch").request().post(entity(ticket, APPLICATION_XML));
		
		assertThat(actual.getStatusInfo().getStatusCode()).isEqualTo(ClientResponse.Status.OK.getStatusCode());
		String ticketReponse = actual.readEntity(String.class);
		assertThat(ticketReponse).contains("<ERROR>Un problème est survenu lors de l'insertion du ticket dans la table encours.</ERROR>");
    }
    
    @Test
    public void should_post_to_dispatch_case_exception() throws SQLException {
    	TicketQuery ticket = new TicketQuery();
		ticket.setId(123);
		ticket.setIdProduit(456);
		ticket.setIdOCR(789);
		ticket.setPriority(1);
		ticket.setType("auto_test");
		Files files = new Files();
		for (int i = 1; i < 10; ++i) {
			File file = new File();
			file.setValue("C:\\banette_entree\\fichier1 - Copie (" + i + ").tar");
			files.getFILE().add(file);
		}
		ticket.setFiles(files);
		doThrow(new SQLException("sql error")).when(dBclient).saveTicket(any(TicketQuery.class), any(String.class), any(String.class), any(LocalDateTime.class));
		
		Response actual = target("/dispatch").request().post(entity(ticket, APPLICATION_XML));
		
		assertThat(actual.getStatusInfo().getStatusCode()).isEqualTo(ClientResponse.Status.OK.getStatusCode());
		String ticketReponse = actual.readEntity(String.class);
		assertThat(ticketReponse).contains("<ERROR>Un problème est survenu lors de l'insertion du ticket dans la table encours:sql error</ERROR>");
    }
}
