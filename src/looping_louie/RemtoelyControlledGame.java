package looping_louie;

import com.jcraft.jsch.ConfigRepository.Config;

public class RemtoelyControlledGame extends Game {
	
	private boolean motor_moving_forward = true;
	
	//Bluetooth connection to the NXT
	NXTBluetoothConnection btConnection;

	public RemtoelyControlledGame(Configuration configuration, Display display, NXTBluetoothConnection btConnection) {
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
