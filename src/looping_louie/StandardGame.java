package looping_louie;

public class StandardGame extends Game {
	
	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------
	
	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------

	public StandardGame(Configuration configuration) {
		// use constructor of parent class (Game)
		super(configuration);
	}

	@Override
	protected void setupGame() {
		this.motor.setSpeed(this.configuration.getSpeed());
	}

	@Override
	protected void setupSensors() {
//		Sensor sensor = new LightSensor(SensorPort.S1, 0, this, this.gameReadyToStartLatch);
//		this.sensors.add(sensor);
		this.setupLightSensors();
	}
}
