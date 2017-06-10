package looping_louie;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;

public abstract class Game implements Runnable {

	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------
	Sound sounds;

	// thread
	protected Thread thread;
	protected CountDownLatch gameFinishedLatch;
	protected CountDownLatch gameReadyToStartLatch;

	// sensors and motor
	protected ArrayList<Sensor> sensors = new ArrayList<>();
	protected final RegulatedMotor motor = new NXTRegulatedMotor(MotorPort.A);

	// configurator
	public final Configuration configuration;

	// display
	protected final Display display;

	// game variables
	protected int[] player_lifes;

	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------

	/**
	 * Constructor
	 * 
	 * @param configuration
	 */
	public Game(Configuration configuration, Display display) {
		this.configuration = configuration;
		this.display = display;
		this.player_lifes = new int[4];
		for (int i = 0; i < 4; i++) {
			this.player_lifes[i] = configuration.getDefaultLifes();
		}
		this.gameFinishedLatch = new CountDownLatch(1);
		this.gameReadyToStartLatch = new CountDownLatch(1);
		try {
			this.startGame();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	/**
	 * Starts game in new thread and waits for it to finish
	 * 
	 * @throws InterruptedException
	 */
	public void startGame() throws InterruptedException {
		// start game in new thread
		this.thread = new Thread(this);
		this.thread.setDaemon(true);
		this.thread.start();
		// resume once game finished
		this.thread.join();
	}

	/**
	 * Setup all available light sensors
	 */
	protected void setupLightSensors() {

		// available ports
		Port[] ports = new Port[] { SensorPort.S1, SensorPort.S2, SensorPort.S3, SensorPort.S4 };

		// create a light sensor for each port
		for (int i = 0; i < 4; i++) {
			Sensor sensor = new LightSensor(ports[i], i, this, this.gameReadyToStartLatch);
			this.sensors.add(sensor);
		}
		// Sensor sensor = new LightSensor(MotorPort.D, 0, this,
		// this.gameReadyToStartLatch);
		// this.sensors.add(sensor);
	}

	/**
	 * Setup sensors
	 */
	protected abstract void setupSensors();

	/**
	 * Setup prerequisites for game
	 */
	protected abstract void setupGame();

	/**
	 * Start game
	 */
	private void begin() {
		this.countdown();
		this.motor.backward();
		this.gameReadyToStartLatch.countDown();
		this.display.displayInitialLifes(this.configuration.getDefaultLifes());
	}

	/**
	 * Count down before game starts
	 */
	public void countdown() {		
		try {
			Sound.beep();
			LCD.clear();
			LCD.drawInt(3, 7, 4);
			Thread.sleep(1000);
			LCD.clear();
			LCD.drawInt(2, 7, 4);			
			Sound.beep();
			Thread.sleep(1000);
			LCD.clear();
			LCD.drawInt(1, 7, 4);
			Sound.beep();
			Thread.sleep(1000);
			LCD.clear();
			LCD.drawString("GO", 6, 4);
			Sound.twoBeeps();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Free up used resources
	 */
	protected void cleanup() {
		this.motor.close();
		for (Sensor sensor : sensors) {
			sensor.stop();
		}
	}

	/**
	 * Remove life from player. Stops game if player reaches 0 lifes
	 * 
	 * @param player
	 */
	public synchronized void removeLife(int player) {
		this.player_lifes[player] += -1;
		this.display.displayLifesForPlayer(player, this.player_lifes[player]);
		if (this.player_lifes[player] >= 1) {
			Sound.twoBeeps();
		} else {
			Sound.beep();
		}
		if (this.player_lifes[player] == 0) {
			this.motor.stop();
			this.display.displayLossForPlayer(player);
			Button.waitForAnyPress();
			this.gameFinishedLatch.countDown();
		}
	}

	@Override
	public void run() {
		LCD.clear();
		this.setupSensors();
		this.setupGame();
		this.begin();
		try {
			this.gameFinishedLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.cleanup();
		return;
	}

}
