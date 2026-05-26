package hospital.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Login screen for the Hospital Appointment Management System.
 */
public class LoginFrame extends JFrame {

	private static final String ADMIN_USERNAME = "admin";
	private static final String ADMIN_PASSWORD = "admin123";

	private final JTextField usernameField;
	private final JPasswordField passwordField;

	/**
	 * Creates and configures the login window.
	 */
	public LoginFrame() {
		setTitle("Hospital Appointment Management System - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(520, 360);
		setLocationRelativeTo(null);
		setResizable(true);
		setLayout(new BorderLayout());

		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.setBackground(new Color(15, 23, 42));
		add(rootPanel, BorderLayout.CENTER);

		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(new Color(15, 23, 42));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 12, 24));

		JLabel titleLabel = new JLabel("Hospital Appointment Management System", SwingConstants.CENTER);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

		JLabel subtitleLabel = new JLabel("Admin Login", SwingConstants.CENTER);
		subtitleLabel.setForeground(new Color(148, 163, 184));
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

		headerPanel.add(titleLabel, BorderLayout.CENTER);
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

		JLabel usernameLabel = createLabel("Username");
		JLabel passwordLabel = createLabel("Password");

		usernameField = new JTextField();
		passwordField = new JPasswordField();
		styleInput(usernameField);
		styleInput(passwordField);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		formPanel.add(usernameLabel, constraints);

		constraints.gridy = 1;
		formPanel.add(usernameField, constraints);

		constraints.gridy = 2;
		formPanel.add(passwordLabel, constraints);

		constraints.gridy = 3;
		formPanel.add(passwordField, constraints);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(15, 23, 42));

		JButton loginButton = createButton("Login", new Color(14, 165, 233));
		JButton exitButton = createButton("Exit", new Color(71, 85, 105));

		loginButton.addActionListener(event -> handleLogin());
		exitButton.addActionListener(event -> System.exit(0));

		buttonPanel.add(loginButton);
		buttonPanel.add(exitButton);

		constraints.gridy = 4;
		formPanel.add(buttonPanel, constraints);

		rootPanel.add(formPanel, BorderLayout.CENTER);
	}

	/**
	 * Launches the login screen.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
	}

	private void handleLogin() {
		String username = usernameField.getText().trim();
		String password = new String(passwordField.getPassword());

		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
			JOptionPane.showMessageDialog(this, "Login successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
			dispose();
			new DashboardFrame().setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(new Color(226, 232, 240));
		label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		return label;
	}

	private void styleInput(JTextField field) {
		field.setPreferredSize(new Dimension(320, 36));
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
		button.setPreferredSize(new Dimension(120, 36));
		button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
		return button;
	}

}
