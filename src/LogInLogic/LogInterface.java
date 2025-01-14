package LogInLogic;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ClientLogic.ClientInterface;
import AdminLogic.AdminInterface;

public class LogInterface {

    private final String DB_URL = "jdbc:mysql://localhost/librarie";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LogInterface::new);
    }

    public LogInterface() {
        createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("Logare Librarie");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Librarius");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Nume utilizator:");
        JTextField userField = new JTextField(20);
        JLabel passLabel = new JLabel("Parola:");
        JPasswordField passField = new JPasswordField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout());

        JButton loginButton = new JButton("Logare");
        JButton adminButton = new JButton("Logare ca Administrator");
        JButton createAccountButton = new JButton("Creare Cont");

        customizeButton(loginButton);
        customizeButton(adminButton);
        customizeButton(createAccountButton);

        buttonPanel.add(loginButton);
        buttonPanel.add(adminButton);
        buttonPanel.add(createAccountButton);

        loginButton.addActionListener(e -> handleLogin(userField.getText(), new String(passField.getPassword()), frame, "users", "utilizator"));
        adminButton.addActionListener(e -> handleLogin(userField.getText(), new String(passField.getPassword()), frame, "admins", "administrator"));
        createAccountButton.addActionListener(e -> handleCreateAccount(userField.getText(), new String(passField.getPassword()), frame));

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(formPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void handleLogin(String username, String password, JFrame frame, String tableName, String userType) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Completați toate câmpurile!", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (authenticate(username, password, tableName)) {
            JOptionPane.showMessageDialog(frame, "Autentificare reușită! Bun venit, " + userType + " " + username + "!");
            SwingUtilities.invokeLater(() -> {
                if (tableName.equals("users")) {
                    ClientInterface clientApp = new ClientInterface();
                    clientApp.createUI();
                } else {
                    AdminInterface adminApp = new AdminInterface();
                    adminApp.createUI();
                }
            });
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Nume de utilizator sau parolă incorectă!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean authenticate(String username, String password, String tableName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM " + tableName + " WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void handleCreateAccount(String username, String password, JFrame frame) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Completați toate câmpurile!", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (createAccount(username, password)) {
            JOptionPane.showMessageDialog(frame, "Cont creat cu succes!");
        } else {
            JOptionPane.showMessageDialog(frame, "Eroare la crearea contului.", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean createAccount(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void customizeButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    public void setVisible(boolean b) {
    }
}
