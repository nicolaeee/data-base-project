package LogInLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogInterface {

    private final String DB_URL = "jdbc:mysql://localhost/librarie";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    public static void main(String[] args) {
        LogInterface app = new LogInterface();
        app.createUI();
    }

    private void createUI() {
        // Crearea ferestrei principale
        JFrame frame = new JFrame("Logare Librarie");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(5, 1));

        // Crearea campurilor pentru utilizator si parola
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new FlowLayout());
        JLabel userLabel = new JLabel("Nume utilizator:");
        JTextField userField = new JTextField(20);
        userPanel.add(userLabel);
        userPanel.add(userField);

        JPanel passPanel = new JPanel();
        passPanel.setLayout(new FlowLayout());
        JLabel passLabel = new JLabel("Parola:");
        JPasswordField passField = new JPasswordField(20);
        passPanel.add(passLabel);
        passPanel.add(passField);

        // Crearea butoanelor de logare si creare cont
        JButton loginButton = new JButton("Logare");
        JButton adminButton = new JButton("Logare ca Administrator");
        JButton createAccountButton = new JButton("Creare Cont");

        // Adaugarea componentelor in fereastra
        frame.add(userPanel);
        frame.add(passPanel);
        frame.add(loginButton);
        frame.add(adminButton);
        frame.add(createAccountButton);

        // Actiune pentru butonul Logare
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                // Logica pentru logarea utilizatorului
                System.out.println("Utilizator: " + username + ", Parola: " + password);
            }
        });

        // Actiune pentru butonul Logare ca Administrator
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                // Logica pentru logarea ca administrator
                System.out.println("Administrator: " + username + ", Parola: " + password);
            }
        });

        // Actiune pentru butonul Creare Cont
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                if (createAccount(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Cont creat cu succes!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Eroare la crearea contului.");
                }
            }
        });

        // Afisarea ferestrei
        frame.setVisible(true);
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
}
