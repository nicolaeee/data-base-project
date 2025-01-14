package AdminLogic;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class AdminInterface extends JFrame {
    private final String DB_URL = "jdbc:mysql://localhost/librarie";
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private JPanel sideMenu;

    public AdminInterface() {
        setTitle("Admin - Library Management");
        setSize(800, 800); // Dimensiune mai mare pentru a permite spațiu suficient
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crearea unui panel principal cu BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // Titlu stilizat
        JLabel titleLabel = new JLabel("Admin Panel - Library Management");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panoul meniului lateral
        sideMenu = createSideMenu();
        sideMenu.setVisible(false); // Ascundem meniul la teral inițial
        mainPanel.add(sideMenu, BorderLayout.WEST);

        // Adăugarea unui buton de tip burger
        JButton burgerButton = new JButton("☰");
        burgerButton.setFont(new Font("Arial", Font.BOLD, 18));
        burgerButton.setFocusPainted(false);
        burgerButton.setBorderPainted(false);
        burgerButton.setBackground(Color.LIGHT_GRAY);

        // Crearea unui panou pentru bara de sus (topPanel)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(burgerButton, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Eveniment pentru afișarea/ascunderea meniului lateral
        burgerButton.addActionListener(e -> toggleSideMenu());
        addSearchBar(topPanel);

        // Crearea instanței Analytics pentru grafice
        Analytics analytics = new Analytics();

        // Crearea unui panou pentru grafice cu GridLayout
        JPanel chartPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        chartPanel.add(analytics.getCurrentStockChart());
        chartPanel.add(analytics.getOrdersByClientChart());

        // Adăugarea chartPanel în zona centrală a layout-ului principal
        mainPanel.add(chartPanel, BorderLayout.CENTER);

        setVisible(true);
    }



    private JPanel createSideMenu() {
        JPanel menuPanel = new JPanel();
        // Modificăm GridLayout să aibă o coloană (1), astfel încât butoanele să fie unul sub altul
        menuPanel.setLayout(new GridLayout(0, 1, 10, 10)); // 0 rânduri (determină automat câte sunt necesare), 1 coloană
        menuPanel.setBackground(new Color(230, 230, 230));
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        menuPanel.setPreferredSize(new Dimension(200, getHeight())); // Menținem lățimea

        // Adăugarea butoanelor în meniul lateral
        JButton addClientButton = createStyledButton("Adaugă Client");
        JButton viewClientsButton = createStyledButton("Vizualizează Clienți");
        JButton addBookButton = createStyledButton("Adaugă Carte");
        JButton deleteBookButton = createStyledButton("Șterge Carte");
        JButton filterBooksButton = createStyledButton("Filtrează Cărți");
        JButton verifyOrdersButton = createStyledButton("Verifică Comenzi");

        // Adăugăm butoanele unul sub altul
        menuPanel.add(addClientButton);
        menuPanel.add(viewClientsButton);
        menuPanel.add(addBookButton);
        menuPanel.add(deleteBookButton);
        menuPanel.add(filterBooksButton);
        menuPanel.add(verifyOrdersButton);

        // Evenimente pentru butoane
        addClientButton.addActionListener(e -> showAddClientDialog());
        viewClientsButton.addActionListener(e -> displayClients());
        addBookButton.addActionListener(e -> showAddBookDialog());
        deleteBookButton.addActionListener(e -> showDeleteBookDialog());
        filterBooksButton.addActionListener(e -> showFilterBooksDialog());
        verifyOrdersButton.addActionListener(e -> openVerifyOrders());

        return menuPanel;
    }

    private void openVerifyOrders() {
        VerifyOrdersAdmin verifyOrdersAdmin = new VerifyOrdersAdmin();
        verifyOrdersAdmin.checkOrders(this);
    }

    private void addSearchBar(JPanel topPanel) {
        JTextField searchBar = new JTextField(20);

        searchBar.addActionListener(e -> {
            String query = searchBar.getText();
            if (!query.isEmpty()) {
                SearchBooks searchBooks = new SearchBooks();
                searchBooks.searchAndOpenResults(query, this);
            } else {
                JOptionPane.showMessageDialog(this, "Introduceți un termen pentru căutare!", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        topPanel.add(searchBar, BorderLayout.CENTER);
    }


    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }

    private void toggleSideMenu() {
        sideMenu.setVisible(!sideMenu.isVisible());
        revalidate();
        repaint();
    }

    // Metoda pentru a deschide dialogul de filtrare a cărților
    private void showFilterBooksDialog() {
        JDialog dialog = new JDialog(this, "Filtrează Cărți după Autor", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dialog.add(panel);

        JTextField authorField = new JTextField(15);
        panel.add(new JLabel("Autor:"));
        panel.add(authorField);

        JButton filterButton = new JButton("Filtrează");
        panel.add(filterButton);

        filterButton.addActionListener(e -> {
            String author = authorField.getText();
            displayBooksByAuthor(author);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    // Metoda pentru a vizualiza cărțile filtrate după autor și ordonate
    private void displayBooksByAuthor(String author) {
        StringBuilder booksList = new StringBuilder("Cărți de autorul " + author + ":\n");

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            // Modificarea interogării pentru a adăuga ordonarea după an (descrescător) și gen (crescător)
            String sql = "SELECT * FROM carti WHERE autor = ? ORDER BY an DESC, gen ASC";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, author);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("titlu");
                int year = resultSet.getInt("an");
                String genre = resultSet.getString("gen");

                booksList.append("Titlu: ").append(title)
                        .append(", An: ").append(year)
                        .append(", Gen: ").append(genre).append("\n");
            }

            JOptionPane.showMessageDialog(this, booksList.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void showAddClientDialog() {
        JDialog dialog = new JDialog(this, "Adaugă Client", true);
        dialog.setSize(300, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dialog.add(panel);

        JTextField nameField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField cnpField = new JTextField(15);

        panel.add(new JLabel("Nume:"));
        panel.add(nameField);
        panel.add(new JLabel("Telefon:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("CNP:"));
        panel.add(cnpField);

        JButton addButton = new JButton("Adaugă");
        panel.add(addButton);

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String cnp = cnpField.getText();

            if (addClientToDatabase(name, phone, email, cnp)) {
                JOptionPane.showMessageDialog(dialog, "Client adăugat cu succes!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Eroare la adăugarea clientului.", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private boolean addClientToDatabase(String name, String phone, String email, String cnp) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO client (nume, telefon, posta, cnp) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, cnp);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void displayClients() {
        StringBuilder clientsList = new StringBuilder("Clienți:\n");

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM client";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String name = resultSet.getString("nume");
                String phone = resultSet.getString("telefon");
                String email = resultSet.getString("posta");
                String cnp = resultSet.getString("cnp");

                clientsList.append("Nume: ").append(name)
                        .append(", Telefon: ").append(phone)
                        .append(", Email: ").append(email)
                        .append(", CNP: ").append(cnp).append("\n");
            }

            JOptionPane.showMessageDialog(this, clientsList.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metoda pentru a deschide un dialog de adăugare a unei cărți
    private void showAddBookDialog() {
        JDialog dialog = new JDialog(this, "Adaugă Carte", true);
        dialog.setSize(300, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dialog.add(panel);

        JTextField titleField = new JTextField(15);
        JTextField authorField = new JTextField(15);
        JTextField yearField = new JTextField(15);
        JTextField priceField = new JTextField(15);
        JTextField genreField = new JTextField(15);

        panel.add(new JLabel("Titlu:"));
        panel.add(titleField);
        panel.add(new JLabel("Autor:"));
        panel.add(authorField);
        panel.add(new JLabel("An:"));
        panel.add(yearField);
        panel.add(new JLabel("Pret:"));
        panel.add(priceField);
        panel.add(new JLabel("Gen:"));
        panel.add(genreField);

        JButton addButton = new JButton("Adaugă");
        panel.add(addButton);

        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            int year = Integer.parseInt(yearField.getText());
            int price = Integer.parseInt(priceField.getText());
            String genre = genreField.getText();

            Book book = new Book(title, author, year, price, genre);
            if (book.addBookToDatabase()) {
                JOptionPane.showMessageDialog(dialog, "Carte adăugată cu succes!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Eroare la adăugarea cărții.", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    // Metoda pentru a deschide un dialog de ștergere a unei cărți
    private void showDeleteBookDialog() {
        JDialog dialog = new JDialog(this, "Șterge Carte", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dialog.add(panel);

        JTextField titleField = new JTextField(15);
        panel.add(new JLabel("Titlu:"));
        panel.add(titleField);

        JButton deleteButton = new JButton("Șterge");
        panel.add(deleteButton);

        deleteButton.addActionListener(e -> {
            String title = titleField.getText();

            Book book = new Book(title, "", 0, 0, "");

            if (book.deleteBookFromDatabase(title)) {
                JOptionPane.showMessageDialog(dialog, "Carte ștearsă cu succes!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Eroare la ștergerea cărții.", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new AdminInterface();
    }

    public void createUI() {
    }
}
