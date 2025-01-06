package ClientLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ClientInterface {

    public static void main(String[] args) {
        ClientInterface app = new ClientInterface();
        app.createUI();
    }

    private void createUI() {
        // Crearea ferestrei principale
        JFrame frame = new JFrame("Client - Magazin de Carti");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new GridLayout(8, 1));

        // Lista pentru cosul de cumparaturi
        List<String> shoppingCart = new ArrayList<>();

        // Buton pentru cautare produse dupa denumire
        JButton searchButton = new JButton("Cauta produs dupa denumire");
        searchButton.addActionListener(e -> {
            String searchQuery = JOptionPane.showInputDialog(frame, "Introduceti denumirea produsului:");
            JOptionPane.showMessageDialog(frame, "Cautare pentru: " + searchQuery);
        });

        // Buton pentru selectarea unei categorii de produse
        JButton categoryButton = new JButton("Selecteaza categorie");
        categoryButton.addActionListener(e -> {
            String[] categories = {"Fictiune", "Stiinta", "Istorie", "Copii"};
            String category = (String) JOptionPane.showInputDialog(frame, "Selectati o categorie:", "Categorii",
                    JOptionPane.QUESTION_MESSAGE, null, categories, categories[0]);
            JOptionPane.showMessageDialog(frame, "Ati selectat categoria: " + category);
        });

        // Buton pentru ordonarea produselor
        JButton sortButton = new JButton("Ordoneaza produse dupa pret");
        sortButton.addActionListener(e -> {
            String[] options = {"Crescator", "Descrescator"};
            String choice = (String) JOptionPane.showInputDialog(frame, "Selectati ordinea:", "Ordonare",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            JOptionPane.showMessageDialog(frame, "Produsele vor fi ordonate " + choice);
        });

        // Buton pentru adaugarea in cosul de cumparaturi
        JButton addToCartButton = new JButton("Adauga in cosul de cumparaturi");
        addToCartButton.addActionListener(e -> {
            String product = JOptionPane.showInputDialog(frame, "Introduceti numele produsului de adaugat in cos:");
            shoppingCart.add(product);
            JOptionPane.showMessageDialog(frame, product + " a fost adaugat in cosul de cumparaturi.");
        });

        // Buton pentru vizualizarea unui produs
        JButton viewProductButton = new JButton("Vizualizeaza produs");
        viewProductButton.addActionListener(e -> {
            String product = JOptionPane.showInputDialog(frame, "Introduceti numele produsului pentru vizualizare:");
            JOptionPane.showMessageDialog(frame, "Informatii despre produs: " + product);
        });

        // Buton pentru adaugarea unui comentariu
        JButton addCommentButton = new JButton("Adauga comentariu");
        addCommentButton.addActionListener(e -> {
            String comment = JOptionPane.showInputDialog(frame, "Introduceti comentariul dvs.:");
            JOptionPane.showMessageDialog(frame, "Comentariu adaugat: " + comment);
        });

        // Buton pentru filtrare dupa criterii
        JButton filterButton = new JButton("Filtreaza produse");
        filterButton.addActionListener(e -> {
            String[] filters = {"Pret", "Autor", "Genul cartii", "Editura"};
            String filter = (String) JOptionPane.showInputDialog(frame, "Selectati criteriul de filtrare:", "Filtrare",
                    JOptionPane.QUESTION_MESSAGE, null, filters, filters[0]);
            JOptionPane.showMessageDialog(frame, "Filtrare aplicata: " + filter);
        });

        // Adaugarea butoanelor in fereastra
        frame.add(searchButton);
        frame.add(categoryButton);
        frame.add(sortButton);
        frame.add(addToCartButton);
        frame.add(viewProductButton);
        frame.add(addCommentButton);
        frame.add(filterButton);

        // Afisarea ferestrei
        frame.setVisible(true);
    }
}
