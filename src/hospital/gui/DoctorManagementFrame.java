package hospital.gui;

import hospital.models.Doctor;
import hospital.services.DoctorService;

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
import javax.swing.event.ListSelectionEvent;
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
 * Doctor management screen for add, update, delete, and search operations.
 */
public class DoctorManagementFrame extends JFrame {

	private final DoctorService doctorService;
	private final DefaultTableModel tableModel;
	private final JTable doctorTable;
	private final JTextField doctorIdField;
	private final JTextField doctorNameField;
	private final JTextField specializationField;
	private final JTextField availabilityField;
	private final JTextField searchField;

	/**
	 * Creates and configures the doctor management window.
	 */
	public DoctorManagementFrame() {
		doctorService = new DoctorService();
		tableModel = new DefaultTableModel(
				new Object[]{"Doctor ID", "Doctor Name", "Specialization", "Availability"}, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		doctorTable = new JTable(tableModel);
		doctorIdField = new JTextField();
		doctorNameField = new JTextField();
		specializationField = new JTextField();
		availabilityField = new JTextField();
		searchField = new JTextField();

		setTitle("Hospital Appointment Management System - Manage Doctors");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1100, 700);
		setLocationRelativeTo(null);
		setResizable(true);
		JScrollPane scrollPane = new JScrollPane(buildContentPane());
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setBackground(new Color(15, 23, 42));
		setContentPane(scrollPane);
		loadDoctors();
	}

	private JPanel buildContentPane() {
		JPanel rootPanel = new JPanel(new BorderLayout(18, 18));
		rootPanel.setBackground(new Color(15, 23, 42));
		rootPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);

