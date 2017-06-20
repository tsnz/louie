package looping_louie;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import lejos.hardware.Bluetooth;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;

public class NXTBluetoothConnection implements Runnable {

	static String connectionFailed = "Fehlgeschlagen";

	final static String REMOTE_EV3_NAME = "EV4";

	private Thread thread;
	private CountDownLatch gameReadyToStartLatch;

	NXTConnection connection;

	DataInputStream bluetoothInStream;

	RemotelyControlledGame game;

	public NXTBluetoothConnection() throws BluetoothConnectionFailed {
		NXTCommConnector connector = Bluetooth.getNXTCommConnector();
		connection = connector.connect(REMOTE_EV3_NAME, NXTConnection.PACKET);

		if (connection == null)
			throw new BluetoothConnectionFailed(connectionFailed);

		this.bluetoothInStream = connection.openDataInputStream();
	}

	public void setGame(RemotelyControlledGame game) {
		this.game = game;
	}

	public void setGameReadyToStartLatch(CountDownLatch gameReadyToStartLatch) {
		this.gameReadyToStartLatch = gameReadyToStartLatch;
	}

	public void disconnect() {
		try {
			this.connection.close();
		} catch (IOException e) {
			// can be ignored
		}
	}

	public void startBTListener() {
		this.thread = new Thread(this);
		this.thread.setDaemon(true);
		this.thread.start();
	}

	public void stopBTListener() {
		this.thread.interrupt();
	}

	@Override
	public void run() {

		try {
			this.gameReadyToStartLatch.await();
		} catch (InterruptedException e) {
			// can be ignored since latch wont be interrupted
		}

		while (!this.thread.isInterrupted()) {

			try {

				String bluetoothIn = this.bluetoothInStream.readUTF();

				// check if game exists
				if (this.game != null) {

					switch (bluetoothIn) {
					case "ENTER":
						this.game.toggleMotorDirection();
						break;
					case "DOWN":
						this.game.decreaseMotorSpeed();
						break;
					case "UP":
						this.game.increaseMotorSpeed();
						break;
					default:
						break;
					}

				}

			} catch (IOException e) {
				LCD.clear();
				LCD.drawString("Error", 0, 3);
			}

		}
		return;

	}

}
