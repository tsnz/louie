package looping_louie;

import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.utility.TextMenu;

public class Configurator {

	private Configuration configuration;
	private Display display;
	private NXTBluetoothConnection btConnection = null;

	final static int RETURN_KEY_BITMAP = 8192;

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
			"Sensoren konfigurieren", "Use Remote" };
	private static TextMenu options_menu = new TextMenu(options_menu_entries, 1, options_menu_title);

	// game life menu
	private static String lifes_menu_title = "Leben";
	private static String[] lifes_menu_entries = { "1", "2", "3", "5", "8" };
	private static TextMenu lifes_menu = new TextMenu(lifes_menu_entries, 1, lifes_menu_title);

	// game speed menu
	private static String speed_menu_title = "Geschwindigkeit";
	private static String[] speed_menu_entries = { "50", "80", "100", "120", "150" };
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
			case 0: // start standard game
				Game game = new StandardGame(this.configuration, this.display);
				try {
					game.startGame();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case 1: // start extended game
				ExtendedGame extendedGame = new ExtendedGame(this.configuration, this.display, this.btConnection);
				try {
					extendedGame.startGame();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2: // options menu
				this.showOptionsMenu();
				break;
			default: // escape pressed
				exit = true;
			}
			// Wait for player to confirm 'player lost' screen after a game
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
			case 4:
				this.setuptNXTRemote();
				break;
			default:
				exit = true;
			}
		}
	}

	/**
	 * Setup connection to the NXT to which a IR receiver is connected
	 */
	private void setuptNXTRemote() {
		// toggle based on weather a connection is initialized
		if (!(this.btConnection == null)) {
			// disconnect from NXT
			btConnection.disconnect();
			btConnection = null;
			this.display.clearDisplay();
			this.display.displayString("Getrennt", 4, true);
		} else {
			// try to connect to NXT
			this.display.clearDisplay();
			try {
				this.btConnection = new NXTBluetoothConnection();
			} catch (BluetoothConnectionFailed e) {
				this.display.displayString(e.getMessage(), 4, true);
			}
			this.display.displayString("Verbunden", 4, true);
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
			this.display.clearDisplay();
			for (int i = 0; i < 4; i++) {				
				this.display.displayString("Sensor " + Integer.toString(i) + ": " + lightSensors.get(i).getValue(),
						i + 2, false); // display sensor value rounded to 2
										// decimal places
			}

			int pressedKey = Button.waitForAnyEvent();

			if (pressedKey == RETURN_KEY_BITMAP)
				configurationFinished = true;
		}
		for (LightSensor sensor : lightSensors) {
			sensor.stop();
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
