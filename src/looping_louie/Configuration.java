package looping_louie;

public class Configuration {

	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------

	// game parameters
	private int speed;
	private int lifes;
	private boolean randomDirectionChange;

	// game defaults
	final static int DEFAULT_SPEED = 80;
	final static int DEFAULT_LIFES = 3;
	final static boolean DEFAULT_RANDOM_DIRECTION_CHANGE = false;

	// game variables
	// -- arm configuration
	final static int ARM_CONFIGURATION_SPEED = 10; // speed at which arm moves
													// during configuration
	final static int ARM_CONFIGURATION_AGLE = 4; // degrees the arm moves each
													// tick during configuration
	final static int ARM_CONFIGURATION_SLEEP_TIME = 200; // pause in between
															// each tick
	// -- motor listener
	final static int MOTOR_LISTENER_SLEEP_TIME = 3; // sleep time between
													// position checks

	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------

	/**
	 * Creates configuration from default values
	 */
	public Configuration() {
		this.speed = Configuration.DEFAULT_SPEED;
		this.lifes = Configuration.DEFAULT_LIFES;
		this.randomDirectionChange = Configuration.DEFAULT_RANDOM_DIRECTION_CHANGE;
	}

	/**
	 * 
	 * @return game speed
	 */
	public int getSpeed() {
		return this.speed;
	}

	/**
	 * 
	 * @param speed speed for the game to run at
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * 
	 * @return True if random direction changes are active, false if not
	 */
	public boolean getRandomDirectionChange() {
		return this.randomDirectionChange;
	}

	/**
	 * 
	 * @param random True to activate random direction changes, false to deactivate them
	 */
	public void setRandomDirectionChange(boolean random) {
		this.randomDirectionChange = random;
	}

	/**
	 * 
	 * @return Initial player lifes
	 */
	public int getLifes() {
		return this.lifes;
	}

	/**
	 * 
	 * @param lifes Initial lifes each player has
	 */
	public void setLifes(int lifes) {
		this.lifes = lifes;
	}
}
