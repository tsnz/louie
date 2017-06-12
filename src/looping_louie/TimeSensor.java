package looping_louie;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import lejos.hardware.lcd.LCD;

public class TimeSensor extends Sensor implements Runnable {

	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------
	
	// thread
	protected Thread thread;
	protected CountDownLatch gameReadyToStartLatch;
	
	// times in ms
	private static final int MIN_WAIT_TIME = 3000;
	private static final int MAX_RANDOM_DELAY = 10000;

	protected final ExtendedGame extendedGame;

	Runnable method;

	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------

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
			Random randomGenerator = new Random();
			this.gameReadyToStartLatch.await();

			while (!this.thread.isInterrupted()) {
				long randomDelay = randomGenerator.nextInt(TimeSensor.MAX_RANDOM_DELAY);
				randomDelay += TimeSensor.MIN_WAIT_TIME;
				Thread.sleep(randomDelay); // not Delay from lejos since it is not interruptable!				
				LCD.drawString("Toggeling after " + Long.toString(randomDelay), 0, 5);
				// call method without parameters
				// this.method.accept(null);
				this.method.run();
				// this.extendedGame.newRandomSpeed();
			}

		} catch (InterruptedException e) {
			return;
		}
		return;
	}

	@Override
	protected void stop() {
		this.thread.interrupt();
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
