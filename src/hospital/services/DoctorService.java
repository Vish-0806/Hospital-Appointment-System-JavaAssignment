package hospital.services;

import hospital.database.DBConnection;
import hospital.models.Doctor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides doctor-related database operations.
 */
public class DoctorService {

	private static final String INSERT_SQL =
			"INSERT INTO doctors (doctor_name, specialization, availability) VALUES (?, ?, ?)";
	private static final String UPDATE_SQL =
			"UPDATE doctors SET doctor_name = ?, specialization = ?, availability = ? WHERE doctor_id = ?";
	private static final String DELETE_SQL = "DELETE FROM doctors WHERE doctor_id = ?";
	private static final String SELECT_BY_ID_SQL =
			"SELECT doctor_id, doctor_name, specialization, availability FROM doctors WHERE doctor_id = ?";
	private static final String SELECT_ALL_SQL =
			"SELECT doctor_id, doctor_name, specialization, availability FROM doctors ORDER BY doctor_id";
	private static final String SEARCH_SQL =
			"SELECT doctor_id, doctor_name, specialization, availability FROM doctors "
					+ "WHERE doctor_name LIKE ? OR specialization LIKE ? OR availability LIKE ? ORDER BY doctor_id";

	/**
	 * Adds a new doctor to the database.
	 *
	 * @param doctor the doctor to store
	 * @return true if the record was inserted successfully; otherwise false
	 */
	public boolean addDoctor(Doctor doctor) {
		validateDoctor(doctor);

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return false;
			}

			try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
				statement.setString(1, doctor.getDoctorName().trim());
				statement.setString(2, doctor.getSpecialization().trim());
				statement.setString(3, doctor.getAvailability().trim());
				return statement.executeUpdate() > 0;
			}
		} catch (SQLException exception) {
			System.out.println("Failed to add doctor: " + exception.getMessage());
			return false;
		}
	}

	/**
	 * Updates an existing doctor.
	 *
	 * @param doctor the doctor with updated values
	 * @return true if the record was updated successfully; otherwise false
	 */
	public boolean updateDoctor(Doctor doctor) {
		validateDoctor(doctor);

		if (doctor.getDoctorId() <= 0) {
			throw new IllegalArgumentException("Doctor ID must be greater than zero.");
		}

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return false;
			}

			try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
				statement.setString(1, doctor.getDoctorName().trim());
				statement.setString(2, doctor.getSpecialization().trim());
				statement.setString(3, doctor.getAvailability().trim());
				statement.setInt(4, doctor.getDoctorId());
				return statement.executeUpdate() > 0;
			}
		} catch (SQLException exception) {
			System.out.println("Failed to update doctor: " + exception.getMessage());
			return false;
		}
	}

	/**
	 * Deletes a doctor by ID.
	 *
	 * @param doctorId the doctor identifier
	 * @return true if the record was deleted successfully; otherwise false
	 */
	public boolean deleteDoctor(int doctorId) {
		if (doctorId <= 0) {
			throw new IllegalArgumentException("Doctor ID must be greater than zero.");
		}

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return false;
			}

			try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
				statement.setInt(1, doctorId);
				return statement.executeUpdate() > 0;
			}
		} catch (SQLException exception) {
			System.out.println("Failed to delete doctor: " + exception.getMessage());
			return false;
		}
	}

	/**
	 * Retrieves a doctor by ID.
	 *
	 * @param doctorId the doctor identifier
	 * @return the matching doctor, or null if not found
	 */
	public Doctor getDoctorById(int doctorId) {
		if (doctorId <= 0) {
			throw new IllegalArgumentException("Doctor ID must be greater than zero.");
		}

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return null;
			}

			try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
				statement.setInt(1, doctorId);

				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						return mapDoctor(resultSet);
					}
				}
			}
		} catch (SQLException exception) {
			System.out.println("Failed to fetch doctor: " + exception.getMessage());
		}

		return null;
	}

	/**
	 * Retrieves all doctors in the database.
	 *
	 * @return a list of doctors
	 */
	public List<Doctor> getAllDoctors() {
		List<Doctor> doctors = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return doctors;
			}

			try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
				 ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {
					doctors.add(mapDoctor(resultSet));
				}
			}
		} catch (SQLException exception) {
			System.out.println("Failed to fetch doctors: " + exception.getMessage());
		}

		return doctors;
	}

	/**
	 * Searches doctors by name, specialization, or availability.
	 *
	 * @param keyword the search text
	 * @return a list of matching doctors
	 */
	public List<Doctor> searchDoctor(String keyword) {
		List<Doctor> doctors = new ArrayList<>();

		if (keyword == null || keyword.trim().isEmpty()) {
			return doctors;
		}

		String searchPattern = "%" + keyword.trim() + "%";

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return doctors;
			}

			try (PreparedStatement statement = connection.prepareStatement(SEARCH_SQL)) {
				statement.setString(1, searchPattern);
				statement.setString(2, searchPattern);
				statement.setString(3, searchPattern);

				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						doctors.add(mapDoctor(resultSet));
					}
				}
			}
		} catch (SQLException exception) {
			System.out.println("Failed to search doctors: " + exception.getMessage());
		}

		return doctors;
	}

	private void validateDoctor(Doctor doctor) {
		if (doctor == null) {
			throw new IllegalArgumentException("Doctor cannot be null.");
		}

		if (doctor.getDoctorName() == null || doctor.getDoctorName().trim().isEmpty()) {
			throw new IllegalArgumentException("Doctor name is required.");
		}

		if (doctor.getSpecialization() == null || doctor.getSpecialization().trim().isEmpty()) {
			throw new IllegalArgumentException("Doctor specialization is required.");
		}

		if (doctor.getAvailability() == null || doctor.getAvailability().trim().isEmpty()) {
			throw new IllegalArgumentException("Doctor availability is required.");
		}
	}

	private Doctor mapDoctor(ResultSet resultSet) throws SQLException {
		return new Doctor(
				resultSet.getInt("doctor_id"),
				resultSet.getString("doctor_name"),
				resultSet.getString("specialization"),
				resultSet.getString("availability")
		);
	}
}
