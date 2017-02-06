package fr.argus.socle.thread;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.thread.service.Monitorable;

/**
 * © @author Mongi MIRAOUI 23 mai 2016
 */
public class ProcessingQueueThread implements Runnable, Monitorable {

	private static final Logger logger = LogManager.getLogger(ProcessingBanetteInThread.class);
	ScheduledExecutorService pool;
	Set<Callable<Boolean>> instances = new HashSet<>();
	private String classNameModule;
	private int nbThreads;
	/**
	 * @param instances
	 */
	public ProcessingQueueThread(String className,int nombreThreadsMax) {
		this.setClassNameModule(className);
		this.nbThreads =nombreThreadsMax;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		preExecute();
		try {
			processQueue();
		} catch (InstantiationException e) {			
			logger.error("Erreur d'intanciation-InstantiationException::processQueue(): ", e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error("Erreu d'accès -IllegalAccessException::processQueue(): ", e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.error("Erreur-ClassNotFoundException::processQueue(): ", e.getMessage());
		}
		postExecute();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.argus.socle.thread.Monitorable#preExecute()
	 */
	@Override
	public void preExecute() {
		MonitoringThread.register(this, Instant.now());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.argus.socle.thread.Monitorable#postExecute()
	 */
	@Override
	public void postExecute() {
		MonitoringThread.unRegister(this);
		logger.info("fin de traitement de la pile ....");

	}

	@SuppressWarnings("unchecked")
	private void processQueue() throws InstantiationException, IllegalAccessException, ClassNotFoundException  {
		logger.info("début  de traitement de la pile ....");

		int cpus = Runtime.getRuntime().availableProcessors();		
		logger.warn("<<<<<<<<<<<<<<<====== Nombre de Processeurs sur la machine est: "+cpus+" =======>>>>>>>>>>>>>>>");
		pool = Executors.newScheduledThreadPool(nbThreads);
		
		
		Class<?> clazz =  Class.forName(this.getClassNameModule());
		for (int i = 0; i < nbThreads; i++) {
			instances.add((Callable<Boolean>)clazz.newInstance());
		}
		
		
		//TODO: Traitement du retour des Threads mamadou
		
		try {
			List<Future<Boolean>> futures = pool.invokeAll(instances);

			futures.stream().forEach((future -> {

				try {
					logger.warn("future : " + future.get());
					//if(!future.get() )
					//	logger.trace("");
					
				} catch (Exception e) {
					logger.error("future error: " + e.getMessage());				
				}
			}));

		} catch (InterruptedException e) {
			logger.error("Une erreur s'est produite lors du traitement de la file à traiter : " + e.getMessage());
		}

		pool.shutdownNow();

	}

	/**
	 * arrêt du thread ProcessingQueueThread.
	 */
	public void shutdown() {
		if (pool != null)
			pool.shutdown();

		while (!pool.isTerminated()) {
			try {
				logger.info("ProcessingQueueThread pas encore terminé ...");
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				logger.error("Erreur d'arret du thread ProcessingQueueThread :" + e.getMessage());
			}
		}
	}

	/**
	 * @return the instanceModule
	 */
	public String getClassNameModule() {
		return classNameModule;
	}

	/**
	 * @param instanceModule the instanceModule to set
	 */
	public void setClassNameModule(String instanceModule) {
		this.classNameModule = instanceModule;
	}

}
