package com.jetbrains.uzair.ui;

import javafx.application.Platform;
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

public class BarChartFX extends JPanel {
    private JFXPanel fxPanel;
    private String dbPath;
    private Connection conn;
    private HashMap<String, Integer> hourMap = new HashMap<>();;

    public BarChartFX(String dbPath){
        this.dbPath = dbPath;
        setLayout(new BorderLayout());
        fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateChart();
        Platform.runLater(this::initFX);
    }
    private void initFX(){


        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Hour");
        yAxis.setLabel("Visits");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(true);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (int h = 7; h <= 21; h++) {
            String hour = String.format("%02d", h);
            series.getData().add(new XYChart.Data<>(hour + ":00", hourMap.get(hour)));
        }

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
        if (conn == null) return;

        String query = """
                SELECT strftime('%H', date) AS hour, COUNT(*) AS visits
                FROM visits
                WHERE date(date) = date('now')
                GROUP BY hour;
                """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

// 1️⃣ Pre-fill ALL hours 07–21 with 0
            for (int h = 7; h <= 21; h++) {
                hourMap.put(String.format("%02d", h), 0);
            }

// 2️⃣ Replace values from database
            while (rs.next()) {
                hourMap.put(rs.getString("hour"), rs.getInt("visits"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
