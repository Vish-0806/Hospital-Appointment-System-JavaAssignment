package hospital.main;

import hospital.gui.LoginFrame;

import javax.swing.SwingUtilities;

/**
 * Application entry point for the Hospital Appointment Management System.
 */
public class Main {

	/**
	 * Starts the application by opening the login screen.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
	}
}
