/**
 * 
 */
package fr.argus.socle.thread;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.db.ConfigModule;
import fr.argus.socle.db.entity.TypeModule;
import fr.argus.socle.thread.service.Monitorable;
import fr.argus.socle.util.Helper;

/**
 * © @author Mongi MIRAOUI 11 mai 2016
 */
public class MonitoringThread implements Runnable {

	private static final Logger logger = LogManager.getLogger(MonitoringThread.class);
	private static ConcurrentHashMap<Monitorable, Instant> monitoredThreads = new ConcurrentHashMap<>();

	/**
	 * Inscription au thread de gestion de surveillance des time-out.
	 * 
	 * @param monitorable
	 *            thread à surveiller.
	 * @param startTime
	 *            date de lancement du thread.
	 */
	public static void register(Monitorable monitorable, Instant startTime) {
		logger.warn(
				"Inscription au thread de gestion de surveillance des time-out : " + monitorable.getClass().getName());
		try {
			monitoredThreads.put(monitorable, startTime);
		} catch (Exception e) {
			logger.error("Erreur d'inscription au thread de gestion de surveillance des time-out :" + e.getMessage());
		}
	}

	/**
	 * Supprimer le thread de la liste des threads surveillés.
	 * 
	 * @param monitorable
	 *            thread à suppprimer.
	 */
	public static void unRegister(Monitorable monitorable) {
		logger.warn("Suppression du thread de la liste des threads surveillés  : " + monitorable.getClass().getName());
		try {
			monitoredThreads.remove(monitorable);
		} catch (Exception e) {
			logger.error("Erreur de suppression du thread de la liste des threads surveillés :" + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		TypeModule 	currentModule = ConfigModule.getInstance().getModule(MainThread.getModuleName());	
		long processBanetteInDuration = currentModule.getParametres().getScrutationBanetteDelaySeconds();
		long processQueueDuration =  currentModule.getParametres().getScrutationPileDelaySeconds();
		//TODO : 
		long processTicketDuration = Long.valueOf(Helper.getProperty("duration.process.ticket.minutes"));
	
		monitoredThreads.entrySet().stream().forEach(entry -> {
			Instant startTime = entry.getValue();
		
			if (entry.getKey() instanceof ProcessingBanetteInThread) {
				long diffAsMinutes;
				diffAsMinutes = ChronoUnit.MINUTES.between(startTime, Instant.now());
				if (diffAsMinutes > processBanetteInDuration)
					try {
						((ProcessingBanetteInThread) entry.getKey()).shutdown();
						unRegister(entry.getKey());
						logger.error(
								"Erreur de dépassement de temps de traitement pour le thread ProcessingBanetteInThread.");

					} catch (Exception ex) {
						logger.error("Erreur d'arrêt du thread ProcessingBanetteInThread : " + ex);
					}

			} else if (entry.getKey() instanceof ProcessingQueueThread) {
				long diffAsMinutes;
				diffAsMinutes = ChronoUnit.MINUTES.between(startTime, Instant.now());
				if (diffAsMinutes > processQueueDuration)
					try {
						((ProcessingQueueThread) entry.getKey()).shutdown();
						unRegister(entry.getKey());
						logger.error(
								"Erreur de dépassement de temps de traitement pour le thread ProcessingQueueThread.");

					} catch (Exception ex) {
						logger.error("Erreur d'arrêt du thread ProcessingQueueThread : " + ex);
					}

			}

			else if (entry.getKey() instanceof ProcessTicketThread) {
				long diffAsMinutes;
				diffAsMinutes = ChronoUnit.MINUTES.between(startTime, Instant.now());
				if (diffAsMinutes > processTicketDuration)
					try {
						ProcessTicketThread.shutdown();
						unRegister(entry.getKey());
						logger.error(
								"Erreur de dépassement de temps de traitement pour le thread ProcessTicketThread.");

					} catch (Exception ex) {
						logger.error("Erreur d'arrêt du thread ProcessTicketThread : " + ex);
					}

			}

		});

	}

}
