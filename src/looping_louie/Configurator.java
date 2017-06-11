package looping_louie;

import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.utility.TextMenu;

public class Configurator {

	private Configuration configuration;
	private Display display;

	// -----------------------------------------------------------------------------
	// variables
	// -----------------------------------------------------------------------------

	// -----------------------------------------------------------------------------
	// error messages

	static final String errorPausingMenuTrhead = "Menü konnte nicht in den Wartezustand versetzt"
			+ "werden. Spiel kann nicht gestartet werden";

	// -----------------------------------------------------------------------------
	// menus

	// main menu
	private static String display_title = "Looping Louie";
	private static String[] main_menu_entries = { "Standardpiel", "Erweitertes Spiel", "Optionen" };
	private static TextMenu main_menu = new TextMenu(main_menu_entries, 1, display_title);

	// options menu
	private static String options_menu_title = "Optionen";
	private static String[] options_menu_entries = { "Leben", "Geschwindigkeit", "Richtungswechsel",
			"Sensoren konfigurieren" };
	private static TextMenu options_menu = new TextMenu(options_menu_entries, 1, options_menu_title);

	// game life menu
	private static String lifes_menu_title = "Leben";
	private static String[] lifes_menu_entries = { "1", "2", "3", "5", "8" };
	private static TextMenu lifes_menu = new TextMenu(lifes_menu_entries, 1, lifes_menu_title);

	// game speed menu
	private static String speed_menu_title = "Geschwindigkeit";
	private static String[] speed_menu_entries = { "100", "150", "200", "250" };
	private static TextMenu speed_menu = new TextMenu(speed_menu_entries, 1, speed_menu_title);

	// -----------------------------------------------------------------------------
	// functions
	// -----------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public Configurator() {
		// load saved configuration from file system

		// create new default configuration if none is found
		this.configuration = new Configuration();
		this.display = new Display();
	}

	/**
	 * Show initial menu after program start
	 */
	public void startupMainMenu() {

		boolean exit = false;
		// stay in menu until escape is pressed
		while (!exit) {
			LCD.clear();

			// get user selection
			int selected_entry = main_menu.select();

			switch (selected_entry) {
			// start standard game
			case 0:
				Game game = new StandardGame(this.configuration, this.display);
				try {
					game.startGame();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			// start extended game
			case 1:
				ExtendedGame extendedGame = new ExtendedGame(this.configuration, this.display);
				try {
					extendedGame.startGame();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			// options
			case 2:
				this.showOptionsMenu();
				break;
			// escape pressed
			default:
				exit = true;
			}
			// Wait for player to confirm 'player lost' screen
			Button.waitForAnyPress();
		}
	}

	/**
	 * Shows options menu
	 */
	private void showOptionsMenu() {
		boolean exit = false;
		while (!exit) {
			LCD.clear();
			// show available options and get selection
			int selected_entry = Configurator.options_menu.select();
			switch (selected_entry) {
			// change default game lifes
			case 0:
				this.selectGameLifes();
				break;
			// change default game speed
			case 1:
				this.selectGameSpeed();
				break;
			case 3:
				showSensorOutput();
				break;
			default:
				exit = true;
			}
		}
	}

	private void showSensorOutput() {
		LCD.clear();

		// available ports
		Port[] ports = new Port[] { SensorPort.S1, SensorPort.S2, SensorPort.S3, SensorPort.S4 };
		// list to store all created sensors
		ArrayList<LightSensor> lightSensors = new ArrayList<>();
		// create a light sensor for each port
		for (int i = 0; i < 4; i++) {
			LightSensor lightsensor = new LightSensor(ports[i]);
			lightSensors.add(lightsensor);
		}

		boolean configurationFinished = false;
		while (!configurationFinished) {
			for (int i = 0; i < 4; i++) {
				LCD.drawString("Sensor " + Integer.toString(i) + ": " + lightSensors.get(i).getValue() , //Float.toString(lightSensors.get(i).getValue())
						0, i);
			}

			int pressedKey = Button.waitForAnyEvent();
			LCD.drawInt(pressedKey, 0, 6);
			// 8192 = escape bitmap
			if (pressedKey == 8192)
				configurationFinished = true;
		}
		for (LightSensor sensor : lightSensors) {
			sensor.cleanup();
		}
	}

	/**
	 * Shows menu to select default game lifes
	 */
	private void selectGameLifes() {
		LCD.clear();
		int selected_entry = Configurator.lifes_menu.select();
		// use selected entry to get value from entries
		// and parse entry to integer
		this.configuration.setLifes(Integer.valueOf(Configurator.lifes_menu_entries[selected_entry]));
	}

	/**
	 * Shows menu to select default game speed
	 */
	private void selectGameSpeed() {
		LCD.clear();
		int selected_entry = Configurator.speed_menu.select();
		// use selected entry to get value from entries
		// and parse entry to integer
		this.configuration.setSpeed(Integer.valueOf(Configurator.speed_menu_entries[selected_entry]));
	}

	/**
	 * Shows an error message and asks user to confirm
	 * 
	 * @param error_message
	 */
	public void showErrorMessage(String error_message) {

	}

	/**
	 * Write given exception to log file
	 * 
	 * @param e
	 */
	public void writeExceptionToLog(Exception e) {

	}

}
