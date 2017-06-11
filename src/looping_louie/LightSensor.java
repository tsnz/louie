package looping_louie;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.SensorMode;

public class LightSensor extends Sensor {

	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------	
	final long SENSOR_POLLING_DELAY;
	final long INVINCIBILITY_FRAME;
	
	// sensor
	final NXTLightSensor light_sensor;
	final Port sensor_port;
	final int player_id;
	final float threshhold = 0.80f;
	
	protected  final Game game;
	
	
	// variables needed to measure values from sensor
	SensorMode ambient;
	int sample_size;
	float[] sample;

	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------

	public LightSensor(Port sensor_port, int id, Game game) {
		this.game = game;
		this.player_id = id;
		this.sensor_port = sensor_port;
		this.light_sensor = new NXTLightSensor(sensor_port);		
		this.SENSOR_POLLING_DELAY = game.configuration.getPollingDelay();
		this.INVINCIBILITY_FRAME = game.configuration.getInvincibilityFrame();		
	
		this.ambient = this.light_sensor.getAmbientMode();
		this.sample_size = ambient.sampleSize();
		this.sample = new float[sample_size];
		
	}
	
	public LightSensor(Port sensor_port) {
		this.game = null;
		this.player_id = 0;
		this.sensor_port = sensor_port;
		this.light_sensor = new NXTLightSensor(sensor_port);		
		this.SENSOR_POLLING_DELAY = 0;
		this.INVINCIBILITY_FRAME = 0;		
	
		this.ambient = this.light_sensor.getAmbientMode();
		this.sample_size = ambient.sampleSize();
		this.sample = new float[sample_size];
		
	}
	
	public boolean checkForBreach() {
		
		
		this.ambient.fetchSample(this.sample, 0);
		LCD.drawString(Float.toString(this.sample[0]), 1, 1);
		if (this.sample[0] < threshhold) {
			return true;								
		}
		return false;
	}
	
	public void notifyGame() {
		this.game.removeLife(this.player_id);	
	}
	
	public float getValue() {
		this.ambient.fetchSample(this.sample, 0);
		LCD.drawString(Float.toString(this.sample[0]), 1, 1);
		return this.sample[0];
	}

	@Override
	protected void cleanup() {
		this.light_sensor.close();
	}
}
