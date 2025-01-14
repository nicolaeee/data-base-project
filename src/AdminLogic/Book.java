package AdminLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Book {
    private String titlu;
    private String autor;
    private int an;
    private double pret; // Schimbare: prețul este acum de tip `double`
    private String gen;

    // Constructor principal
    public Book(String titlu, String autor, int an, double pret, String gen) {
        this.titlu = titlu;
        this.autor = autor;
        this.an = an;
        this.pret = pret;
        this.gen = gen;
    }

    // Constructor simplificat pentru utilizare în coș
    public Book(int id, String titlu, String autor, double pret) {
        this.titlu = titlu;
        this.autor = autor;
        this.pret = pret;
    }

    // Getteri și Setteri pentru toate câmpurile
    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAn() {
        return an;
    }

    public void setAn(int an) {
        this.an = an;
    }

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    // Metodă pentru a adăuga o carte în baza de date
    public boolean addBookToDatabase() {
        final String DB_URL = "jdbc:mysql://localhost/librarie";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO carti (titlu, autor, an, pret, gen) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, titlu);
            preparedStatement.setString(2, autor);
            preparedStatement.setInt(3, an);
            preparedStatement.setDouble(4, pret); // Utilizare tip double pentru preț
            preparedStatement.setString(5, gen);

            int addedRows = preparedStatement.executeUpdate();
            return addedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metodă pentru a șterge o carte din baza de date după titlu
    public boolean deleteBookFromDatabase(String titlu) {
        final String DB_URL = "jdbc:mysql://localhost/librarie";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM carti WHERE titlu = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, titlu);

            int deletedRows = preparedStatement.executeUpdate();
            return deletedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
