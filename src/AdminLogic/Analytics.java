package AdminLogic;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Analytics {

    private final String DB_URL = "jdbc:mysql://localhost/librarie";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    // Diagrama pentru comenzi pe zi (astăzi)
    public JPanel getOrdersForTodayChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = today.format(formatter);

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT COUNT(*) AS total_comenzi FROM orders WHERE DATE(data) = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, todayDate);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int orders = resultSet.getInt("total_comenzi");
                dataset.addValue(orders, "Comenzi", "Astăzi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Comenzi pentru Astăzi",
                "Ziua",
                "Comenzi",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Creare ChartPanel cu MouseListener
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openDetailedView("Comenzi pentru Astăzi", "Comenzi pentru astăzi detaliate");
            }
        });

        return chartPanel;
    }



    // Diagrama pentru stocul curent per fiecare carte
    // Diagrama pentru stocul curent per fiecare carte
    public JPanel getCurrentStockChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT titlu, stoc FROM carti";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("titlu");
                int stock = resultSet.getInt("stoc");
                dataset.addValue(stock, "Stoc", bookTitle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Stocul Curent per Carte",
                "Carte",
                "Stoc",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        return new ChartPanel(chart);
    }

    // Diagrama pentru comenzi per client
    public JPanel getOrdersByClientChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT CONCAT(nume, ' ', prenume) AS client, COUNT(*) AS total_comenzi FROM orders GROUP BY client";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String client = resultSet.getString("client");
                int orders = resultSet.getInt("total_comenzi");
                dataset.addValue(orders, "Comenzi", client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Comenzi per Client",
                "Client",
                "Comenzi",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Creare ChartPanel cu MouseListener
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openDetailedView("Comenzi per Client", "Comenzi detaliate per client");
            }
        });

        return chartPanel;
    }

    // Metoda pentru a deschide o fereastră detaliată
    private void openDetailedView(String title, String content) {
        JFrame detailedViewFrame = new JFrame(title);
        detailedViewFrame.setSize(400, 300);
        detailedViewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailedViewFrame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        detailedViewFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        detailedViewFrame.setVisible(true);
    }
}
