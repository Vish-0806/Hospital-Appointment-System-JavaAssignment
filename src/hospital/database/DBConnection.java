package hospital.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides a JDBC connection to the local SQLite database used by the system.
 */
public final class DBConnection {

	private static final String DATABASE_URL = "jdbc:sqlite:hospital.db";

	private DBConnection() {
		// Utility class; prevent instantiation.
	}

	/**
	 * Opens a connection to the SQLite database.
	 *
	 * @return an active Connection, or null if the connection could not be created
	 */
	public static Connection getConnection() {
		try {
			Connection connection = DriverManager.getConnection(DATABASE_URL);
			System.out.println("Database Connected Successfully");
			return connection;
		} catch (SQLException exception) {
			System.out.println("Failed to connect to the database: " + exception.getMessage());
			return null;
		}
	}
}
