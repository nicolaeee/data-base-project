package ClientLogic;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CartLogic {

    private List<String> cartItems = new ArrayList<>(); // Listă pentru a stoca titlurile cărților adăugate

    // Adăugarea unui produs în coș
    public void addToCart(String bookTitle) {
        cartItems.add(bookTitle);
        JOptionPane.showMessageDialog(null, bookTitle + " a fost adăugat în coș!");
    }

    // Afișarea coșului
    public void showCart(JFrame frame) {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Coșul este gol.");
        } else {
            StringBuilder cartContent = new StringBuilder("Produse în coș:\n");
            for (String item : cartItems) {
                cartContent.append(item).append("\n");
            }
            JOptionPane.showMessageDialog(frame, cartContent.toString());
        }
    }
}
