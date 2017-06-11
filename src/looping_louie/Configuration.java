package looping_louie;

public class Configuration {
	
	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------
	
	// stored parameters
	private int speed;	
	private int lifes;
	private boolean random;
	
	
	// Stored defaults
	final static int defaultSpeed = 80;
	final static int defaultLifes = 3;
	final static boolean defaultRandom = false;
	final static int SENSOR_POLLING_DELAY = 10;
	final static int INVINCIBILITY_FRAME = 1000;
	final static int ARM_CONFIGURATION_SPEED = 10;
	final static int ARM_CONFIGURATION_AGLE = 4;
	final static int ARM_CONFIGURATION_SLEEP_TIME = 200;
	
	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public Configuration() {
		this.speed = Configuration.defaultSpeed;
		this.lifes = Configuration.defaultLifes;
		this.random = Configuration.defaultRandom;
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isRandom() {
		return random;
	}

	public void setRandom(boolean random) {
		this.random = random;
	}

	public int getDefaultLifes() {
		return lifes;
	}

	public void setLifes(int lifes) {
		this.lifes = lifes;
	}
	
	public int getPollingDelay() {
		return Configuration.SENSOR_POLLING_DELAY;
	}
	
	public int getInvincibilityFrame() {
		return Configuration.INVINCIBILITY_FRAME;		
	}
}
