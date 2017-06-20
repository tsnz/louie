package looping_louie;

public class RemotelyControlledGame extends Game {
	
	private boolean motor_moving_forward = true;
	
	//Bluetooth connection to the NXT
	NXTBluetoothConnection btConnection;

	public RemotelyControlledGame(Configuration configuration, Display display, NXTBluetoothConnection btConnection) {
		super(configuration, display);
		
		this.btConnection = btConnection;
	}

	@Override
	protected void setupAdditionalSensors() {		
		this.btConnection.setGame(this);
		this.btConnection.setGameReadyToStartLatch(this.gameReadyToStartLatch);
	}

	@Override
	protected void setupGame() {		
		this.motor.setSpeed(this.configuration.getSpeed());	
		this.btConnection.startBTListener();
	}

	@Override
	protected void cleanup() {		
		this.btConnection.stopBTListener();
		this.motorListener.stop();
		this.motor.close();
		for (Sensor sensor : lightSensors) {
			sensor.stop();
		}
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
	
	public void decreaseMotorSpeed() {
		int currentSpeed = this.motor.getSpeed();
		int newSpeed = currentSpeed - Configuration.REMOTE_SPEED_CHANGE_STEP;
		if (newSpeed >= Configuration.MIN_MOTOR_SPEED)
			this.motor.setSpeed(newSpeed);
	}
	
	public void increaseMotorSpeed() {
		int currentSpeed = this.motor.getSpeed();
		int newSpeed = currentSpeed + Configuration.REMOTE_SPEED_CHANGE_STEP;
		if (newSpeed <= Configuration.MAX_MOTOR_SPEED)
			this.motor.setSpeed(newSpeed);
	}

}
