package looping_louie;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import lejos.hardware.Sound;
import lejos.robotics.RegulatedMotor;

public class MotorListener implements Runnable {

	// variables
	Sound sounds;
	private CountDownLatch start;
	Thread thread;
	RegulatedMotor motor;
	ArrayList<LightSensor> sensors;	
	
	final int SLEEP_TIME;

	public MotorListener(CountDownLatch start, RegulatedMotor motor, ArrayList<LightSensor> sensors) {
		this.start = start;
		this.motor = motor;
		this.sensors = sensors;		
		this.SLEEP_TIME = Configuration.MOTOR_LISTENER_SLEEP_TIME;
		this.start();
	}

	public void start() {
		this.thread = new Thread(this);
		this.thread.setDaemon(true);
		this.thread.start();
	}

	public void stop() {
		this.thread.interrupt();
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			this.start.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (!this.thread.isInterrupted()) {

			int tacho_count = this.motor.getTachoCount();
			int current_position = -(tacho_count % 360);

			switch (current_position) {
			case 359:
			case 0:
			case 1:							
				if (this.sensors.get(0).checkForBreach())
					this.sensors.get(0).notifyGame();
				break;						
			case 89:
			case 90:
			case 91:
				if (this.sensors.get(1).checkForBreach())
					this.sensors.get(1).notifyGame();
				break;			
			case 179:
			case 180:
			case 181:
				if (this.sensors.get(2).checkForBreach())
					this.sensors.get(2).notifyGame();
				break;			
			case 269:
			case 270:
			case 271:
				if (this.sensors.get(3).checkForBreach())
					this.sensors.get(3).notifyGame();
				break;
			default:
				break;
			}

			try {
				Thread.sleep((long) SLEEP_TIME);
			} catch (InterruptedException e) {
				return;
			}
		}
		return;
	}

}
