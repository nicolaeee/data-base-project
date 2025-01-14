package ClientLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientInterface {

    private JPanel sideMenuPanel;
    private boolean isMenuVisible = false;
    private CartLogic cartLogic = new CartLogic(); // Instanță globală pentru coș
    private HistoryLogic historyLogic = new HistoryLogic(); // Instanță globală pentru istoric


    public static void main(String[] args) {
        ClientInterface app = new ClientInterface();
        app.createUI();
    }

    public void createUI() {
        JFrame frame = new JFrame("Client - Magazin de Carti");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Bine ați venit la Magazinul de Cărți!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchBar = new JTextField();
        searchBar.setFont(new Font("Arial", Font.PLAIN, 16));
        searchBar.setPreferredSize(new Dimension(400, 30));
        searchPanel.add(searchBar);

        // Butonul pentru coș
        ImageIcon cartIcon = new ImageIcon("src/Images/cart.png");
        Image img = cartIcon.getImage();
        Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        JButton cartButton = new JButton(scaledIcon);
        cartButton.setPreferredSize(new Dimension(40, 40));
        cartButton.setBackground(new Color(225, 225, 225));
        cartButton.setForeground(Color.WHITE);
        cartButton.setFocusPainted(false);
        cartButton.setBorder(BorderFactory.createEmptyBorder());
        cartButton.addActionListener(e -> cartLogic.showCart(frame));
        searchPanel.add(cartButton);

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
        sideMenuPanel.setLayout(new GridLayout(5, 1, 5, 5));
        sideMenuPanel.setBackground(new Color(220, 220, 220));
        sideMenuPanel.setPreferredSize(new Dimension(150, 0));
        sideMenuPanel.setVisible(false);

        // Adăugare butoane în meniul lateral
        JButton searchButton = createStyledButton("Caută produs");
        JButton categoryButton = createStyledButton("Selectează genul");
        JButton sortButton = createStyledButton("Preț");
        JButton verifyOrderButton = createStyledButton("Verifică Comandă");
        JButton historyButton = createStyledButton("Istoric Utilizare");
        sideMenuPanel.add(searchButton);
        sideMenuPanel.add(categoryButton);
        sideMenuPanel.add(sortButton);
        sideMenuPanel.add(verifyOrderButton);
        sideMenuPanel.add(historyButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(sideMenuPanel, BorderLayout.EAST);

        setupSearchButton(searchButton, frame);
        setupCategoryButton(categoryButton, frame);
        setupPriceButton(sortButton, frame);
        setupVerifyOrderButton(verifyOrderButton, frame);
        historyButton.addActionListener(e -> historyLogic.showHistory(frame));


        // Integrarea clasei SearchBar pentru searchBar
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String searchQuery = searchBar.getText().trim();
                    SearchBar searchBarLogic = new SearchBar(cartLogic);
                    searchBarLogic.handleSearch(searchQuery, frame);
                    historyLogic.addAction("Căutare efectuată: " + searchQuery);
                    searchBarLogic.handleSearch(searchQuery, frame);
                }
            }
        });

        frame.setVisible(true);
    }

    private void toggleMenu() {
        isMenuVisible = !isMenuVisible;
        sideMenuPanel.setVisible(isMenuVisible);
        historyLogic.addAction("Meniul lateral " + (isMenuVisible ? "deschis" : "închis"));
    }

    private void setupSearchButton(JButton searchButton, JFrame frame) {
        searchButton.addActionListener(e -> {
            historyLogic.addAction("Butonul 'Caută produs' apăsat");
            SearchButton searchButtonLogic = new SearchButton();
            searchButtonLogic.performSearch(frame);
        });
    }




    private void setupCategoryButton(JButton categoryButton, JFrame frame) {
        categoryButton.addActionListener(e -> {
            historyLogic.addAction("Butonul 'Selectează genul' apăsat");
            SelectGen selectGenLogic = new SelectGen();
            selectGenLogic.selectGenres(frame);
        });
    }


    private void setupPriceButton(JButton priceButton, JFrame frame) {
        priceButton.addActionListener(e -> {
            PriceFilter priceFilterLogic = new PriceFilter();
            priceFilterLogic.filterByPrice(frame);
            historyLogic.addAction("Butonul 'Preț' apăsat");
            priceFilterLogic.filterByPrice(frame);
        });
    }

    private void setupVerifyOrderButton(JButton verifyOrderButton, JFrame frame) {
        verifyOrderButton.addActionListener(e -> {
            VerifyOrder verifyOrderLogic = new VerifyOrder();
            verifyOrderLogic.checkOrders(frame);
            historyLogic.addAction("Butonul 'Verifică Comandă' apăsat");
            verifyOrderLogic.checkOrders(frame);
        });
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
