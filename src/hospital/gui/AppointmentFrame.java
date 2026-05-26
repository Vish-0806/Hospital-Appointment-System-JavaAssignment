package hospital.gui;

import hospital.models.Appointment;
import hospital.models.Doctor;
import hospital.models.Patient;
import hospital.services.AppointmentService;
import hospital.services.DoctorService;
import hospital.services.PatientService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * Appointment booking screen.
 */
public class AppointmentFrame extends JFrame {

	private final JComboBox<PatientItem> patientComboBox;
	private final JComboBox<DoctorItem> doctorComboBox;
	private final JTextField appointmentDateField;
	private final JComboBox<String> statusComboBox;
	private final AppointmentService appointmentService;

	/**
	 * Creates and configures the appointment window.
	 */
	public AppointmentFrame() {
		appointmentService = new AppointmentService();

		setTitle("Hospital Appointment Management System - Book Appointment");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(680, 460);
		setLocationRelativeTo(null);
		setResizable(true);
		setLayout(new BorderLayout());

		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.setBackground(new Color(15, 23, 42));
		add(rootPanel, BorderLayout.CENTER);

		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(new Color(15, 23, 42));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 12, 24));

		JLabel titleLabel = new JLabel("Book Appointment");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

		JLabel subtitleLabel = new JLabel("Select a patient, doctor, date, and status", JLabel.LEFT);
		subtitleLabel.setForeground(new Color(148, 163, 184));
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

		headerPanel.add(titleLabel, BorderLayout.NORTH);
		headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
		rootPanel.add(headerPanel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(new Color(15, 23, 42));
		formPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(10, 40, 24, 40),
				BorderFactory.createLineBorder(new Color(51, 65, 85), 1, true)));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridwidth = 2;

		patientComboBox = new JComboBox<>();
		doctorComboBox = new JComboBox<>();
		appointmentDateField = new JTextField();
		statusComboBox = new JComboBox<>(new String[]{"Scheduled", "Confirmed", "Cancelled"});

		styleInput(patientComboBox);
		styleInput(doctorComboBox);
		styleInput(appointmentDateField);
		styleInput(statusComboBox);

		addRow(formPanel, constraints, 0, "Patient", patientComboBox);
		addRow(formPanel, constraints, 2, "Doctor", doctorComboBox);
		addRow(formPanel, constraints, 4, "Appointment Date (YYYY-MM-DD)", appointmentDateField);
		addRow(formPanel, constraints, 6, "Status", statusComboBox);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(15, 23, 42));

		JButton bookButton = createButton("Book", new Color(14, 165, 233));
		JButton cancelButton = createButton("Cancel", new Color(71, 85, 105));

		bookButton.addActionListener(event -> bookAppointment());
		cancelButton.addActionListener(event -> clearFields());

		buttonPanel.add(bookButton);
		buttonPanel.add(cancelButton);

		constraints.gridy = 8;
		formPanel.add(buttonPanel, constraints);

		JScrollPane scrollPane = new JScrollPane(formPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setBackground(new Color(15, 23, 42));
		rootPanel.add(scrollPane, BorderLayout.CENTER);

		loadPatients();
		loadDoctors();
	}

	private void bookAppointment() {
		PatientItem selectedPatient = (PatientItem) patientComboBox.getSelectedItem();
		DoctorItem selectedDoctor = (DoctorItem) doctorComboBox.getSelectedItem();
		String appointmentDate = appointmentDateField.getText().trim();
		String status = (String) statusComboBox.getSelectedItem();

		if (selectedPatient == null || selectedDoctor == null) {
			JOptionPane.showMessageDialog(this, "Please select both patient and doctor.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (appointmentDate.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Appointment date is required.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		Appointment appointment = new Appointment(0, selectedPatient.getPatientId(), selectedDoctor.getDoctorId(),
				appointmentDate, status == null ? "Scheduled" : status);

		boolean saved = appointmentService.bookAppointment(appointment);

		if (saved) {
			JOptionPane.showMessageDialog(this, "Appointment booked successfully.", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			clearFields();
		} else {
			JOptionPane.showMessageDialog(this, "Unable to book appointment.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void clearFields() {
		patientComboBox.setSelectedIndex(patientComboBox.getItemCount() > 0 ? 0 : -1);
		doctorComboBox.setSelectedIndex(doctorComboBox.getItemCount() > 0 ? 0 : -1);
		appointmentDateField.setText("");
		statusComboBox.setSelectedIndex(0);
	}

	private void loadPatients() {
		patientComboBox.removeAllItems();
		List<Patient> patients = new PatientService().getAllPatients();
		for (Patient patient : patients) {
			patientComboBox.addItem(new PatientItem(patient.getPatientId(), patient.getName()));
		}
	}

	private void loadDoctors() {
		doctorComboBox.removeAllItems();
		List<Doctor> doctors = new DoctorService().getAllDoctors();
		for (Doctor doctor : doctors) {
			doctorComboBox.addItem(new DoctorItem(doctor.getDoctorId(), doctor.getDoctorName()));
		}
	}

	private void addRow(JPanel panel, GridBagConstraints constraints, int row, String labelText, java.awt.Component field) {
		JLabel label = new JLabel(labelText);
		label.setForeground(new Color(226, 232, 240));
		label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		constraints.gridy = row;
		panel.add(label, constraints);

		constraints.gridy = row + 1;
		panel.add(field, constraints);
	}

	private void styleInput(JComboBox<?> comboBox) {
		comboBox.setPreferredSize(new Dimension(360, 36));
		comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	}

	private void styleInput(JTextField field) {
		field.setPreferredSize(new Dimension(360, 36));
		field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(100, 116, 139), 1, true),
				BorderFactory.createEmptyBorder(8, 12, 8, 12)));
		field.setBackground(Color.WHITE);
	}

	private JButton createButton(String text, Color background) {
		JButton button = new JButton(text);
		button.setFocusPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setFont(new Font("Segoe UI", Font.BOLD, 13));
		button.setForeground(Color.WHITE);
		button.setBackground(background);
		button.setPreferredSize(new Dimension(130, 36));
		button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
		return button;
	}


	private static final class PatientItem {
		private final int patientId;
		private final String displayName;

		private PatientItem(int patientId, String displayName) {
			this.patientId = patientId;
			this.displayName = displayName;
		}

		private int getPatientId() {
			return patientId;
		}

		@Override
		public String toString() {
			return displayName + " (ID: " + patientId + ")";
		}
	}

	private static final class DoctorItem {
		private final int doctorId;
		private final String displayName;

		private DoctorItem(int doctorId, String displayName) {
			this.doctorId = doctorId;
			this.displayName = displayName;
		}

		private int getDoctorId() {
			return doctorId;
		}

		@Override
		public String toString() {
			return displayName + " (ID: " + doctorId + ")";
		}
	}
}
