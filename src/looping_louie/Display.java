package looping_louie;

import lejos.hardware.lcd.LCD;

public class Display {
	
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
		LCD.drawString("Spieler " + Integer.toString(player + 1) + " verliert!", 2, 4);
	}

}
