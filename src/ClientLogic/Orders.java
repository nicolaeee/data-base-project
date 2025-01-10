package ClientLogic;

import AdminLogic.Book;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class Orders {

    private static final String DB_URL = "jdbc:mysql://localhost/librarie";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public Orders(List<Book> cartItems, double totalPrice) {
        JDialog dialog = new JDialog((Frame) null, "Formular Comandă", true); // Modal JDialog
        dialog.setSize(400, 600);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        // Afișarea produselor din coș
        JTextArea orderSummary = new JTextArea(10, 30);
        orderSummary.setEditable(false);
        StringBuilder summary = new StringBuilder("Produse comandate:\n");
        for (Book book : cartItems) {
            summary.append("Titlu: ").append(book.getTitlu())
                    .append(", Autor: ").append(book.getAutor())
                    .append("\n");
        }
        summary.append("\nPreț total: ").append(totalPrice).append(" RON");
        orderSummary.setText(summary.toString());
        dialog.add(new JScrollPane(orderSummary), BorderLayout.NORTH);

        // Formular pentru datele clientului
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();

        formPanel.add(new JLabel("Nume:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Prenume:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Telefon:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Adresă:"));
        formPanel.add(addressField);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Buton pentru trimiterea comenzii
        JButton submitButton = new JButton("Trimite Comanda");
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() ||
                    phone.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Toate câmpurile sunt obligatorii!");
                return;
            }

            // Salvarea comenzii în baza de date
            try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                String sql = "INSERT INTO orders (nume, prenume, posta, numar, adresa, titlulcartii, autor) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                for (Book book : cartItems) {
                    stmt.setString(1, name);
                    stmt.setString(2, surname);
                    stmt.setString(3, email);
                    stmt.setString(4, phone);
                    stmt.setString(5, address);
                    stmt.setString(6, book.getTitlu());
                    stmt.setString(7, book.getAutor());
                    stmt.addBatch();
                }

                stmt.executeBatch();
                JOptionPane.showMessageDialog(dialog, "Comanda a fost plasată cu succes!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Eroare la trimiterea comenzii: " + ex.getMessage());
            }
        });

        dialog.add(submitButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
