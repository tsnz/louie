package looping_louie;

import java.util.Random;

public class ExtendedGame extends Game {

	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------

	private boolean motor_moving_forward; // 1 = forward, 0 = backward

	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------

	public ExtendedGame(Configuration configuration) {
		// use constructor of parent class (Game)
		super(configuration);
	}

	@Override
	protected void setupGame() {
		this.motor_moving_forward = true;
		this.motor.setSpeed(this.configuration.getSpeed());
	}

	@Override
	protected void setupSensors() {
//		Sensor sensor1 = new LightSensor(SensorPort.S1, 0, this, this.gameReadyToStartLatch);
//		this.sensors.add(sensor1);
		this.setupLightSensors();
		Sensor sensor2 = new TimeSensor(this, this.gameReadyToStartLatch, () -> this.toggleMotorDirection());
		this.sensors.add(sensor2);
		Sensor sensor3 = new TimeSensor(this, this.gameReadyToStartLatch, () -> this.newRandomSpeed());
		this.sensors.add(sensor3);
	}

	/**
	 * Toggles motor direction
	 */
	public void toggleMotorDirection() {
		if (this.motor_moving_forward == true) {
			this.motor.backward();
			this.motor_moving_forward = false;
		} else {
			this.motor.forward();
			this.motor_moving_forward = true;
		}
	}

	/**
	 * Changes motor speed to a random speed
	 */
	public void newRandomSpeed() {
		Random randomGenerator = new Random();
		int randomSpeed = randomGenerator.nextInt(250);		
		this.motor.setSpeed(randomSpeed);
	}

}
