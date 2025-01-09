package ClientLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClientInterface {

    private JPanel sideMenuPanel; // Panoul meniului lateral
    private boolean isMenuVisible = false; // Starea meniului

    public static void main(String[] args) {
        ClientInterface app = new ClientInterface();
        app.createUI();
    }

    public void createUI() {
        // Crearea ferestrei principale
        JFrame frame = new JFrame("Client - Magazin de Carti");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Centrarea ferestrei pe ecran
        frame.setLayout(new BorderLayout());

        // Panou pentru bara de sus
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Bine ați venit la Magazinul de Cărți!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Panou central pentru căutare
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchBar = new JTextField();
        searchBar.setFont(new Font("Arial", Font.PLAIN, 16));
        searchBar.setPreferredSize(new Dimension(400, 30));
        searchPanel.add(searchBar);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        // Butonul burger pentru meniul lateral
        JButton burgerButton = new JButton("☰");
        burgerButton.setFont(new Font("Arial", Font.BOLD, 18));
        burgerButton.setFocusPainted(false);
        burgerButton.setPreferredSize(new Dimension(40, 40));
        burgerButton.setBackground(new Color(70, 130, 180));
        burgerButton.setForeground(Color.WHITE);
        burgerButton.setBorder(BorderFactory.createEmptyBorder());
        burgerButton.addActionListener(e -> toggleMenu());
        topPanel.add(burgerButton, BorderLayout.EAST);

        // Panou pentru meniul lateral
        sideMenuPanel = new JPanel();
        sideMenuPanel.setLayout(new GridLayout(4, 1, 5, 5));
        sideMenuPanel.setBackground(new Color(220, 220, 220));
        sideMenuPanel.setPreferredSize(new Dimension(150, 0));
        sideMenuPanel.setVisible(false); // Inițial este ascuns

        // Adăugare butoane în meniul lateral
        JButton searchButton = createStyledButton("Caută produs");
        JButton categoryButton = createStyledButton("Selectează genul");
        JButton sortButton = createStyledButton("Ordonează produse");
        JButton addToCartButton = createStyledButton("Adaugă în coș");
        sideMenuPanel.add(searchButton);
        sideMenuPanel.add(categoryButton);
        sideMenuPanel.add(sortButton);
        sideMenuPanel.add(addToCartButton);

        // Adăugarea panourilor în fereastră
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(sideMenuPanel, BorderLayout.EAST);

        // Eveniment pentru căutare
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String searchQuery = searchBar.getText().trim();
                    if (!searchQuery.isEmpty()) {
                        searchBooks(searchQuery, frame);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Introduceți denumirea unei cărți pentru căutare!");
                    }
                }
            }
        });

        // Afisarea ferestrei
        frame.setVisible(true);
    }

    private void toggleMenu() {
        // Logica pentru afișarea/ascunderea meniului
        isMenuVisible = !isMenuVisible;
        sideMenuPanel.setVisible(isMenuVisible);
    }

    private void searchBooks(String searchQuery, JFrame frame) {
        String DB_URL = "jdbc:mysql://localhost/librarie";
        String USERNAME = "root";
        String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM carti WHERE titlu LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchQuery + "%");

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
                JOptionPane.showMessageDialog(frame, "Nu au fost găsite produse cu acest titlu.");
            } else {
                JOptionPane.showMessageDialog(frame, results.toString());
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Eroare la căutarea în baza de date: " + ex.getMessage());
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return button;
    }
}
