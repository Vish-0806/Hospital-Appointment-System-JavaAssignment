package hospital.models;

/**
 * Represents a patient in the Hospital Appointment Management System.
 */
public class Patient {

	private int patientId;
	private String name;
	private int age;
	private String phone;
	private String email;
	private String password;

	/**
	 * Creates an empty patient instance.
	 */
	public Patient() {
	}

	/**
	 * Creates a patient instance with all fields initialized.
	 *
	 * @param patientId the patient identifier
	 * @param name the patient name
	 * @param age the patient age
	 * @param phone the patient phone number
	 * @param email the patient email address
	 * @param password the patient password
	 */
	public Patient(int patientId, String name, int age, String phone, String email, String password) {
		this.patientId = patientId;
		this.name = name;
		this.age = age;
		this.phone = phone;
		this.email = email;
		this.password = password;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Patient{" +
				"patientId=" + patientId +
				", name='" + name + '\'' +
				", age=" + age +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