		JLabel titleLabel = new JLabel("Doctor Management");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));

		JLabel subtitleLabel = new JLabel("Add, update, delete, and search doctors", SwingConstants.LEFT);
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

		doctorIdField.setEditable(false);
		styleField(doctorIdField);
		styleField(doctorNameField);
		styleField(specializationField);
		styleField(availabilityField);
		styleField(searchField);

		addField(formPanel, constraints, 0, "Doctor ID", doctorIdField);
		addField(formPanel, constraints, 2, "Doctor Name", doctorNameField);
		addField(formPanel, constraints, 4, "Specialization", specializationField);
		addField(formPanel, constraints, 6, "Availability", availabilityField);

		constraints.gridy = 8;
		formPanel.add(createSectionLabel("Search"), constraints);

		constraints.gridy = 9;
		formPanel.add(searchField, constraints);

		JPanel searchButtons = new JPanel(new GridLayout(1, 2, 8, 8));
		searchButtons.setOpaque(false);
		searchButtons.add(createButton("Search", new Color(14, 165, 233), event -> searchDoctors()));
		searchButtons.add(createButton("Show All", new Color(71, 85, 105), event -> loadDoctors()));
		constraints.gridy = 10;
		formPanel.add(searchButtons, constraints);

		JPanel actionButtons = new JPanel(new GridLayout(2, 2, 8, 8));
		actionButtons.setOpaque(false);
		actionButtons.add(createButton("Add Doctor", new Color(14, 165, 233), event -> addDoctor()));
		actionButtons.add(createButton("Update Doctor", new Color(34, 197, 94), event -> updateDoctor()));
		actionButtons.add(createButton("Delete Doctor", new Color(239, 68, 68), event -> deleteDoctor()));
		actionButtons.add(createButton("Clear", new Color(71, 85, 105), event -> clearFields()));

		constraints.gridy = 11;
		formPanel.add(actionButtons, constraints);

		JButton backButton = createButton("Back to Dashboard", new Color(100, 116, 139), event -> {
			dispose();
			new DashboardFrame().setVisible(true);
		});
		constraints.gridy = 12;
		formPanel.add(backButton, constraints);

		return formPanel;
	}

	private JPanel buildTablePanel() {
		JPanel tablePanel = new JPanel(new BorderLayout(0, 12));
		tablePanel.setBackground(new Color(30, 41, 59));
		tablePanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(51, 65, 85), 1, true),
				BorderFactory.createEmptyBorder(18, 18, 18, 18)));

		JLabel tableTitle = new JLabel("Doctor Table");
		tableTitle.setForeground(Color.WHITE);
		tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		tablePanel.add(tableTitle, BorderLayout.NORTH);

		doctorTable.setRowHeight(28);
		doctorTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		doctorTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
		doctorTable.getTableHeader().setBackground(new Color(14, 165, 233));
		doctorTable.getTableHeader().setForeground(Color.WHITE);
		doctorTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		doctorTable.getSelectionModel().addListSelectionListener(this::handleTableSelection);

		JScrollPane scrollPane = new JScrollPane(doctorTable);
		scrollPane.setPreferredSize(new Dimension(520, 560));
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		return tablePanel;
	}

	private void addDoctor() {
		Doctor doctor = buildDoctorFromFields(false);
		if (doctor == null) {
			return;
		}

		if (doctorService.addDoctor(doctor)) {
			JOptionPane.showMessageDialog(this, "Doctor added successfully.", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			clearFields();
			loadDoctors();
		} else {
			JOptionPane.showMessageDialog(this, "Unable to add doctor.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateDoctor() {
		Doctor doctor = buildDoctorFromFields(true);
		if (doctor == null) {
			return;
		}

		if (doctorService.updateDoctor(doctor)) {
			JOptionPane.showMessageDialog(this, "Doctor updated successfully.", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			loadDoctors();
		} else {
			JOptionPane.showMessageDialog(this, "Unable to update doctor.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void deleteDoctor() {
		String doctorIdText = doctorIdField.getText().trim();
		if (doctorIdText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Select a doctor to delete.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int doctorId;
		try {
			doctorId = Integer.parseInt(doctorIdText);
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(this, "Doctor ID must be numeric.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int response = JOptionPane.showConfirmDialog(this, "Delete selected doctor?", "Confirm Delete",
				JOptionPane.YES_NO_OPTION);
		if (response != JOptionPane.YES_OPTION) {
			return;
		}

		if (doctorService.deleteDoctor(doctorId)) {
			JOptionPane.showMessageDialog(this, "Doctor deleted successfully.", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			clearFields();
			loadDoctors();
		} else {
			JOptionPane.showMessageDialog(this, "Unable to delete doctor.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void searchDoctors() {
		String keyword = searchField.getText().trim();
		tableModel.setRowCount(0);

		List<Doctor> doctors = keyword.isEmpty() ? doctorService.getAllDoctors() : doctorService.searchDoctor(keyword);
		for (Doctor doctor : doctors) {
			tableModel.addRow(new Object[]{doctor.getDoctorId(), doctor.getDoctorName(),
					doctor.getSpecialization(), doctor.getAvailability()});
		}
	}

	private void loadDoctors() {
		tableModel.setRowCount(0);
		for (Doctor doctor : doctorService.getAllDoctors()) {
			tableModel.addRow(new Object[]{doctor.getDoctorId(), doctor.getDoctorName(),
					doctor.getSpecialization(), doctor.getAvailability()});
		}
	}

	private Doctor buildDoctorFromFields(boolean requireId) {
		String idText = doctorIdField.getText().trim();
		String name = doctorNameField.getText().trim();
		String specialization = specializationField.getText().trim();
		String availability = availabilityField.getText().trim();

		if (requireId && idText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please select a doctor from the table.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}

		if (name.isEmpty() || specialization.isEmpty() || availability.isEmpty()) {
			JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}

		int doctorId = 0;
		if (!idText.isEmpty()) {
			try {
				doctorId = Integer.parseInt(idText);
			} catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(this, "Doctor ID must be numeric.", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return null;
			}
		}

		return new Doctor(doctorId, name, specialization, availability);
	}

	private void handleTableSelection(ListSelectionEvent event) {
		if (event.getValueIsAdjusting()) {
			return;
		}

		int selectedRow = doctorTable.getSelectedRow();
		if (selectedRow < 0) {
			return;
		}

		doctorIdField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 0)));
		doctorNameField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
		specializationField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
		availabilityField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
	}

	private void clearFields() {
		doctorIdField.setText("");
		doctorNameField.setText("");
		specializationField.setText("");
		availabilityField.setText("");
		searchField.setText("");
		doctorTable.clearSelection();
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