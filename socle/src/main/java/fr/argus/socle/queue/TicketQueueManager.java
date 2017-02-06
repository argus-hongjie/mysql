package fr.argus.socle.queue;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import fr.argus.socle.ws.model.TicketQuery;

/**
 * © @author Mongi MIRAOUI 18 mai 2016
 */
public class TicketQueueManager {

	private PriorityBlockingQueue<TicketQuery> ticketQueue = new PriorityBlockingQueue<TicketQuery>();

	private static class TicketQueueManagerHolder {
		private final static TicketQueueManager instance = new TicketQueueManager();
	}

	public void addTicket(TicketQuery ticket) {
		ticketQueue.put(ticket);
	}

	public void addAllTickets(List<TicketQuery> tickets) {
		ticketQueue.addAll(tickets);
	}

	public TicketQuery getTicket() {
		return ticketQueue.poll();
	}

	public static TicketQueueManager getInstance() {
		return TicketQueueManagerHolder.instance;
	}

	public int getSize() {
		return ticketQueue != null ? ticketQueue.size() : 0;
	}

	public void clear() {
		ticketQueue.clear();
	}

}
