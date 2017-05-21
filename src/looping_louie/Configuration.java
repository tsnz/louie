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
	final static int defaultSpeed = 150;
	final static int defaultLifes = 3;
	final static boolean defaultRandom = false;
	
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

	public int getLifes() {
		return lifes;
	}

	public void setLifes(int lifes) {
		this.lifes = lifes;
	}
}
