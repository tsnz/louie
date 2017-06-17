package looping_louie;

import java.util.ArrayList;
import java.util.Random;
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
	static final String START = "START!";
	
	Sound sounds;

	// thread
	protected Thread thread;
	protected CountDownLatch gameFinishedLatch;
	protected CountDownLatch gameReadyToStartLatch;

	// sensors, motor and listeners
	protected ArrayList<LightSensor> lightSensors = new ArrayList<>();		
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
		// resume once game is finished
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
			LightSensor lightsensor = new LightSensor(ports[i], i, this);
			this.lightSensors.add(lightsensor);
		}		
	}

	/**
	 * Setup sensors
	 */
	protected abstract void setupAdditionalSensors();

	/**
	 * Setup prerequisites for game
	 */
	protected abstract void setupGame();

	/**
	 * Start game
	 */
	private void begin() {
		//this.motor.setAcceleration(210);
		this.setupLightSensors();
		this.calibrateArm();
		this.motor.resetTachoCount();
		this.setArmToRandomPosition();
		motorListener = new MotorListener(gameReadyToStartLatch, this.motor, this.lightSensors);				
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
			this.display.displayInteger(3);
			Thread.sleep(1000);
			this.display.displayInteger(2);
			Sound.beep();
			Thread.sleep(1000);
			this.display.displayInteger(1);
			Sound.beep();
			Thread.sleep(1000);
			this.display.displayString(START, 4, true);
			Sound.twoBeeps();
		} catch (InterruptedException e) {
			// can be ignored. Interruption not possible since this is the only running thread.
			// String cannot be too long since it is not dynamically changing
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
			} else if (this.lightSensors.get(1).checkForBreach()) {				
				calibrated = true;							
				this.rotateArm(90);
			} else if (this.lightSensors.get(2).checkForBreach()) {
				calibrated = true;				
				this.rotateArm(-180);
			} else if (this.lightSensors.get(3).checkForBreach()) {
				calibrated = true;								
				this.rotateArm(-90);
			}
									
			try {
				Thread.sleep(Configuration.ARM_CONFIGURATION_SLEEP_TIME);
			} catch (InterruptedException e) {
				// can be ignored since no interruption is possible
			}
		}
		this.rotateArm(2);
	}
	
	private void rotateArm(int degrees) {
		int currentSpeed = this.motor.getSpeed();
		this.motor.setSpeed(50);
		this.motor.rotate(degrees);
		this.motor.setSpeed(currentSpeed);
	}
	
	private void setArmToRandomPosition() {
		Random randomGenerator = new Random();
		int randomPosition = randomGenerator.nextInt(360);
		this.rotateArm(randomPosition);
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
			Sound.beep();
		} else {
			Sound.twoBeeps();
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
		this.setupAdditionalSensors();
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
