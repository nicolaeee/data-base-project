package ClientLogic;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SearchButton {

    private static final String DB_URL = "jdbc:mysql://localhost/librarie";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public void performSearch(JFrame parentFrame) {
        String[] options = {"Titlu", "Autor", "Pret", "Gen"};
        String selectedCriteria = (String) JOptionPane.showInputDialog(
                parentFrame,
                "După ce criteriu doriți să căutați?",
                "Selectați criteriul",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selectedCriteria != null) {
            String searchQuery = JOptionPane.showInputDialog(
                    parentFrame,
                    "Introduceți valoarea pentru criteriul " + selectedCriteria + ":"
            );

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                searchBooksByCriteria(selectedCriteria, searchQuery, parentFrame);
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Câmpul de căutare nu poate fi gol!");
            }
        }
    }

    private void searchBooksByCriteria(String criteria, String value, JFrame parentFrame) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "";

            // Construim query-ul în funcție de criteriu
            switch (criteria) {
                case "Titlu":
                    sql = "SELECT * FROM carti WHERE titlu LIKE ?";
                    break;
                case "Autor":
                    sql = "SELECT * FROM carti WHERE autor LIKE ?";
                    break;
                case "Pret":
                    sql = "SELECT * FROM carti WHERE pret = ?";
                    break;
                case "Gen":
                    sql = "SELECT * FROM carti WHERE gen LIKE ?";
                    break;
                default:
                    JOptionPane.showMessageDialog(parentFrame, "Criteriu invalid!");
                    return;
            }

            PreparedStatement stmt = conn.prepareStatement(sql);

            // Dacă criteriul este preț, îl tratăm ca valoare numerică
            if (criteria.equals("Pret")) {
                stmt.setDouble(1, Double.parseDouble(value));
            } else {
                stmt.setString(1, "%" + value + "%");
            }

            ResultSet rs = stmt.executeQuery();
            StringBuilder results = new StringBuilder("Rezultate găsite:\n");

            while (rs.next()) {
                results.append("Titlu: ").append(rs.getString("titlu"))
                        .append(", Autor: ").append(rs.getString("autor"))
                        .append(", Preț: ").append(rs.getDouble("pret"))
                        .append(", Gen: ").append(rs.getString("gen"))
                        .append("\n");
            }

            if (results.toString().equals("Rezultate găsite:\n")) {
                JOptionPane.showMessageDialog(parentFrame, "Nu au fost găsite produse cu acest criteriu.");
            } else {
                JOptionPane.showMessageDialog(parentFrame, results.toString());
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, "Eroare la căutarea în baza de date: " + ex.getMessage());
        }
    }
}
