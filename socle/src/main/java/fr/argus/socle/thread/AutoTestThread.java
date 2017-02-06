package fr.argus.socle.thread;

import java.time.Instant;

import fr.argus.socle.thread.service.Monitorable;

/**
 * © @author Mongi MIRAOUI 2 mai 2016
 */
public class AutoTestThread implements Runnable, Monitorable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		preExecute();
		doJob();

		postExecute();

	}

	/**
	 * 
	 */
	private void doJob() {
//		TypeModule module = ConfigModule.getInstance().getModule(Helper.getProperty("module.autotest.name"));
//		Resource resourceAutoTestEntree = ConfigModule.getInstance()
//				.getResourceById(module.getIdResourceAutoTestEntree());
//		Resource resourceAutoTestSortie = ConfigModule.getInstance()
//				.getResourceById(module.getIdResourceAutoTestSortie());

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
	}

}
