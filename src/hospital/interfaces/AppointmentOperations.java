package hospital.interfaces;

/**
 * Defines appointment-related operations for the service layer.
 */
public interface AppointmentOperations {

	void bookAppointment();

	void cancelAppointment(int appointmentId);

	void viewAppointments();
}
