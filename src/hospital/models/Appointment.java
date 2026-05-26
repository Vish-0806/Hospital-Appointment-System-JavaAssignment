package hospital.models;

/**
 * Represents an appointment in the Hospital Appointment Management System.
 */
public class Appointment {

	private int appointmentId;
	private int patientId;
	private int doctorId;
	private String appointmentDate;
	private String status;

	/**
	 * Creates an empty appointment instance.
	 */
	public Appointment() {
	}

	/**
	 * Creates an appointment instance with all fields initialized.
	 *
	 * @param appointmentId the appointment identifier
	 * @param patientId the patient identifier
	 * @param doctorId the doctor identifier
	 * @param appointmentDate the appointment date
	 * @param status the appointment status
	 */
	public Appointment(int appointmentId, int patientId, int doctorId, String appointmentDate, String status) {
		this.appointmentId = appointmentId;
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.appointmentDate = appointmentDate;
		this.status = status;
	}

	public int getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Appointment{" +
				"appointmentId=" + appointmentId +
				", patientId=" + patientId +
				", doctorId=" + doctorId +
				", appointmentDate='" + appointmentDate + '\'' +
				", status='" + status + '\'' +
				'}';
	}
}
