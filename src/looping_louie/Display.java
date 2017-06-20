package looping_louie;

import lejos.hardware.lcd.LCD;
import lejos.internal.ev3.EV3LCDManager;

public class Display {

	final static int DISPLAY_HEIGHT = 8;
	final static int DISPLAY_WIDTH = 18; 
	final static String[] PLAYER_COLLOR = {"Blau","Rot","Schwarz","Gelb"}; // player 1 (internal number 0) has color blue

	public Display() {
		
		// hide std out
		EV3LCDManager lcdManager = EV3LCDManager.getLocalLCDManager();
		lcdManager.getLayer("STDOUT").setVisible(false);
	}

	/**
	 * Display initial lifes (as shape of diamond)
	 * 
	 * @param default_lifes
	 *            number of default lifes
	 */
	public void displayInitialLifes(int default_lifes) {		
		LCD.clear();
		displayString("Gelb", 0, true);
		LCD.drawInt(default_lifes, DISPLAY_WIDTH / 2 - 1, 1); // yellow player's
																// lifes (first
																// line,
																// centred)

		LCD.drawInt(default_lifes, 0, 3);	// blue player
		LCD.drawInt(default_lifes, 16, 3);	// black player
		displayString("Spieler", 3, true);
		LCD.drawString("Blau  Leben  Schw", 0, 4);  // word 'life' between blue and black player

		LCD.drawInt(default_lifes, DISPLAY_WIDTH / 2 - 1, 6); 
		displayString("Rot", 7, true);				  // red player's
																// lifes (first
																// line,
																// centred)
	}

	/**
	 * Display lifes for player
	 * 
	 * @param player
	 *            to display lifes for
	 * @param lifes
	 *            to display
	 */
	public void displayLifesForPlayer(int player, int lifes) {
		switch (player) { // sorted from top to bottom
		case 3: // yellow player
			LCD.drawInt(lifes, DISPLAY_WIDTH / 2 - 1, 1);
			break;

		case 1: // blue player
			LCD.drawInt(lifes, DISPLAY_WIDTH / 2 - 1, 6);
			break;

		case 0: // red player
			LCD.drawInt(lifes, 0, 3);
			break;

		case 2: // black player
			LCD.drawInt(lifes, 16, 3);
			break;

		default:
			break;
		}
	}

	/**
	 * Display which player lost
	 * 
	 * @param player
	 *            that lost
	 */
	public void displayLossForPlayer(int player) {
		LCD.clear();
		this.displayString("Spieler " + PLAYER_COLLOR[player] + " verliert!", 4, true);
	}

	/**
	 * Display integer on LCD
	 * 
	 * @param integerToDisplay
	 *            integer to on LCD
	 */
	public void displayCenteredInteger(int integerToDisplay) {
		int length = Integer.toString(integerToDisplay).length();
		LCD.drawInt(integerToDisplay, (DISPLAY_WIDTH - length + 1) / 2, 4);
	}

	/**
	 * Display string with a maximum of 18 chars on LCD
	 * 
	 * @param stringToDisplay
	 *            string to display on LCD
	 * @param y
	 *            row to display string in
	 * @param centered
	 *            if true string is centered
	 * @throws StringToLongForLCD
	 *             throws exception if string exceeds 18 chars
	 */
	public void displayString(String stringToDisplay, int y, boolean centered) {

		// string not centered if string is longer than 18 chars
		int x = 0;

		// check if string size is valid
		if (!(stringToDisplay.length() > Display.DISPLAY_WIDTH)) {
			// center string if length is < 18 and centered == true
			if (centered)
				x = (DISPLAY_WIDTH - stringToDisplay.length()) / 2;
		}
		LCD.drawString(stringToDisplay, x, y);
	}

	/**
	 * Clear LCD display
	 */
	public void clearDisplay() {
		LCD.clear();
	}

}