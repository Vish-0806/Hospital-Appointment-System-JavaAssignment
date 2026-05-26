package hospital.gui;

import hospital.models.Patient;
import hospital.services.PatientService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Patient registration screen.
 */
public class RegisterPatientFrame extends JFrame {

	private final JTextField nameField;
	private final JTextField ageField;
	private final JTextField phoneField;
	private final JTextField emailField;
	private final JPasswordField passwordField;
	private final PatientService patientService;

	/**
	 * Creates and configures the registration window.
	 */
	public RegisterPatientFrame() {
		patientService = new PatientService();

		setTitle("Hospital Appointment Management System - Register Patient");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(700, 700);
		setLocationRelativeTo(null);
		setResizable(true);
		setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(new Color(15, 23, 42));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
		setContentPane(mainPanel);

		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(new Color(15, 23, 42));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

		JLabel titleLabel = new JLabel("Register Patient");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

		JLabel subtitleLabel = new JLabel("Create a new patient profile", JLabel.LEFT);
		subtitleLabel.setForeground(new Color(148, 163, 184));
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

		headerPanel.add(titleLabel, BorderLayout.NORTH);
		headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
		mainPanel.add(headerPanel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(new Color(15, 23, 42));
		formPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(18, 36, 36, 36),
				BorderFactory.createLineBorder(new Color(51, 65, 85), 1, true)));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridwidth = 2;

		nameField = new JTextField();
		ageField = new JTextField();
		phoneField = new JTextField();
		emailField = new JTextField();
		passwordField = new JPasswordField();

		styleInput(nameField);
		styleInput(ageField);
		styleInput(phoneField);
		styleInput(emailField);
		styleInput(passwordField);

		addRow(formPanel, constraints, 0, "Name", nameField);
		addRow(formPanel, constraints, 2, "Age", ageField);
		addRow(formPanel, constraints, 4, "Phone", phoneField);
		addRow(formPanel, constraints, 6, "Email", emailField);
		addRow(formPanel, constraints, 8, "Password", passwordField);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(15, 23, 42));

		JButton registerButton = createButton("Register", new Color(14, 165, 233));
		JButton resetButton = createButton("Reset", new Color(71, 85, 105));

		registerButton.addActionListener(event -> registerPatient());
		resetButton.addActionListener(event -> clearFields());

		buttonPanel.add(registerButton);
		buttonPanel.add(resetButton);

		constraints.gridy = 10;
		formPanel.add(buttonPanel, constraints);

		JScrollPane scrollPane = new JScrollPane(formPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getViewport().setBackground(new Color(15, 23, 42));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
	}

	private void registerPatient() {
		String name = nameField.getText().trim();
		String ageText = ageField.getText().trim();
		String phone = phoneField.getText().trim();
		String email = emailField.getText().trim();
		String password = new String(passwordField.getPassword());

		if (name.isEmpty() || ageText.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int age;
		try {
			age = Integer.parseInt(ageText);
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(this, "Age must be a valid number.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (age < 1 || age > 120) {
			JOptionPane.showMessageDialog(this, "Age must be between 1 and 120.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (!phone.matches("\\d{10}")) {
			JOptionPane.showMessageDialog(this, "Phone number must contain exactly 10 digits.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (password.length() < 6) {
			JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		Patient patient = new Patient(0, name, age, phone, email, password);
		boolean saved = patientService.registerPatient(patient);

		if (saved) {
			JOptionPane.showMessageDialog(this, "Patient registered successfully.", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			clearFields();
		} else {
			JOptionPane.showMessageDialog(this, "Unable to register patient.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void clearFields() {
		nameField.setText("");
		ageField.setText("");
		phoneField.setText("");
		emailField.setText("");
		passwordField.setText("");
	}

	private void addRow(JPanel panel, GridBagConstraints constraints, int row, String labelText, JTextField field) {
		JLabel label = new JLabel(labelText);
		label.setForeground(new Color(226, 232, 240));
		label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		constraints.gridy = row;
		panel.add(label, constraints);

		constraints.gridy = row + 1;
		panel.add(field, constraints);
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

}

