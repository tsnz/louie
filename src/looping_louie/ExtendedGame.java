package looping_louie;

import java.util.ArrayList;
import java.util.Random;

public class ExtendedGame extends Game {

	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------

	// array containing all non light sensors
	protected ArrayList<Sensor> additionalSensors;

	// stores current motor direction
	private boolean motor_moving_forward; // 1 = forward, 0 = backward
	// minimal speed for the rng to output
	private int min_speed = 50;
	// maximal speed for the rng to output
	private int max_speed = 150;

	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------

	/**
	 * Constructor
	 * 
	 * @param configuration
	 *            to get settings from
	 * @param display
	 *            to display events on
	 */
	public ExtendedGame(Configuration configuration, Display display) {
		// use constructor of parent class (Game)
		super(configuration, display);
		// initialize array for additional sensors
		this.additionalSensors = new ArrayList<>();
	}

	@Override
	protected void setupGame() {
		this.motor_moving_forward = true;
		this.motor.setSpeed(this.configuration.getSpeed());
		this.motor.setAcceleration(180);
	}

	@Override
	protected void setupAdditionalSensors() {
		// only add if random direction change is activated
		if (this.configuration.getRandomDirectionChange()) {
			Sensor sensor2 = new TimeSensor(this, this.gameReadyToStartLatch, () -> this.toggleMotorDirection());
			this.additionalSensors.add(sensor2);
		}
		Sensor sensor3 = new TimeSensor(this, this.gameReadyToStartLatch, () -> this.newRandomSpeed());
		this.additionalSensors.add(sensor3);
	}

	/**
	 * Toggles motor direction
	 */
	public void toggleMotorDirection() {
		// check motor direction and change direction to opposite
		if (this.motor_moving_forward == true) {
			this.motor.forward();
			this.motor_moving_forward = false;
		} else {
			this.motor.backward();
			this.motor_moving_forward = true;
		}
	}

	/**
	 * Changes motor speed to a random speed
	 */
	public void newRandomSpeed() {
		Random randomGenerator = new Random();
		// new random speed using the given offsets
		// subtract min_speed to prevent actual speed exceeding max_speed
		int randomSpeed = randomGenerator.nextInt(this.max_speed - this.min_speed);
		// add min_speed to generated speed
		this.motor.setSpeed(randomSpeed + this.min_speed);
	}

	private void setupBTConnection() {

	}

	@Override
	protected void cleanup() {
		this.motorListener.stop();
		this.motor.close();
		// stop all light sensors
		for (Sensor sensor : lightSensors) {
			sensor.stop();
		}
		// stop all additional sensors
		for (Sensor sensor : additionalSensors) {
			sensor.stop();
		}
	}

}
