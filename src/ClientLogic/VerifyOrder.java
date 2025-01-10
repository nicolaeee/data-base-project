package ClientLogic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.io.FileOutputStream;

public class VerifyOrder {

    private static final String DB_URL = "jdbc:mysql://localhost/librarie";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public void checkOrders(JFrame parentFrame) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM orders";
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.next()) {
                JOptionPane.showMessageDialog(parentFrame, "Nu ai făcut nicio comandă încă!", "Verifică Comandă", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JDialog orderDialog = new JDialog(parentFrame, "Comenzile Tale", true);
            orderDialog.setSize(600, 400);
            orderDialog.setLayout(new BorderLayout());
            orderDialog.setLocationRelativeTo(parentFrame);

            DefaultTableModel tableModel = new DefaultTableModel(new String[]{ "Nume", "Prenume", "Email", "Telefon", "Adresă", "Carte", "Autor"}, 0);

            do {
                tableModel.addRow(new Object[]{
                        rs.getString("nume"),
                        rs.getString("prenume"),
                        rs.getString("email"),
                        rs.getString("numar"),
                        rs.getString("adresa"),
                        rs.getString("titlulcartii"),
                        rs.getString("autor")
                });
            } while (rs.next());

            JTable orderTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(orderTable);
            orderDialog.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton exportPDFButton = new JButton("Exportă PDF");
            JButton exportXLSButton = new JButton("Exportă XLS");

            exportPDFButton.addActionListener(e -> exportToPDF(orderTable));
            exportXLSButton.addActionListener(e -> exportToXLS(orderTable));

            buttonPanel.add(exportPDFButton);
            buttonPanel.add(exportXLSButton);

            orderDialog.add(buttonPanel, BorderLayout.SOUTH);
            orderDialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame, "Eroare la verificarea comenzilor: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToPDF(JTable table) {
        try {
            // Calea completă către desktop
            String filePath = System.getProperty("user.home") + "\\Desktop\\comanda.pdf";

            // Crează un obiect Document pentru PDF
            Document document = new Document();

            // Creați un writer care va scrie documentul pe fișierul PDF
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Deschide documentul pentru a adăuga conținut
            document.open();

            // Adaugă un titlu
            document.add(new Phrase("Comenzile Tale\n\n"));

            // Adaugă datele din tabel
            for (int row = 0; row < table.getRowCount(); row++) {
                // Adaugă fiecare câmp pe o linie separată cu etichete
                document.add(new Paragraph("Nume: " + table.getValueAt(row, 0).toString()));
                document.add(new Paragraph("Prenume: " + table.getValueAt(row, 1).toString()));
                document.add(new Paragraph("Email: " + table.getValueAt(row, 2).toString()));
                document.add(new Paragraph("Telefon: " + table.getValueAt(row, 3).toString()));
                document.add(new Paragraph("Adresă: " + table.getValueAt(row, 4).toString()));
                document.add(new Paragraph("Carte: " + table.getValueAt(row, 5).toString()));
                document.add(new Paragraph("Autor: " + table.getValueAt(row, 6).toString()));

                // Adăugăm un spațiu între comenzile diferite
                document.add(new Paragraph("\n"));
            }

            // Închide documentul
            document.close();

            // Afișează un mesaj de succes
            JOptionPane.showMessageDialog(null, "Comanda a fost exportată în PDF la " + filePath);
        } catch (DocumentException | java.io.IOException e) {
            // Capturăm erorile și le afișăm în consolă
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Eroare la exportul în PDF: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void exportToXLS(JTable table) {
        try {
            // Calea completă către desktop
            String filePath = System.getProperty("user.home") + "\\Desktop\\comanda.xls";

            // Logica pentru exportul în fișierul XLS
            FileWriter writer = new FileWriter(filePath);

            for (int i = 0; i < table.getColumnCount(); i++) {
                writer.write(table.getColumnName(i) + "\t");
            }
            writer.write("\n");

            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    writer.write(table.getValueAt(i, j).toString() + "\t");
                }
                writer.write("\n");
            }

            writer.close();
            JOptionPane.showMessageDialog(null, "Comanda a fost exportată în XLS la " + filePath);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Eroare la exportul în XLS: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

}
