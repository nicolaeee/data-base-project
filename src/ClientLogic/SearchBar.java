package ClientLogic;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SearchBar {

    private static final String DB_URL = "jdbc:mysql://localhost/librarie";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public void handleSearch(String searchQuery, JFrame parentFrame) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Introduceți denumirea unei cărți pentru căutare!");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM carti WHERE titlu LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchQuery.trim() + "%");

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
                JOptionPane.showMessageDialog(parentFrame, "Nu au fost găsite produse cu acest titlu.");
            } else {
                JOptionPane.showMessageDialog(parentFrame, results.toString());
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, "Eroare la căutarea în baza de date: " + ex.getMessage());
        }
    }
}
