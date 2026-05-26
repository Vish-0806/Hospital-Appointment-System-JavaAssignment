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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

/**
 * Patient management screen for view, search, update, and delete operations.
 */
public class PatientManagementFrame extends JFrame {

	private final PatientService patientService;
	private final DefaultTableModel tableModel;
	private final JTable patientTable;
	private final JTextField patientIdField;
	private final JTextField nameField;
	private final JTextField ageField;
	private final JTextField phoneField;
	private final JTextField emailField;
	private final JTextField passwordField;
	private final JTextField searchField;

	/**
	 * Creates and configures the patient management window.
	 */
	public PatientManagementFrame() {
		patientService = new PatientService();
		tableModel = new DefaultTableModel(
				new Object[]{"Patient ID", "Name", "Age", "Phone", "Email", "Password"}, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		patientTable = new JTable(tableModel);
		patientIdField = new JTextField();
		nameField = new JTextField();
		ageField = new JTextField();
		phoneField = new JTextField();
		emailField = new JTextField();
		passwordField = new JTextField();
		searchField = new JTextField();

		setTitle("Hospital Appointment Management System - Manage Patients");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1180, 720);
		setLocationRelativeTo(null);
		setResizable(true);
		JScrollPane scrollPane = new JScrollPane(buildContentPane());
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setBackground(new Color(15, 23, 42));
		setContentPane(scrollPane);
		loadPatients();
	}

	private JPanel buildContentPane() {
		JPanel rootPanel = new JPanel(new BorderLayout(18, 18));
		rootPanel.setBackground(new Color(15, 23, 42));
		rootPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);

