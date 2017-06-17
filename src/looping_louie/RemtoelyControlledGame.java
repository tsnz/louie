package looping_louie;

public class RemtoelyControlledGame extends Game {
	
	//Bluetooth connection to the NXT
	NXTBluetoothConnection btConnection;

	public RemtoelyControlledGame(Configuration configuration, Display display, NXTBluetoothConnection btConnection) {
		super(configuration, display);
		
		this.btConnection = btConnection;
	}

	@Override
	protected void setupAdditionalSensors() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setupGame() {
		// TODO Auto-generated method stub

		
	}

	@Override
	protected void cleanup() {		
		if (!(this.btConnection == null))
			this.btConnection.disconnect();
	}

}
