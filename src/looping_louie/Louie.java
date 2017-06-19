package looping_louie;

public class Louie {

	public static void main(String[] args) {
		
		// -----------------------------------------------------------------------------
		// variables
		// -----------------------------------------------------------------------------
		
		// -----------------------------------------------------------------------------
		// functions
		// -----------------------------------------------------------------------------

		// Create configurator and start main menu
		Louie louie = new Louie();
		louie.startProgram();
	}

	private void startProgram() {			
		Configurator configurator = new Configurator();
		configurator.startupMainMenu();
	}

}