		JLabel titleLabel = new JLabel("Patient Management");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));

		JLabel subtitleLabel = new JLabel("Search, update, and delete patient records", SwingConstants.LEFT);
		subtitleLabel.setForeground(new Color(148, 163, 184));
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		headerPanel.add(titleLabel, BorderLayout.NORTH);
		headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
		rootPanel.add(headerPanel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new GridLayout(1, 2, 18, 18));
		centerPanel.setOpaque(false);
		centerPanel.add(buildFormPanel());
		centerPanel.add(buildTablePanel());
		JScrollPane scrollPane = new JScrollPane(centerPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setBackground(new Color(15, 23, 42));
		rootPanel.add(scrollPane, BorderLayout.CENTER);
		return rootPanel;
	}

	private JPanel buildFormPanel() {
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(new Color(30, 41, 59));
		formPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(51, 65, 85), 1, true),
				BorderFactory.createEmptyBorder(18, 18, 18, 18)));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(8, 8, 8, 8);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridwidth = 2;

		patientIdField.setEditable(false);
		styleField(patientIdField);
		styleField(nameField);
		styleField(ageField);
		styleField(phoneField);
		styleField(emailField);
		styleField(passwordField);
		styleField(searchField);

		addField(formPanel, constraints, 0, "Patient ID", patientIdField);
		addField(formPanel, constraints, 2, "Name", nameField);
		addField(formPanel, constraints, 4, "Age", ageField);
		addField(formPanel, constraints, 6, "Phone", phoneField);
		addField(formPanel, constraints, 8, "Email", emailField);
		addField(formPanel, constraints, 10, "Password", passwordField);

		constraints.gridy = 12;
		formPanel.add(createSectionLabel("Search"), constraints);

		constraints.gridy = 13;
		formPanel.add(searchField, constraints);

		JPanel searchButtons = new JPanel(new GridLayout(1, 2, 8, 8));
		searchButtons.setOpaque(false);
		searchButtons.add(createButton("Search", new Color(14, 165, 233), event -> searchPatients()));
		searchButtons.add(createButton("Show All", new Color(71, 85, 105), event -> loadPatients()));
		constraints.gridy = 14;
		formPanel.add(searchButtons, constraints);

		JPanel actionButtons = new JPanel(new GridLayout(2, 2, 8, 8));
		actionButtons.setOpaque(false);
		actionButtons.add(createButton("Update Patient", new Color(34, 197, 94), event -> updatePatient()));
		actionButtons.add(createButton("Delete Patient", new Color(239, 68, 68), event -> deletePatient()));
		actionButtons.add(createButton("Clear", new Color(71, 85, 105), event -> clearFields()));
		actionButtons.add(createButton("Refresh", new Color(14, 165, 233), event -> loadPatients()));

		constraints.gridy = 15;
		formPanel.add(actionButtons, constraints);

		JButton backButton = createButton("Back to Dashboard", new Color(100, 116, 139), event -> {
			dispose();
			new DashboardFrame().setVisible(true);
		});
		constraints.gridy = 16;
		formPanel.add(backButton, constraints);

		return formPanel;
	}

	private JPanel buildTablePanel() {
		JPanel tablePanel = new JPanel(new BorderLayout(0, 12));
		tablePanel.setBackground(new Color(30, 41, 59));
		tablePanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(51, 65, 85), 1, true),
				BorderFactory.createEmptyBorder(18, 18, 18, 18)));

		JLabel tableTitle = new JLabel("Patient Table");
		tableTitle.setForeground(Color.WHITE);
		tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		tablePanel.add(tableTitle, BorderLayout.NORTH);

		patientTable.setRowHeight(28);
		patientTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		patientTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
		patientTable.getTableHeader().setBackground(new Color(14, 165, 233));
		patientTable.getTableHeader().setForeground(Color.WHITE);
		patientTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		patientTable.getSelectionModel().addListSelectionListener(event -> {
			if (event.getValueIsAdjusting()) {
				return;
			}

			int selectedRow = patientTable.getSelectedRow();
			if (selectedRow < 0) {
				return;
			}

			patientIdField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 0)));
			nameField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
			ageField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
			phoneField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
			emailField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
			passwordField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 5)));
		});

		JScrollPane scrollPane = new JScrollPane(patientTable);
		scrollPane.setPreferredSize(new Dimension(560, 580));
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		return tablePanel;
	}

	private void updatePatient() {
		Patient patient = buildPatientFromFields(true);
		if (patient == null) {
			return;
		}

		if (patientService.updatePatient(patient)) {
			JOptionPane.showMessageDialog(this, "Patient updated successfully.", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			loadPatients();
		} else {
			JOptionPane.showMessageDialog(this, "Unable to update patient.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void deletePatient() {
		String patientIdText = patientIdField.getText().trim();
		if (patientIdText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Select a patient from the table.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int response = JOptionPane.showConfirmDialog(this, "Delete selected patient?", "Confirm Delete",
				JOptionPane.YES_NO_OPTION);
		if (response != JOptionPane.YES_OPTION) {
			return;
		}

		int patientId;
		try {
			patientId = Integer.parseInt(patientIdText);
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(this, "Patient ID must be numeric.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (patientService.deletePatient(patientId)) {
			JOptionPane.showMessageDialog(this, "Patient deleted successfully.", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			clearFields();
			loadPatients();
		} else {
			JOptionPane.showMessageDialog(this, "Unable to delete patient.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void searchPatients() {
		String keyword = searchField.getText().trim();
		tableModel.setRowCount(0);

		List<Patient> patients = keyword.isEmpty() ? patientService.getAllPatients() : patientService.searchPatient(keyword);
		for (Patient patient : patients) {
			tableModel.addRow(new Object[]{patient.getPatientId(), patient.getName(), patient.getAge(),
					patient.getPhone(), patient.getEmail(), patient.getPassword()});
		}
	}

	private void loadPatients() {
		tableModel.setRowCount(0);
		for (Patient patient : patientService.getAllPatients()) {
			tableModel.addRow(new Object[]{patient.getPatientId(), patient.getName(), patient.getAge(),
					patient.getPhone(), patient.getEmail(), patient.getPassword()});
		}
	}

	private Patient buildPatientFromFields(boolean requireId) {
		String idText = patientIdField.getText().trim();
		String name = nameField.getText().trim();
		String ageText = ageField.getText().trim();
		String phone = phoneField.getText().trim();
		String email = emailField.getText().trim();
		String password = passwordField.getText().trim();

		if (requireId && idText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Select a patient from the table.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}

		if (name.isEmpty() || ageText.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}

		int age;
		try {
			age = Integer.parseInt(ageText);
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(this, "Age must be numeric.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}

		int patientId = 0;
		if (!idText.isEmpty()) {
			try {
				patientId = Integer.parseInt(idText);
			} catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(this, "Patient ID must be numeric.", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return null;
			}
		}

		if (age < 1 || age > 120) {
			JOptionPane.showMessageDialog(this, "Age must be between 1 and 120.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}

		if (!phone.matches("\\d{10}")) {
			JOptionPane.showMessageDialog(this, "Phone number must contain exactly 10 digits.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}

		if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}

		if (password.length() < 6) {
			JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}

		return new Patient(patientId, name, age, phone, email, password);
	}

	private void clearFields() {
		patientIdField.setText("");
		nameField.setText("");
		ageField.setText("");
		phoneField.setText("");
		emailField.setText("");
		passwordField.setText("");
		searchField.setText("");
		patientTable.clearSelection();
	}

	private void addField(JPanel panel, GridBagConstraints constraints, int row, String labelText, JTextField field) {
		constraints.gridy = row;
		panel.add(createSectionLabel(labelText), constraints);

		constraints.gridy = row + 1;
		panel.add(field, constraints);
	}

	private JLabel createSectionLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(new Color(226, 232, 240));
		label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		return label;
	}

	private void styleField(JTextField field) {
		field.setPreferredSize(new Dimension(320, 36));
		field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(100, 116, 139), 1, true),
				BorderFactory.createEmptyBorder(8, 12, 8, 12)));
		field.setBackground(Color.WHITE);
	}

	private JButton createButton(String text, Color background, java.awt.event.ActionListener listener) {
		JButton button = new JButton(text);
		button.setFocusPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setFont(new Font("Segoe UI", Font.BOLD, 13));
		button.setForeground(Color.WHITE);
		button.setBackground(background);
		button.setPreferredSize(new Dimension(180, 38));
		button.addActionListener(listener);
		return button;
	}

}