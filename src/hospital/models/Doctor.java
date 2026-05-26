package hospital.models;

/**
 * Represents a doctor in the Hospital Appointment Management System.
 */
public class Doctor {

	private int doctorId;
	private String doctorName;
	private String specialization;
	private String availability;

	/**
	 * Creates an empty doctor instance.
	 */
	public Doctor() {
	}

	/**
	 * Creates a doctor instance with all fields initialized.
	 *
	 * @param doctorId the doctor identifier
	 * @param doctorName the doctor's name
	 * @param specialization the doctor's specialization
	 * @param availability the doctor's availability
	 */
	public Doctor(int doctorId, String doctorName, String specialization, String availability) {
		this.doctorId = doctorId;
		this.doctorName = doctorName;
		this.specialization = specialization;
		this.availability = availability;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	@Override
	public String toString() {
		return "Doctor{" +
				"doctorId=" + doctorId +
				", doctorName='" + doctorName + '\'' +
				", specialization='" + specialization + '\'' +
				", availability='" + availability + '\'' +
				'}';
	}
}
