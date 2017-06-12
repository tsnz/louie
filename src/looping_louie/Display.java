package looping_louie;

import lejos.hardware.lcd.LCD;

public class Display {
	
	final static int DISPLAY_HEIGHT = 8;
	final static int DISPLAY_WIDTH = 18;
	
	public Display()  {
		
	}
	
	/**
	 * Display initial lifes (same for every player)
	 * @param default_lifes number of default lifes
	 */
	public void displayInitialLifes(int default_lifes) {
		for (int i = 0; i < 4; i++) {
			LCD.drawString("Spieler " + Integer.toString(i + 1) + ": " + Integer.toString(default_lifes), 4, i + 2);
		}
	}
	
	/**
	 * Display lifes for player
	 * @param player to display lifes for
	 * @param lifes to ddisplay
	 */
	public void displayLifesForPlayer(int player, int lifes) {
		LCD.clear(player + 2);
		LCD.drawString("Spieler " + Integer.toString(player + 1 ) + ": " + Integer.toString(lifes), 4, player + 2);
	}		
	
	/**
	 * Display which player lost
	 * @param player that lost
	 */
	public void displayLossForPlayer(int player) {
		LCD.clear();
		this.displayString("Spieler " + Integer.toString(player + 1) + " verliert!", 4, true);
	}
	
	/**
	 * Display integer on LCD
	 * @param integerToDisplay integer to on LCD
	 */
	public void displayInteger(int integerToDisplay) {
		LCD.clear();
		LCD.drawInt(integerToDisplay, 6, 4);
	}
	
	/**
	 * Display string with a maximum of 18 chars on LCD
	 * @param stringToDisplay string to display on LCD
	 * @param y row to display string in
	 * @param centered if true string is centered
	 * @throws StringToLongForLCD throws exception if string exceeds 18 chars
	 */
	public void displayString(String stringToDisplay, int y, boolean centered) {
		
		// string not centered if string is longer than 18 chars
		int x = 0;
		
		// check if string size is valid
		if (!(stringToDisplay.length() > Display.DISPLAY_WIDTH)){
			// center string if length is < 18 and centered == true
			if (centered)
				x = (DISPLAY_WIDTH - stringToDisplay.length()) / 2;
		}		
			
		LCD.clear();
		LCD.drawString(stringToDisplay, x, y);
	}

}