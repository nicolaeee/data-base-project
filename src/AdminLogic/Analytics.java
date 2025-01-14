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

        return new ChartPanel(chart);
    }

    // Diagrama pentru comenzi pe săptămână
    public JPanel getOrdersForWeekChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1); // Luni
        LocalDate endOfWeek = startOfWeek.plusDays(6); // Duminică
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT COUNT(*) AS total_comenzi FROM orders WHERE DATE(data) BETWEEN ? AND ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, startOfWeek.format(formatter));
            preparedStatement.setString(2, endOfWeek.format(formatter));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int orders = resultSet.getInt("total_comenzi");
                dataset.addValue(orders, "Comenzi", "Această Săptămână");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Comenzi pentru Această Săptămână",
                "Săptămâna",
                "Comenzi",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        return new ChartPanel(chart);
    }

    // Diagrama pentru stocul curent per fiecare carte
    public JPanel getCurrentStockChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT titlulcartii, COUNT(*) AS total_stock FROM orders GROUP BY titlulcartii";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("titlulcartii");
                int stock = resultSet.getInt("total_stock");
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

        return new ChartPanel(chart);
    }
}
