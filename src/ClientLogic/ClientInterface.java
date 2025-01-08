package ClientLogic;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClientInterface {

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

        // Setarea unui layout modern
        frame.setLayout(new BorderLayout());

        // Panou pentru bara de sus
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Bine ați venit la Magazinul de Cărți!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel);

        // Panou pentru butoane
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 2, 10, 10));

        // Lista pentru coșul de cumpărături
        List<String> shoppingCart = new ArrayList<>();

        // Crearea butoanelor
        JButton searchButton = createStyledButton("Caută produs după denumire");
        searchButton.addActionListener(e -> {
            String searchQuery = JOptionPane.showInputDialog(frame, "Introduceți denumirea produsului:");
            // Logica pentru căutarea produsului
        });

        JButton categoryButton = createStyledButton("Selectează genul");
        categoryButton.addActionListener(e -> {
            String[] categories = {"Ficțiune", "Știință", "Istorie", "Copii"};
            String category = (String) JOptionPane.showInputDialog(frame, "Selectați un gen:", "Genul",
                    JOptionPane.QUESTION_MESSAGE, null, categories, categories[0]);
            JOptionPane.showMessageDialog(frame, "Ați selectat categoria: " + category);
        });

        JButton sortButton = createStyledButton("Ordonează produse după preț");
        sortButton.addActionListener(e -> {
            String[] options = {"Crescător", "Descrescător"};
            String choice = (String) JOptionPane.showInputDialog(frame, "Selectați ordinea:", "Ordonare",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            JOptionPane.showMessageDialog(frame, "Produsele vor fi ordonate " + choice);
        });

        JButton addToCartButton = createStyledButton("Adaugă în coșul de cumpărături");
        addToCartButton.addActionListener(e -> {
            String product = JOptionPane.showInputDialog(frame, "Introduceți numele produsului de adăugat în coș:");
            shoppingCart.add(product);
            JOptionPane.showMessageDialog(frame, product + " a fost adăugat în coșul de cumpărături.");
        });

        // Adăugarea butoanelor în panou
        buttonPanel.add(searchButton);
        buttonPanel.add(categoryButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(addToCartButton);

        // Adăugarea panourilor în fereastră
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Afisarea ferestrei
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(70, 130, 180)); // Culoare similară cu cea din Admin
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }
}
