package hospital.services;

import hospital.database.DBConnection;
import hospital.interfaces.AppointmentOperations;
import hospital.models.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides appointment-related database operations.
 */
public class AppointmentService implements AppointmentOperations {

	private static final String INSERT_SQL =
			"INSERT INTO appointments (patient_id, doctor_id, appointment_date, status) VALUES (?, ?, ?, ?)";
	private static final String UPDATE_STATUS_SQL =
			"UPDATE appointments SET status = ? WHERE appointment_id = ?";
	private static final String CANCEL_SQL =
			"UPDATE appointments SET status = 'Cancelled' WHERE appointment_id = ?";
	private static final String SELECT_ALL_SQL =
			"SELECT appointment_id, patient_id, doctor_id, appointment_date, status FROM appointments ORDER BY appointment_id";
	private static final String SELECT_BY_PATIENT_SQL =
			"SELECT appointment_id, patient_id, doctor_id, appointment_date, status FROM appointments "
					+ "WHERE patient_id = ? ORDER BY appointment_id";
	private static final String SELECT_WITH_JOIN_SQL =
			"SELECT a.appointment_id, p.name AS patient_name, d.doctor_name, a.appointment_date, a.status "
					+ "FROM appointments a "
					+ "JOIN patients p ON a.patient_id = p.patient_id "
					+ "JOIN doctors d ON a.doctor_id = d.doctor_id "
					+ "ORDER BY a.appointment_id";

	/**
	 * Books a new appointment.
	 *
	 * @return true if the appointment was saved; otherwise false
	 */
	@Override
	public void bookAppointment() {
		System.out.println("Use bookAppointment(Appointment) to book an appointment programmatically.");
	}

	/**
	 * Books a new appointment.
	 *
	 * @param appointment the appointment to save
	 * @return true if the appointment was saved; otherwise false
	 */
	public boolean bookAppointment(Appointment appointment) {
		validateAppointment(appointment);

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return false;
			}

