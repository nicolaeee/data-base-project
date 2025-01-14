//package AdminLogic;
//
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.category.DefaultCategoryDataset;
//import org.jfree.data.general.DefaultPieDataset;
//import org.jfree.data.statistics.HistogramDataset;
//
//import javax.swing.*;
//import java.sql.*;
//
//public class Analytics {
//    private final String DB_URL = "jdbc:mysql://localhost/librarie";
//    private final String USERNAME = "root";
//    private final String PASSWORD = "";
//
//    public void showLineChart() {
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
//            String sql = "SELECT titlu, stoc FROM carti";
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String title = resultSet.getString("titlu");
//                int stock = resultSet.getInt("stoc");
//                dataset.addValue(stock, "Stoc", title);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        JFreeChart lineChart = ChartFactory.createLineChart(
//                "Stoc pe fiecare carte",
//                "Carte",
//                "Stoc",
//                dataset
//        );
//
//        showChart(lineChart);
//    }
//
//    public void showBarChart() {
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
//            String sql = "SELECT COUNT(*) AS num_orders FROM comenzi WHERE status = 'în așteptare'";
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                int pendingOrders = resultSet.getInt("num_orders");
//                dataset.addValue(pendingOrders, "Comenzi", "În așteptare");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        JFreeChart barChart = ChartFactory.createBarChart(
//                "Numărul de comenzi în așteptare",
//                "Status",
//                "Număr",
//                dataset
//        );
//
//        showChart(barChart);
//    }
//
//    public void showPieChart() {
//        DefaultPieDataset dataset = new DefaultPieDataset();
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
//            String sql = "SELECT COUNT(*) AS num_clients FROM clienti";
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                int clients = resultSet.getInt("num_clients");
//                dataset.setValue("Clienți", clients);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        JFreeChart pieChart = ChartFactory.createPieChart(
//                "Numărul de clienți",
//                dataset,
//                true,
//                true,
//                false
//        );
//
//        showChart(pieChart);
//    }
//
//    public void showHistogram() {
//        HistogramDataset dataset = new HistogramDataset();
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
//            String sql = "SELECT SUM(pret * cantitate) AS venit FROM comenzi WHERE MONTH(data) = 1";
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                double income = resultSet.getDouble("venit");
//                dataset.addSeries("Venit Ianuarie", new double[]{income}, 10);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        JFreeChart histogram = ChartFactory.createHistogram(
//                "Venitul din luna Ianuarie",
//                "Venit",
//                "Frecvență",
//                dataset
//        );
//
//        showChart(histogram);
//    }
//
//    private void showChart(JFreeChart chart) {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setSize(800, 600);
//        frame.setLocationRelativeTo(null);
//
//        ChartPanel chartPanel = new ChartPanel(chart);
//        frame.add(chartPanel);
//
//        frame.setVisible(true);
//    }
//}
