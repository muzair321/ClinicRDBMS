package com.jetbrains.uzair.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BarChartFX extends JPanel {
    private JFXPanel fxPanel;
    private Connection conn;
    private XYChart.Series<String, Number> series;

    public BarChartFX(String dbPath){
        setLayout(new BorderLayout());
        fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Platform.runLater(this::initFX);
    }
    private void initFX(){


        CategoryAxis xAxis = new CategoryAxis();
        ObservableList<String> hours = FXCollections.observableArrayList();

        for (int h = 7; h <= 21; h++) {
            hours.add(String.format("%02d:00", h));
        }

        xAxis.setCategories(hours);
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Hour");
        yAxis.setLabel("Visits");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(true);

        series = new XYChart.Series<>();

        barChart.getData().add(series);

        StackPane root = new StackPane(barChart);
        Scene scene = new Scene(root, 900, 500);

        // Load CSS
        String css = getClass().getResource("/dashboard.css").toExternalForm();
        scene.getStylesheets().add(css);

        fxPanel.setScene(scene);

        updateChart();
    }
    private void updateChart() {

        Platform.runLater(() -> {

            series.getData().clear();

            Map<String, Integer> hourMap = new HashMap<>();

            for (int h = 7; h <= 21; h++) {
                hourMap.put(String.format("%02d", h), 0);
            }
            try {
                String sql = """
                SELECT strftime('%H', date) AS hour, COUNT(*) AS visits
                FROM visits
                WHERE date(date) = date('now')
                GROUP BY hour
            """;
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    hourMap.put(rs.getString("hour"), rs.getInt("visits"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int h = 7; h <= 21; h++) {
                String hour = String.format("%02d", h);
                series.getData().add(new XYChart.Data<>(hour + ":00", hourMap.get(hour)));
            }
        });
    }
}
