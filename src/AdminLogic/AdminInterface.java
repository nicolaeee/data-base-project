package AdminLogic;

import javax.swing.*;
import java.sql.*;

public class AdminInterface extends JFrame {
    private final String DB_URL = "jdbc:mysql://localhost/librarie";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    public AdminInterface() {
        setTitle("Library Management System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        setContentPane(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Butoane pentru interfață
        JButton addClientButton = new JButton("Adaugă Client");
        JButton viewClientsButton = new JButton("Vizualizează Clienți");
        JButton addBookButton = new JButton("Adaugă Carte");
        JButton deleteBookButton = new JButton("Șterge Carte");
        JButton filterBooksButton = new JButton("Filtrează Cărți după Autor");


        mainPanel.add(addClientButton);
        mainPanel.add(viewClientsButton);
        mainPanel.add(addBookButton);
        mainPanel.add(deleteBookButton);
        mainPanel.add(filterBooksButton);

        // Acțiune pentru butonul "Adaugă Client"
        addClientButton.addActionListener(e -> showAddClientDialog());

        // Acțiune pentru butonul "Vizualizează Clienți"
        viewClientsButton.addActionListener(e -> displayClients());

        // Acțiune pentru butonul "Adaugă Carte"
        addBookButton.addActionListener(e -> showAddBookDialog());

        // Acțiune pentru butonul "Șterge Carte"
        deleteBookButton.addActionListener(e -> showDeleteBookDialog());

        filterBooksButton.addActionListener(e -> showFilterBooksDialog());


        setVisible(true);
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
        JTextField genreField = new JTextField(15);

        panel.add(new JLabel("Titlu:"));
        panel.add(titleField);
        panel.add(new JLabel("Autor:"));
        panel.add(authorField);
        panel.add(new JLabel("An:"));
        panel.add(yearField);
        panel.add(new JLabel("Gen:"));
        panel.add(genreField);

        JButton addButton = new JButton("Adaugă");
        panel.add(addButton);

        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            int year = Integer.parseInt(yearField.getText());
            String genre = genreField.getText();

            Book book = new Book(title, author, year, genre);
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

            Book book = new Book(title, "", 0, "");
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
}

