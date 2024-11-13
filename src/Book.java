import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Book {
    private String titlu;
    private String autor;
    private int an;
    private String gen;

    public Book(String titlu, String autor, int an, String gen) {
        this.titlu = titlu;
        this.autor = autor;
        this.an = an;
        this.gen = gen;
    }

    public boolean addBookToDatabase() {
        final String DB_URL = "jdbc:mysql://localhost/librarie";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO carti (titlu, autor, an, gen) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, titlu);
            preparedStatement.setString(2, autor);
            preparedStatement.setInt(3, an);
            preparedStatement.setString(4, gen);

            int addedRows = preparedStatement.executeUpdate();
            return addedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Getter și Setter dacă sunt necesari
}
