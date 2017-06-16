package looping_louie;

public class StandardGame extends Game {

	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------

	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------

	public StandardGame(Configuration configuration, Display display) {
		// use constructor of parent class (Game)
		super(configuration, display);
	}

	@Override
	protected void setupGame() {
		this.motor.setSpeed(this.configuration.getSpeed());
	}

	@Override
	protected void setupSensors() {
		this.setupLightSensors();
	}

	@Override
	protected void cleanup() {
		this.motorListener.stop();
		this.motor.close();
		for (Sensor sensor : lightSensors) {
			sensor.stop();
		}
	}

}
