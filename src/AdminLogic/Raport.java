package AdminLogic;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.io.FileOutputStream;
import java.sql.*;

public class Raport {

    private static final String DB_URL = "jdbc:mysql://localhost/librarie";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private void openGenerateReport() {
        Raport raport = new Raport();
        raport.generateReport();  // Apelează metoda pentru generarea raportului
    }


    public void generateReport() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM orders ORDER BY id DESC LIMIT 10";  // Adaptează interogarea pentru ultimele 10 comenzi
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Calea completă către desktop pentru salvarea raportului PDF
            String filePath = System.getProperty("user.home") + "\\Desktop\\raport_comenzi.pdf";

            // Creăm obiectul Document pentru PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Titlu raport
            document.add(new Paragraph("Raport Ultimele Comenzi\n\n"));

            // Adăugăm antetul tabelului
            document.add(new Paragraph("ID | Nume | Prenume | Email | Numar | Adresa | Titlu Carte | Autor"));

            // Adăugăm fiecare comandă în PDF
            while (rs.next()) {
                document.add(new Paragraph(rs.getInt("id") + " | " +
                        rs.getString("nume") + " | " +
                        rs.getString("prenume") + " | " +
                        rs.getString("email") + " | " +
                        rs.getString("numar") + " | " +
                        rs.getString("adresa") + " | " +
                        rs.getString("titlulcartii") + " | " +
                        rs.getString("autor")));
            }

            // Închide documentul
            document.close();

            // Afișăm un mesaj de succes
            JOptionPane.showMessageDialog(null, "Raportul a fost generat și salvat pe Desktop la " + filePath);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Eroare la generarea raportului: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
