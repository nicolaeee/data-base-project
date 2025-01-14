package AdminLogic;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SearchBooks {
    private final String DB_URL = "jdbc:mysql://localhost/librarie";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    public void searchAndOpenResults(String query, JFrame parent) {
        JDialog dialog = new JDialog(parent, "Rezultate Căutare", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        dialog.add(panel);

        // Zonă pentru afișarea rezultatelor
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> resultsList = new JList<>(listModel);
        panel.add(new JScrollPane(resultsList), BorderLayout.CENTER);

        searchBooks(query, listModel);

        resultsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedBook = resultsList.getSelectedValue();
                if (selectedBook != null) {
                    int bookId = Integer.parseInt(selectedBook.split(" - ")[0]); // Extragem ID-ul cărții
                    openBookDetailsDialog(bookId, dialog);
                }
            }
        });

        dialog.setVisible(true);
    }

    private void searchBooks(String query, DefaultListModel<String> listModel) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql;
            PreparedStatement preparedStatement;

            // Verificăm dacă query-ul este un număr (pentru a căuta cărți pe baza prețului)
            if (isNumeric(query)) {
                sql = "SELECT id, titlu, pret FROM carti WHERE pret <= ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, Integer.parseInt(query)); // Setăm prețul cărților
            } else {
                // Dacă nu este număr, căutăm după titlu
                sql = "SELECT id, titlu FROM carti WHERE titlu LIKE ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, "%" + query + "%"); // Căutăm titluri care conțin query-ul
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("titlu");
                // Adăugăm titlul și ID-ul cărții în listă
                listModel.addElement(id + " - " + title);
            }

            if (listModel.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nu s-au găsit cărți cu termenul specificat.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str); // Dacă reușește să convertească în int, înseamnă că este număr
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void openBookDetailsDialog(int bookId, JDialog parentDialog) {
        JDialog dialog = new JDialog(parentDialog, "Detalii Carte", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(parentDialog);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dialog.add(panel);

        JTextField idField = new JTextField(10);
        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField yearField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JTextField genreField = new JTextField(20);
        JTextField stockField = new JTextField(10);

        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Titlu:"));
        panel.add(titleField);
        panel.add(new JLabel("Autor:"));
        panel.add(authorField);
        panel.add(new JLabel("An:"));
        panel.add(yearField);
        panel.add(new JLabel("Preț:"));
        panel.add(priceField);
        panel.add(new JLabel("Gen:"));
        panel.add(genreField);
        panel.add(new JLabel("Stoc:"));
        panel.add(stockField);

        JButton updateButton = new JButton("Modifică");
        panel.add(updateButton);

        // Obține datele despre carte
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM carti WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                idField.setText(String.valueOf(resultSet.getInt("id")));
                titleField.setText(resultSet.getString("titlu"));
                authorField.setText(resultSet.getString("autor"));
                yearField.setText(String.valueOf(resultSet.getInt("an")));
                priceField.setText(String.valueOf(resultSet.getInt("pret")));
                genreField.setText(resultSet.getString("gen"));
                stockField.setText(String.valueOf(resultSet.getInt("stoc")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateButton.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                String sql = "UPDATE carti SET titlu = ?, autor = ?, an = ?, pret = ?, gen = ?, stoc = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, titleField.getText());
                preparedStatement.setString(2, authorField.getText());
                preparedStatement.setInt(3, Integer.parseInt(yearField.getText()));
                preparedStatement.setInt(4, Integer.parseInt(priceField.getText()));
                preparedStatement.setString(5, genreField.getText());
                preparedStatement.setInt(6, Integer.parseInt(stockField.getText()));
                preparedStatement.setInt(7, bookId);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(dialog, "Cartea a fost modificată cu succes!");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Eroare la modificarea cărții.", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        dialog.setVisible(true);
    }
}
