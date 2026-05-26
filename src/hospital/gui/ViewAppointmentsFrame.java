package hospital.gui;

import hospital.services.AppointmentService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Displays appointments using a JOIN query across patient and doctor tables.
 */
public class ViewAppointmentsFrame extends JFrame {

	private final AppointmentService appointmentService;
	private final DefaultTableModel tableModel;
	private final JTable appointmentTable;

	/**
	 * Creates and configures the appointment viewer.
	 */
	public ViewAppointmentsFrame() {
		appointmentService = new AppointmentService();
		tableModel = new DefaultTableModel(
				new Object[]{"Appointment ID", "Patient Name", "Doctor Name", "Date", "Status"}, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		appointmentTable = new JTable(tableModel);

		setTitle("Hospital Appointment Management System - View Appointments");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(980, 640);
		setLocationRelativeTo(null);
		setResizable(true);
		JScrollPane scrollPane = new JScrollPane(buildContentPane());
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setBackground(new Color(15, 23, 42));
		setContentPane(scrollPane);
		loadAppointments();
	}

	private JPanel buildContentPane() {
		JPanel rootPanel = new JPanel(new BorderLayout(0, 18));
		rootPanel.setBackground(new Color(15, 23, 42));
		rootPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);

		JLabel titleLabel = new JLabel("View Appointments");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));

		JLabel subtitleLabel = new JLabel("Appointments with patient and doctor details", SwingConstants.LEFT);
		subtitleLabel.setForeground(new Color(148, 163, 184));
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		headerPanel.add(titleLabel, BorderLayout.NORTH);
		headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
		rootPanel.add(headerPanel, BorderLayout.NORTH);

		JPanel bodyPanel = new JPanel(new BorderLayout(0, 12));
		bodyPanel.setBackground(new Color(30, 41, 59));
		bodyPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(51, 65, 85), 1, true),
				BorderFactory.createEmptyBorder(18, 18, 18, 18)));

		appointmentTable.setRowHeight(28);
		appointmentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		appointmentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
		appointmentTable.getTableHeader().setBackground(new Color(14, 165, 233));
		appointmentTable.getTableHeader().setForeground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(appointmentTable);
		scrollPane.setPreferredSize(new Dimension(900, 500));
		bodyPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setOpaque(false);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(8, 8, 8, 8);

		JButton refreshButton = createButton("Refresh", new Color(14, 165, 233));
		refreshButton.addActionListener(event -> loadAppointments());

		JButton backButton = createButton("Back to Dashboard", new Color(71, 85, 105));
		backButton.addActionListener(event -> {
			dispose();
			new DashboardFrame().setVisible(true);
		});

		constraints.gridx = 0;
		buttonPanel.add(refreshButton, constraints);
		constraints.gridx = 1;
		buttonPanel.add(backButton, constraints);

		bodyPanel.add(buttonPanel, BorderLayout.SOUTH);
		JScrollPane bodyScrollPane = new JScrollPane(bodyPanel);
		bodyScrollPane.setBorder(BorderFactory.createEmptyBorder());
		bodyScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		bodyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		bodyScrollPane.getViewport().setBackground(new Color(15, 23, 42));
		rootPanel.add(bodyScrollPane, BorderLayout.CENTER);
		return rootPanel;
	}

	private void loadAppointments() {
		tableModel.setRowCount(0);
		for (AppointmentService.AppointmentViewRow row : appointmentService.getAppointmentRowsWithJoin()) {
			tableModel.addRow(new Object[]{row.getAppointmentId(), row.getPatientName(), row.getDoctorName(),
					row.getAppointmentDate(), row.getStatus()});
		}
	}

	private JButton createButton(String text, Color background) {
		JButton button = new JButton(text);
		button.setFocusPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setFont(new Font("Segoe UI", Font.BOLD, 13));
		button.setForeground(Color.WHITE);
		button.setBackground(background);
		button.setPreferredSize(new Dimension(180, 38));
		button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
		return button;
	}

}