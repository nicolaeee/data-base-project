package ClientLogic;

import AdminLogic.Book;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CartLogic {

    private List<Book> cartItems = new ArrayList<>(); // Listă pentru cărți
    private double totalPrice = 0.0; // Preț total al coșului

    public void addToCart(Book book) {
        cartItems.add(book);
        totalPrice += book.getPret(); // Adaugă prețul cărții la total
        JOptionPane.showMessageDialog(null, book.getTitlu() + " a fost adăugată în coș!");
    }

    public void showCart(JFrame frame) {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Coșul este gol.");
        } else {
            JPanel cartPanel = new JPanel(new BorderLayout());
            JTextArea cartArea = new JTextArea(10, 30);
            cartArea.setEditable(false);

            StringBuilder cartContent = new StringBuilder("Produse în coș:\n");
            for (Book item : cartItems) {
                cartContent.append("Titlu: ").append(item.getTitlu())
                        .append(", Autor: ").append(item.getAutor())
                        .append(", Preț: ").append(item.getPret())
                        .append("\n");
            }
            cartContent.append("\nPreț total: ").append(totalPrice).append(" RON");

            cartArea.setText(cartContent.toString());
            cartPanel.add(new JScrollPane(cartArea), BorderLayout.CENTER);

            JButton placeOrderButton = new JButton("Plasează Comanda");
            placeOrderButton.addActionListener(e -> new Orders(cartItems, totalPrice)); // Deschide Orders

            cartPanel.add(placeOrderButton, BorderLayout.SOUTH);
            JOptionPane.showMessageDialog(frame, cartPanel, "Coșul meu", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
