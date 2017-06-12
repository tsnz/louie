package looping_louie;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TimeSensor extends Sensor implements Runnable {

	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------

	// thread
	private Thread thread;
	private CountDownLatch gameReadyToStartLatch;

	// times in ms
	private static final int MIN_WAIT_TIME = 3000;
	private static final int MAX_WAIT_TIME = 10000;

	@SuppressWarnings("unused")
	private final ExtendedGame extendedGame;

	private Runnable method;

	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------

	/**
	 * Constructor
	 * 
	 * @param extendedGame
	 *            game to notify of random events
	 * @param gameReadyToStart
	 *            latch to wait for to start thread
	 * @param method
	 *            method to call after a random delay
	 */
	public TimeSensor(ExtendedGame extendedGame, CountDownLatch gameReadyToStart, Runnable method) {
		this.extendedGame = extendedGame;
		this.gameReadyToStartLatch = gameReadyToStart;

		this.method = method;

		this.start();
	}

	/**
	 * Starts listener
	 */
	protected void start() {
		this.thread = new Thread(this);
		this.thread.setDaemon(true);
		this.thread.start();
	}

	@Override
	public void run() {
		try {
			// create new RNG to create sleep timings
			Random randomGenerator = new Random();
			this.gameReadyToStartLatch.await();

			while (!this.thread.isInterrupted()) {
				long randomDelay = randomGenerator.nextInt(MAX_WAIT_TIME - MIN_WAIT_TIME);
				randomDelay += TimeSensor.MIN_WAIT_TIME;
				Thread.sleep(randomDelay); // not Delay from lejos since it is
											// not interruptable!
				this.method.run();
			}

		} catch (InterruptedException e) {
			return; // return if sleep is interrupted
		}
		return; // return if thread is interrupted
	}

	@Override
	protected void stop() {
		this.thread.interrupt();
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			// can be ignored thread will not be interrupted
		}
	}
}
