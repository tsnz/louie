package looping_louie;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

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

	// sensors, motor and listeners
	protected ArrayList<LightSensor> lightSensors = new ArrayList<>();	
	protected LightSensor configurationSensor;
	protected final RegulatedMotor motor = new NXTRegulatedMotor(MotorPort.D);
	protected MotorListener motorListener;

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
	 * @param configuration to get settings for the game from
	 */
	public Game(Configuration configuration, Display display) {
		// set local variables using parameters
		this.configuration = configuration;
		this.display = display;
		// prepare player lifes
		this.player_lifes = new int[4];
		for (int i = 0; i < 4; i++) {
			this.player_lifes[i] = configuration.getDefaultLifes();
		}
		// create latches to manage game start and stop
		this.gameFinishedLatch = new CountDownLatch(1);
		this.gameReadyToStartLatch = new CountDownLatch(1);	
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

		//
		LightSensor sensor = new LightSensor(ports[0], 0, this);
		this.configurationSensor = sensor;
		this.lightSensors.add(sensor);
		// create a light sensor for each port
		for (int i = 1; i < 4; i++) {
			LightSensor lightsensor = new LightSensor(ports[i], i, this);
			this.lightSensors.add(lightsensor);
		}		
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
		calibrateArm();
		motorListener = new MotorListener(gameReadyToStartLatch, this.motor, this.lightSensors);				
		this.motor.resetTachoCount();
		this.motor.setSpeed(this.configuration.getSpeed());
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
	 * Calibrate arm by moving it to its default starting position
	 * This is necessary for the correct sensor timings
	 */
	public void calibrateArm() {
		
		boolean calibrated = false;
		
		this.motor.setSpeed(Configuration.ARM_CONFIGURATION_SPEED);		
		
		while(!calibrated) {
			
			this.motor.rotate(Configuration.ARM_CONFIGURATION_AGLE);
			
			// check for arms position and move arm to player one
			if(this.lightSensors.get(0).checkForBreach()) {
				calibrated = true;			
			} //else if (this.lightSensors.get(1).checkForBreach()) {				
//				calibrated = true;
//				this.motor.rotate(90);			
//			} else if (this.lightSensors.get(2).checkForBreach()) {
//				calibrated = true;
//				this.motor.rotate(-180);
//			} else if (this.lightSensors.get(3).checkForBreach()) {
//				calibrated = true;
//				this.motor.rotate(-90);
//			}
									
			try {
				Thread.sleep(Configuration.ARM_CONFIGURATION_SLEEP_TIME);
			} catch (InterruptedException e) {
				// can be ignored since no interruption is possible
			}
		}		
	}

	/**
	 * Free up used resources
	 */
	protected abstract void cleanup();

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
			// interruption not possible since this is the main thread
		}
		this.cleanup();
		return;
	}

}
