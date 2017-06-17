package looping_louie;

import lejos.hardware.lcd.LCD;

public class Display {

	final static int DISPLAY_HEIGHT = 8;
	final static int DISPLAY_WIDTH = 18; // isn't it 16 characters? ->
											// http://www.lejos.org/nxt/nxj/tutorial/LCD_Sensors/LCD_Sensors.htm

	public Display() {

	}

	/**
	 * Display initial lifes (as shape of diamond)
	 * 
	 * @param default_lifes
	 *            number of default lifes
	 */
	public void displayInitialLifes(int default_lifes) {
		String sDefault_lifes = Integer.toString(default_lifes);

		LCD.drawInt(default_lifes, DISPLAY_WIDTH / 2 - 1, 0); // 4th player's
																// lifes (first
																// line,
																// centred)
		displayString("Spieler 4", 1, true);

		LCD.drawString(sDefault_lifes + "  übrige Leben" + sDefault_lifes, 0, 3);
		LCD.drawString("Sp. 1        Sp. 3", 0, 4);

		displayString("Spieler 2", 6, true);
		LCD.drawInt(default_lifes, DISPLAY_WIDTH / 2 - 1, 7); // 2nd player's
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
		case 3: // player 4
			LCD.clear(0);
			LCD.drawInt(lifes, DISPLAY_WIDTH / 2 - 1, 0);
			break;

		case 1: // player 2
			LCD.clear(7);
			LCD.drawInt(lifes, DISPLAY_WIDTH / 2 - 1, 7);
			break;

		case 0: // player 1
			LCD.clear(0, 3, 1);
			LCD.drawInt(lifes, 0, 3);
			break;

		case 2: // player 3
			LCD.clear(17, 3, 1);
			LCD.drawInt(lifes, 17, 3);
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
		this.displayString("Spieler " + Integer.toString(player + 1) + " verliert!", 4, true);
	}

	/**
	 * Display integer on LCD
	 * 
	 * @param integerToDisplay
	 *            integer to on LCD
	 */
	public void displayCenteredInteger(int integerToDisplay) {
		LCD.drawInt(integerToDisplay, DISPLAY_WIDTH / 2, 4);
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