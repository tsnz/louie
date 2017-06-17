package looping_louie;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.SensorMode;

public class LightSensor extends Sensor {

	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------

	// sensor
	final NXTLightSensor light_sensor;
	final Port sensor_port;
	final int player_id;
	final static float THRESHOLD = 0.80f;

	protected final Game game;

	// variables needed to measure values from sensor
	SensorMode ambient;
	int sample_size;
	float[] sample;

	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------

	/**
	 * Constructor
	 * 
	 * @param sensor_port
	 *            port for the sensor to use
	 * @param id
	 *            player id of the sensors player
	 * @param game
	 *            game to notify if sensor is breached
	 */
	public LightSensor(Port sensor_port, int id, Game game) {
		// store needed variables to run the game
		this.game = game;
		this.player_id = id;
		this.sensor_port = sensor_port;
		this.light_sensor = new NXTLightSensor(sensor_port);

		// initialize variables needed to query the sensor to save time
		this.ambient = this.light_sensor.getAmbientMode();
		this.sample_size = ambient.sampleSize();
		this.sample = new float[sample_size];

	}

	/**
	 * Constructor for sensor calibration purposes calling other functions than
	 * getValue() and checkForBreach() may cause a NullPointer Exception
	 * 
	 * @param sensor_port
	 *            port for the sensor to use
	 */
	public LightSensor(Port sensor_port) {
		this.game = null;
		this.player_id = 0;
		this.sensor_port = sensor_port;
		this.light_sensor = new NXTLightSensor(sensor_port);
		// initialize variables needed to query the sensor to save time
		this.ambient = this.light_sensor.getAmbientMode();
		this.sample_size = ambient.sampleSize();
		this.sample = new float[sample_size];

	}

	/**
	 * Check if light sensor is breached
	 * 
	 * @return returns true if sensor is breached
	 */
	public boolean checkForBreach() {
		// query sample and compare to threshold
		this.ambient.fetchSample(this.sample, 0);		
		if (this.sample[0] < THRESHOLD) {
			return true;
		}
		return false;
	}

	/**
	 * Notify game to remove a life
	 */
	public void notifyGame() {
		this.game.removeLife(this.player_id);
	}
	
	/**
	 * Reads current value from sensor
	 * 
	 * @return Returns current value
	 */
	public float getValue() {
		this.ambient.fetchSample(this.sample, 0);		
		return this.sample[0];
	}

	@Override
	protected void stop() {
		this.light_sensor.close();
	}
}
