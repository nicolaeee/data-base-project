package ClientLogic;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchBar {

    private static final String DB_URL = "jdbc:mysql://localhost/librarie";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private CartLogic cartLogic; // Instanță pentru gestionarea coșului

    public SearchBar(CartLogic cartLogic) {
        this.cartLogic = cartLogic; // Primește instanța CartLogic
    }

    public void searchBooks(String searchQuery, JFrame parentFrame) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Introduceți denumirea unei cărți pentru căutare!");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM carti WHERE titlu LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchQuery.trim() + "%");

            ResultSet rs = stmt.executeQuery();

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JScrollPane scrollPane = new JScrollPane(panel);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            while (rs.next()) {
                String title = rs.getString("titlu");
                String author = rs.getString("autor");
                double price = rs.getDouble("pret");
                String genre = rs.getString("gen");

                JPanel bookPanel = new JPanel();
                bookPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                bookPanel.add(new JLabel("Titlu: " + title));
                bookPanel.add(new JLabel("Autor: " + author));
                bookPanel.add(new JLabel("Preț: " + price));
                bookPanel.add(new JLabel("Gen: " + genre));

                JButton addToCartButton = new JButton("Adaugă în coș");
                addToCartButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cartLogic.addToCart(title); // Folosește cartLogic pentru a adăuga cartea în coș
                        JOptionPane.showMessageDialog(parentFrame, "Cartea a fost adăugată în coș!");
                    }
                });

                bookPanel.add(addToCartButton);

                panel.add(bookPanel);
            }

            if (panel.getComponentCount() == 0) {
                JOptionPane.showMessageDialog(parentFrame, "Nu au fost găsite produse cu acest titlu.");
            } else {
                JOptionPane.showMessageDialog(parentFrame, scrollPane, "Rezultate Căutare", JOptionPane.PLAIN_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, "Eroare la căutarea în baza de date: " + ex.getMessage());
        }
    }

    public void handleSearch(String searchQuery, JFrame parentFrame) {
        searchBooks(searchQuery, parentFrame);
    }
}
