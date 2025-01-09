package ClientLogic;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PriceFilter {

    public void filterByPrice(JFrame frame) {
        // Crearea componentelor pentru introducerea limitelor de preț
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel minLabel = new JLabel("Introduceți prețul minim:");
        JTextField minField = new JTextField();
        minField.setPreferredSize(new java.awt.Dimension(200, 25));

        JLabel maxLabel = new JLabel("Introduceți prețul maxim:");
        JTextField maxField = new JTextField();
        maxField.setPreferredSize(new java.awt.Dimension(200, 25));

        panel.add(minLabel);
        panel.add(minField);
        panel.add(Box.createVerticalStrut(10)); // Spațiere între componente
        panel.add(maxLabel);
        panel.add(maxField);

        int option = JOptionPane.showConfirmDialog(
                frame,
                panel,
                "Filtrare după preț",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                double minPrice = Double.parseDouble(minField.getText().trim());
                double maxPrice = Double.parseDouble(maxField.getText().trim());

                if (minPrice > maxPrice) {
                    JOptionPane.showMessageDialog(frame, "Prețul minim trebuie să fie mai mic sau egal cu prețul maxim.");
                } else {
                    searchBooksByPrice(minPrice, maxPrice, frame);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Introduceți valori numerice valide pentru prețuri.");
            }
        }
    }

    private void searchBooksByPrice(double minPrice, double maxPrice, JFrame frame) {
        String DB_URL = "jdbc:mysql://localhost/librarie";
        String USERNAME = "root";
        String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM carti WHERE pret BETWEEN ? AND ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, minPrice);
            stmt.setDouble(2, maxPrice);

            ResultSet rs = stmt.executeQuery();
            StringBuilder results = new StringBuilder("Cărți găsite:\n");

            while (rs.next()) {
                results.append("Titlu: ").append(rs.getString("titlu"))
                        .append(", Autor: ").append(rs.getString("autor"))
                        .append(", Preț: ").append(rs.getDouble("pret"))
                        .append(", Gen: ").append(rs.getString("gen"))
                        .append("\n");
            }

            if (results.toString().equals("Cărți găsite:\n")) {
                JOptionPane.showMessageDialog(frame, "Nu au fost găsite cărți în intervalul de preț specificat.");
            } else {
                JOptionPane.showMessageDialog(frame, results.toString());
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Eroare la căutarea în baza de date: " + ex.getMessage());
        }
    }
}
