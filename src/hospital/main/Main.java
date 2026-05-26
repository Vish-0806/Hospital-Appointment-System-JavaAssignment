package hospital.main;

import hospital.database.DBConnection;

/**
 * Application entry point for the Hospital Appointment Management System.
 */
public class Main {

	/**
	 * Starts the application and initializes the database connection.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		DBConnection.getConnection();
	}
}
