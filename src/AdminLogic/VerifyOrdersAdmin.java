package AdminLogic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VerifyOrdersAdmin {

    private static final String DB_URL = "jdbc:mysql://localhost/librarie";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public void checkOrders(JFrame parentFrame) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM orders";
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.next()) {
                JOptionPane.showMessageDialog(parentFrame, "Nu există comenzi înregistrate!", "Verifică Comenzi", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JDialog orderDialog = new JDialog(parentFrame, "Comenzile din Sistem", true);
            orderDialog.setSize(800, 400);
            orderDialog.setLayout(new BorderLayout());
            orderDialog.setLocationRelativeTo(parentFrame);

            DefaultTableModel tableModel = new DefaultTableModel(
                    new String[]{"Nume", "Prenume", "Email", "Telefon", "Adresă", "Carte", "Autor"}, 0
            );

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

            orderDialog.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parentFrame, "Eroare la încărcarea comenzilor: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }
}
