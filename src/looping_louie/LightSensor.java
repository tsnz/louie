package looping_louie;

import java.util.concurrent.CountDownLatch;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.SensorMode;

public class LightSensor extends Sensor {

	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------	
	final long POLLING_DELAY;
	final long INVINCIBILITY_FRAME;
	
	// sensor
	final NXTLightSensor light_sensor;
	final Port sensor_port;
	final int player_id;
	final float threshhold = 0.85f;
	
	protected  final Game game;

	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------

	public LightSensor(Port sensor_port, int id, Game game, CountDownLatch gameReadyToStart) {
		this.game = game;
		this.player_id = id;
		this.sensor_port = sensor_port;
		this.light_sensor = new NXTLightSensor(sensor_port);
		this.gameReadyToStartLatch = gameReadyToStart;
		this.start();
		this.POLLING_DELAY = game.configuration.getPollingDelay();
		this.INVINCIBILITY_FRAME = game.configuration.getInvincibilityFrame();
	}

	@Override
	public void run() {
		try {

			// get sample size from sensor
			SensorMode ambient = this.light_sensor.getAmbientMode();
			int sample_size = ambient.sampleSize();
			float[] sample = new float[sample_size];

			// wait for game to start
			this.gameReadyToStartLatch.await();

			// poll sensor until game ends
			while (!this.thread.isInterrupted()) {
				this.light_sensor.setFloodlight(true);
				ambient.fetchSample(sample, 0);
				if (sample[0] < threshhold && !this.thread.isInterrupted()) {
					this.game.removeLife(this.player_id);
					Thread.sleep(POLLING_DELAY);
				}
				Thread.sleep(INVINCIBILITY_FRAME);
			}
		} catch (InterruptedException e) {
			// close sensor
			// no action, sensor is closed after catch
		}
		// close thread
		return;
	}

	@Override
	protected void cleanup() {
		this.light_sensor.close();
	}
}