			try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
				statement.setInt(1, appointment.getPatientId());
				statement.setInt(2, appointment.getDoctorId());
				statement.setString(3, appointment.getAppointmentDate().trim());
				statement.setString(4, appointment.getStatus().trim());
				return statement.executeUpdate() > 0;
			}
		} catch (SQLException exception) {
			System.out.println("Failed to book appointment: " + exception.getMessage());
			return false;
		}
	}

	/**
	 * Updates the status of an existing appointment.
	 *
	 * @param appointmentId the appointment identifier
	 * @param status the new status
	 * @return true if the record was updated; otherwise false
	 */
	public boolean updateAppointmentStatus(int appointmentId, String status) {
		if (appointmentId <= 0) {
			throw new IllegalArgumentException("Appointment ID must be greater than zero.");
		}

		if (status == null || status.trim().isEmpty()) {
			throw new IllegalArgumentException("Status is required.");
		}

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return false;
			}

			try (PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS_SQL)) {
				statement.setString(1, status.trim());
				statement.setInt(2, appointmentId);
				return statement.executeUpdate() > 0;
			}
		} catch (SQLException exception) {
			System.out.println("Failed to update appointment status: " + exception.getMessage());
			return false;
		}
	}

	/**
	 * Cancels an appointment by marking it as cancelled.
	 *
	 * @param appointmentId the appointment identifier
	 */
	@Override
	public void cancelAppointment(int appointmentId) {
		if (appointmentId <= 0) {
			throw new IllegalArgumentException("Appointment ID must be greater than zero.");
		}

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return;
			}

			try (PreparedStatement statement = connection.prepareStatement(CANCEL_SQL)) {
				statement.setInt(1, appointmentId);
				statement.executeUpdate();
			}
		} catch (SQLException exception) {
			System.out.println("Failed to cancel appointment: " + exception.getMessage());
		}
	}

	/**
	 * Views all appointments.
	 */
	@Override
	public void viewAppointments() {
		List<AppointmentViewRow> rows = getAppointmentRowsWithJoin();
		for (AppointmentViewRow row : rows) {
			System.out.println(row);
		}
	}

	/**
	 * Retrieves all appointments as model objects.
	 *
	 * @return a list of appointments
	 */
	public List<Appointment> getAllAppointments() {
		List<Appointment> appointments = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return appointments;
			}

			try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
				 ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {
					appointments.add(mapAppointment(resultSet));
				}
			}
		} catch (SQLException exception) {
			System.out.println("Failed to fetch appointments: " + exception.getMessage());
		}

		return appointments;
	}

	/**
	 * Retrieves appointments for a specific patient.
	 *
	 * @param patientId the patient identifier
	 * @return a list of matching appointments
	 */
	public List<Appointment> getAppointmentsByPatient(int patientId) {
		if (patientId <= 0) {
			throw new IllegalArgumentException("Patient ID must be greater than zero.");
		}

		List<Appointment> appointments = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return appointments;
			}

			try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_PATIENT_SQL)) {
				statement.setInt(1, patientId);

				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						appointments.add(mapAppointment(resultSet));
					}
				}
			}
		} catch (SQLException exception) {
			System.out.println("Failed to fetch patient appointments: " + exception.getMessage());
		}

		return appointments;
	}

	/**
	 * Returns appointment data joined with patient and doctor names.
	 *
	 * @return the join query rows
	 */
	public List<AppointmentViewRow> getAppointmentRowsWithJoin() {
		List<AppointmentViewRow> rows = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection()) {
			if (connection == null) {
				return rows;
			}

			try (PreparedStatement statement = connection.prepareStatement(SELECT_WITH_JOIN_SQL);
				 ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {
					rows.add(new AppointmentViewRow(
							resultSet.getInt("appointment_id"),
							resultSet.getString("patient_name"),
							resultSet.getString("doctor_name"),
							resultSet.getString("appointment_date"),
							resultSet.getString("status")
					));
				}
			}
		} catch (SQLException exception) {
			System.out.println("Failed to fetch joined appointments: " + exception.getMessage());
		}

		return rows;
	}

	private void validateAppointment(Appointment appointment) {
		if (appointment == null) {
			throw new IllegalArgumentException("Appointment cannot be null.");
		}

		if (appointment.getPatientId() <= 0) {
			throw new IllegalArgumentException("Patient ID must be greater than zero.");
		}

		if (appointment.getDoctorId() <= 0) {
			throw new IllegalArgumentException("Doctor ID must be greater than zero.");
		}

		if (appointment.getAppointmentDate() == null || appointment.getAppointmentDate().trim().isEmpty()) {
			throw new IllegalArgumentException("Appointment date is required.");
		}

		if (appointment.getStatus() == null || appointment.getStatus().trim().isEmpty()) {
			throw new IllegalArgumentException("Status is required.");
		}
	}

	private Appointment mapAppointment(ResultSet resultSet) throws SQLException {
		return new Appointment(
				resultSet.getInt("appointment_id"),
				resultSet.getInt("patient_id"),
				resultSet.getInt("doctor_id"),
				resultSet.getString("appointment_date"),
				resultSet.getString("status")
		);
	}

	/**
	 * Simple projection for displaying joined appointment data.
	 */
	public static final class AppointmentViewRow {
		private final int appointmentId;
		private final String patientName;
		private final String doctorName;
		private final String appointmentDate;
		private final String status;

		public AppointmentViewRow(int appointmentId, String patientName, String doctorName,
								  String appointmentDate, String status) {
			this.appointmentId = appointmentId;
			this.patientName = patientName;
			this.doctorName = doctorName;
			this.appointmentDate = appointmentDate;
			this.status = status;
		}

		public int getAppointmentId() {
			return appointmentId;
		}

		public String getPatientName() {
			return patientName;
		}

		public String getDoctorName() {
			return doctorName;
		}

		public String getAppointmentDate() {
			return appointmentDate;
		}

		public String getStatus() {
			return status;
		}

		@Override
		public String toString() {
			return "AppointmentViewRow{" +
					"appointmentId=" + appointmentId +
					", patientName='" + patientName + '\'' +
					", doctorName='" + doctorName + '\'' +
					", appointmentDate='" + appointmentDate + '\'' +
					", status='" + status + '\'' +
					'}';
		}
	}
}
