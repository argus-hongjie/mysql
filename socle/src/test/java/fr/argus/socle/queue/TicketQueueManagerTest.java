/**
 * 
 */
package fr.argus.socle.queue;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.argus.socle.ws.model.File;
import fr.argus.socle.ws.model.Files;
import fr.argus.socle.ws.model.TicketQuery;

/**
 * © @author Mongi MIRAOUI 31 mai 2016
 */
public class TicketQueueManagerTest {

	TicketQuery ticket;

	// /**
	// * Test method for {@link
	// fr.argus.socle.queue.TicketQueueManager#addTicket(fr.argus.socle.ws.model.TicketQuery)}.
	// */
	// @Test
	// public void testAddTicket() {
	// fail("Not yet implemented");
	// }

	/**
	 * Test method for
	 * {@link fr.argus.socle.queue.TicketQueueManager#getTicket()}.
	 */
	@Test
	public void testGetTicket() {

		ticket = new TicketQuery();
		ticket.setId(123);
		ticket.setIdProduit(456);
		ticket.setIdOCR(789);
		ticket.setPriority(1);
		ticket.setType("auto_test");
		Files files = new Files();
		for (int i = 0; i < 10; ++i) {
			File file = new File();
			file.setValue("C:\\banette_entree\\fichier1 - Copie (" + i + ").tar");
			files.getFILE().add(file);

		}
		ticket.setFiles(files);

		TicketQueueManager.getInstance().addTicket(ticket);
		assertNotNull(TicketQueueManager.getInstance().getTicket());
	}

	/**
	 * Test method for
	 * {@link fr.argus.socle.queue.TicketQueueManager#getInstance()}.
	 */
	@Test
	public void testGetInstance() {
		assertNotNull(TicketQueueManager.getInstance());
	}

	/**
	 * Test method for {@link fr.argus.socle.queue.TicketQueueManager#getSize()}
	 * .
	 */
	@Test
	public void testGetSize() {
		ticket = new TicketQuery();
		ticket.setId(123);
		ticket.setIdProduit(456);
		ticket.setIdOCR(789);
		ticket.setPriority(1);
		ticket.setType("auto_test");
		Files files = new Files();
		for (int i = 0; i < 10; ++i) {
			// files.getFILE().add("C:\\banette_entree\\fichier1 - Copie (" + i
			// + ").tar");
			File file = new File();
			file.setValue("C:\\banette_entree\\fichier1 - Copie (" + i + ").tar");
			files.getFILE().add(file);
		}
		ticket.setFiles(files);

		TicketQueueManager.getInstance().addTicket(ticket);
		assertTrue(TicketQueueManager.getInstance().getSize() == 1);
	}

}
