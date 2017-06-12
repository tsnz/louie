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
	final static int DEFAULT_SPEED = 80;
	final static int DEFAULT_LIFES = 3;
	final static boolean DEFAULT_RANDOM = false;	
	final static int ARM_CONFIGURATION_SPEED = 10;
	final static int ARM_CONFIGURATION_AGLE = 4;
	final static int ARM_CONFIGURATION_SLEEP_TIME = 200;
	final static int MOTOR_LISTENER_SLEEP_TIME = 30;
	
	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public Configuration() {
		this.speed = Configuration.DEFAULT_SPEED;
		this.lifes = Configuration.DEFAULT_LIFES;
		this.random = Configuration.DEFAULT_RANDOM;
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
}
