package hospital.services;

import hospital.database.DBConnection;
import hospital.models.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides patient-related database operations.
 */
public class PatientService {

	private static final String INSERT_SQL =
			"INSERT INTO patients (name, age, phone, email, password) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_SQL =
			"UPDATE patients SET name = ?, age = ?, phone = ?, email = ?, password = ? WHERE patient_id = ?";
	private static final String DELETE_SQL = "DELETE FROM patients WHERE patient_id = ?";
	private static final String SELECT_BY_ID_SQL =
			"SELECT patient_id, name, age, phone, email, password FROM patients WHERE patient_id = ?";
	private static final String SELECT_ALL_SQL =
			"SELECT patient_id, name, age, phone, email, password FROM patients ORDER BY patient_id";
	private static final String SEARCH_SQL =
			"SELECT patient_id, name, age, phone, email, password FROM patients "
					+ "WHERE name LIKE ? OR phone LIKE ? OR email LIKE ? ORDER BY patient_id";

	/**
	 * Registers a new patient in the database.
	 *
	 * @param patient the patient to store
	 * @return true if the record was inserted successfully; otherwise false
	 */
	public boolean registerPatient(Patient patient) {
		validatePatientForSave(patient);

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return false;
			}

			try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
				statement.setString(1, patient.getName().trim());
				statement.setInt(2, patient.getAge());
				statement.setString(3, patient.getPhone().trim());
				statement.setString(4, patient.getEmail().trim());
				statement.setString(5, patient.getPassword());

				return statement.executeUpdate() > 0;
			}
		} catch (SQLException exception) {
			System.out.println("Failed to register patient: " + exception.getMessage());
			return false;
		}
	}

	/**
	 * Updates an existing patient.
	 *
	 * @param patient the patient with updated values
	 * @return true if the record was updated successfully; otherwise false
	 */
	public boolean updatePatient(Patient patient) {
		validatePatientForSave(patient);

		if (patient.getPatientId() <= 0) {
			throw new IllegalArgumentException("Patient ID must be greater than zero.");
		}

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return false;
			}

			try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
				statement.setString(1, patient.getName().trim());
				statement.setInt(2, patient.getAge());
				statement.setString(3, patient.getPhone().trim());
				statement.setString(4, patient.getEmail().trim());
				statement.setString(5, patient.getPassword());
				statement.setInt(6, patient.getPatientId());

				return statement.executeUpdate() > 0;
			}
		} catch (SQLException exception) {
			System.out.println("Failed to update patient: " + exception.getMessage());
			return false;
		}
	}

	/**
	 * Deletes a patient by ID.
	 *
	 * @param patientId the patient identifier
	 * @return true if the record was deleted successfully; otherwise false
	 */
	public boolean deletePatient(int patientId) {
		if (patientId <= 0) {
			throw new IllegalArgumentException("Patient ID must be greater than zero.");
		}

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return false;
			}

			try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
				statement.setInt(1, patientId);
				return statement.executeUpdate() > 0;
			}
		} catch (SQLException exception) {
			System.out.println("Failed to delete patient: " + exception.getMessage());
			return false;
		}
	}

	/**
	 * Retrieves a patient by ID.
	 *
	 * @param patientId the patient identifier
	 * @return the matching patient, or null if not found
	 */
	public Patient getPatientById(int patientId) {
		if (patientId <= 0) {
			throw new IllegalArgumentException("Patient ID must be greater than zero.");
		}

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return null;
			}

			try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
				statement.setInt(1, patientId);

				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						return mapPatient(resultSet);
					}
				}
			}
		} catch (SQLException exception) {
			System.out.println("Failed to fetch patient: " + exception.getMessage());
		}

		return null;
	}

	/**
	 * Retrieves all patients in the database.
	 *
	 * @return a list of patients
	 */
	public List<Patient> getAllPatients() {
		List<Patient> patients = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return patients;
			}

			try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
				 ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {
					patients.add(mapPatient(resultSet));
				}
			}
		} catch (SQLException exception) {
			System.out.println("Failed to fetch patients: " + exception.getMessage());
		}

		return patients;
	}

	/**
	 * Searches patients by name, phone, or email.
	 *
	 * @param keyword the search text
	 * @return a list of matching patients
	 */
	public List<Patient> searchPatient(String keyword) {
		List<Patient> patients = new ArrayList<>();

		if (keyword == null || keyword.trim().isEmpty()) {
			return patients;
		}

		String searchPattern = "%" + keyword.trim() + "%";

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return patients;
			}

			try (PreparedStatement statement = connection.prepareStatement(SEARCH_SQL)) {
				statement.setString(1, searchPattern);
				statement.setString(2, searchPattern);
				statement.setString(3, searchPattern);

				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						patients.add(mapPatient(resultSet));
					}
				}
			}
		} catch (SQLException exception) {
			System.out.println("Failed to search patients: " + exception.getMessage());
		}

		return patients;
	}

	private void validatePatientForSave(Patient patient) {
		if (patient == null) {
			throw new IllegalArgumentException("Patient cannot be null.");
		}

		if (patient.getName() == null || patient.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("Patient name is required.");
		}

		if (patient.getAge() < 1 || patient.getAge() > 120) {
			throw new IllegalArgumentException("Patient age must be between 1 and 120.");
		}

		if (!isValidPhone(patient.getPhone())) {
			throw new IllegalArgumentException("Phone number must contain exactly 10 digits.");
		}

		if (!isValidEmail(patient.getEmail())) {
			throw new IllegalArgumentException("Email format is invalid.");
		}

		if (patient.getPassword() == null || patient.getPassword().length() < 6) {
			throw new IllegalArgumentException("Password must be at least 6 characters long.");
		}
	}

	private boolean isValidPhone(String phone) {
		return phone != null && phone.trim().matches("\\d{10}");
	}

	private boolean isValidEmail(String email) {
		return email != null && email.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
	}

	private Patient mapPatient(ResultSet resultSet) throws SQLException {
		return new Patient(
				resultSet.getInt("patient_id"),
				resultSet.getString("name"),
				resultSet.getInt("age"),
				resultSet.getString("phone"),
				resultSet.getString("email"),
				resultSet.getString("password")
		);
	}
}
