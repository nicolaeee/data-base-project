package ClientLogic;

import AdminLogic.Book;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BooksInterface extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost/librarie";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private CartLogic cartLogic;

    public BooksInterface(CartLogic cartLogic) {
        this.cartLogic = cartLogic;
        setLayout(new GridLayout(0, 3, 10, 10)); // 3 coloane
        loadBooks();
    }

    private void loadBooks() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT id, titlu, autor, pret FROM carti";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String titlu = rs.getString("titlu");
                String autor = rs.getString("autor");
                double pret = rs.getDouble("pret");

                add(createBookCard(new Book(id, titlu, autor, pret)));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eroare la încărcarea cărților: " + e.getMessage());
        }
    }

    private JPanel createBookCard(Book book) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(book.getTitlu());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel authorLabel = new JLabel("Autor: " + book.getAutor());
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("Preț: " + book.getPret() + " RON");
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addToCartButton = new JButton("Adaugă în coș");
        addToCartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addToCartButton.addActionListener(e -> cartLogic.addToCart(book));

        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(authorLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(addToCartButton);
        card.add(Box.createVerticalStrut(10));

        return card;
    }
}
