package looping_louie;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;

public class NXTBluetoothConnection implements Runnable {
	
	static String connectionFailed = "Fehlgeschlagen";
	
	final String NXT = "NXT2";
	
	Game game;
	
	public NXTBluetoothConnection() throws BluetoothConnectionFailed {
		NXTCommConnector connector = Bluetooth.getNXTCommConnector();
		NXTConnection connection = connector.connect(NXT, NXTConnection.PACKET);
		
		if (connection == null)
		 throw new BluetoothConnectionFailed(connectionFailed);
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void disconnect() {
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
