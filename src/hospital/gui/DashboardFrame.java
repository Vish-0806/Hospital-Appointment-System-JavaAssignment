package hospital.gui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;

/**
 * Main application dashboard.
 */
public class DashboardFrame extends JFrame {

	private static final Color BACKGROUND = new Color(15, 23, 42);
	private static final Color CARD_BACKGROUND = new Color(30, 41, 59);
	private static final Color PRIMARY = new Color(14, 165, 233);
	private static final Color DANGER = new Color(239, 68, 68);

	/**
	 * Creates the dashboard window.
	 */
	public DashboardFrame() {
		setTitle("Hospital Appointment Management System - Dashboard");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(980, 640);
		setLocationRelativeTo(null);
		setResizable(true);
		JScrollPane scrollPane = new JScrollPane(buildContentPane());
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setBackground(BACKGROUND);
		setContentPane(scrollPane);
	}

	private JPanel buildContentPane() {
		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.setBackground(BACKGROUND);
		rootPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

		JPanel headerPanel = new JPanel();
		headerPanel.setOpaque(false);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

		JLabel titleLabel = new JLabel("Hospital Appointment Management System", SwingConstants.CENTER);
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));

		JLabel subtitleLabel = new JLabel("Dashboard", SwingConstants.CENTER);
		subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);
		subtitleLabel.setForeground(new Color(148, 163, 184));
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));

		headerPanel.add(titleLabel);
		headerPanel.add(subtitleLabel);
		rootPanel.add(headerPanel, BorderLayout.NORTH);

		JPanel cardPanel = new JPanel(new GridLayout(2, 3, 18, 18));
		cardPanel.setOpaque(false);

		cardPanel.add(createModuleCard("Register Patient", "Add a new patient record", PRIMARY,
				event -> openModule("hospital.gui.RegisterPatientFrame", "Register Patient")));
		cardPanel.add(createModuleCard("Manage Doctors", "Add, update, search, and delete doctors", PRIMARY,
				event -> openModule("hospital.gui.DoctorManagementFrame", "Manage Doctors")));
		cardPanel.add(createModuleCard("Book Appointment", "Create or cancel appointments", PRIMARY,
				event -> openModule("hospital.gui.AppointmentFrame", "Book Appointment")));
		cardPanel.add(createModuleCard("View Appointments", "Review all booked appointments", PRIMARY,
				event -> openModule("hospital.gui.ViewAppointmentsFrame", "View Appointments")));
		cardPanel.add(createModuleCard("Manage Patients", "Search, update, and delete patients", PRIMARY,
				event -> openModule("hospital.gui.PatientManagementFrame", "Manage Patients")));
		cardPanel.add(createModuleCard("Logout", "Return to the login screen", DANGER,
				event -> handleLogout()));

		JScrollPane scrollPane = new JScrollPane(cardPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setBackground(BACKGROUND);
		rootPanel.add(scrollPane, BorderLayout.CENTER);
		return rootPanel;
	}

	private JPanel createModuleCard(String title, String description, Color accent, java.awt.event.ActionListener listener) {
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(CARD_BACKGROUND);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(51, 65, 85), 1, true),
				BorderFactory.createEmptyBorder(18, 18, 18, 18)));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

		JLabel descriptionLabel = new JLabel("<html><body style='width: 220px; color: #cbd5e1;'>" + description + "</body></html>");
		descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

		JButton actionButton = new JButton(title);
		actionButton.setFocusPainted(false);
		actionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		actionButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
		actionButton.setForeground(Color.WHITE);
		actionButton.setBackground(accent);
		actionButton.setPreferredSize(new Dimension(180, 38));
		actionButton.addActionListener(listener);

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		descriptionLabel.setAlignmentX(LEFT_ALIGNMENT);
		textPanel.add(titleLabel);
		textPanel.add(javax.swing.Box.createVerticalStrut(8));
		textPanel.add(descriptionLabel);

		card.add(textPanel, BorderLayout.CENTER);
		card.add(actionButton, BorderLayout.SOUTH);
		return card;
	}

	private void handleLogout() {
		dispose();
		SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
	}

	private void openModule(String className, String title) {
		try {
			Class<?> moduleClass = Class.forName(className);
			Object instance = moduleClass.getDeclaredConstructor().newInstance();

			if (instance instanceof JFrame frame) {
				frame.setLocationRelativeTo(this);
				frame.setVisible(true);
				return;
			}

			JOptionPane.showMessageDialog(this,
					title + " is not available as a window yet.",
					"Module Not Ready",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ClassNotFoundException exception) {
			JOptionPane.showMessageDialog(this,
					title + " is not implemented yet.",
					"Module Missing",
					JOptionPane.WARNING_MESSAGE);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
				InvocationTargetException exception) {
			JOptionPane.showMessageDialog(this,
					"Unable to open " + title + ": " + exception.getMessage(),
					"Navigation Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			requestFocusInWindow();
		}
	}

	private static final long serialVersionUID = 1L;
}
