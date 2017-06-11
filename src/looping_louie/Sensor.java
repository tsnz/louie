package looping_louie;

import java.util.concurrent.CountDownLatch;

public abstract class Sensor {

	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------

	// thread
	protected Thread thread;
	protected CountDownLatch gameReadyToStartLatch;
	
	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------	
	
	/**
	 * Starts listener
	 */
//	protected void start() {
//		this.thread = new Thread(this);
//		this.thread.setDaemon(true);
//		this.thread.start();
//	}
	
	
	
	/**
	 * Stops listener. Returns after thread is closed
	 */
	public void stop() {
		this.cleanup();
	}
	
	/**
	 * free up used resources
	 */
	protected abstract void cleanup();
}
