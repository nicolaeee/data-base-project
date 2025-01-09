package ClientLogic;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SelectGen {

    public void selectGenres(JFrame frame) {
        // Genurile disponibile
        String[] genres = {"Drama", "Copii", "Istorie", "Știință", "Ficțiune", "Clasică"};

        // Afișează o listă cu posibilitatea de a selecta multiple genuri
        JList<String> genreList = new JList<>(genres);
        genreList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(genreList);

        int option = JOptionPane.showConfirmDialog(
                frame,
                scrollPane,
                "Selectați unul sau mai multe genuri:",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        // Dacă utilizatorul apasă OK, procesează selecția
        if (option == JOptionPane.OK_OPTION) {
            // Obține genurile selectate
            StringBuilder selectedGenres = new StringBuilder();
            for (String genre : genreList.getSelectedValuesList()) {
                selectedGenres.append("'").append(genre).append("',");
            }

            // Verifică dacă sunt genuri selectate
            if (selectedGenres.length() > 0) {
                // Elimină ultima virgulă
                selectedGenres.deleteCharAt(selectedGenres.length() - 1);

                // Caută cărțile după genurile selectate
                searchBooksByGenres(selectedGenres.toString(), frame);
            } else {
                JOptionPane.showMessageDialog(frame, "Nu ați selectat niciun gen!");
            }
        }
    }

    private void searchBooksByGenres(String genres, JFrame frame) {
        String DB_URL = "jdbc:mysql://localhost/librarie";
        String USERNAME = "root";
        String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM carti WHERE gen IN (" + genres + ")";
            PreparedStatement stmt = conn.prepareStatement(sql);

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
                JOptionPane.showMessageDialog(frame, "Nu au fost găsite cărți pentru genurile selectate.");
            } else {
                JOptionPane.showMessageDialog(frame, results.toString());
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Eroare la căutarea în baza de date: " + ex.getMessage());
        }
    }
}
