package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.AnalyticsDB;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class BarChartFX{
    private static BarChart<String, Number> barChart;
    private static XYChart.Series<String, Number> series;
    private static CategoryAxis xAxis;
    public static JFXPanel start(int filter) {
        JFXPanel panel = new JFXPanel();

        // --- Step 1: Define the axes ---
        xAxis = new CategoryAxis();
        xAxis.setLabel(filter == 0 ? "Hours" : filter == 1 || filter == 2 ? "Days" : "Months");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Visits");

        // --- Step 2: Create the BarChart ---
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        barChart.setTitle("Patient Visits " + (filter == 0 ? "(24-hrs)" : filter == 1 ? "(7-days)" : filter == 2 ? "(30-days)" :"(12-months)"));
        // --- Step 3: Create a data series ---
        series = new XYChart.Series<>();
        series.setName("Clinic Daily Visits");
        // Add data
        LinkedHashMap<String, Integer> data = AnalyticsDB.visitsToday(filter);
        ArrayList<String> keys = new ArrayList<>(data.keySet());
        for (String key : keys) {
            series.getData().add(new XYChart.Data<>(key, data.get(key)));
        }

        // --- Step 4: Add series to chart ---
        barChart.getData().add(series);
        for (XYChart.Data<String, Number> data1 : series.getData()) {
            Tooltip tooltip = new Tooltip(data1.getYValue().toString());
            tooltip.setShowDelay(javafx.util.Duration.seconds(0.1));
            // The getNode() method returns the actual graphical bar/node
            Tooltip.install(data1.getNode(), tooltip);
        }
        // --- Step 5: Show chart in a Scene ---
        Scene scene = new Scene(barChart, 1920, 200);
        String css = BarChartFX.class.getResource("/dashboard.css").toExternalForm();
        scene.getStylesheets().add(css);
        panel.setScene(scene);
        return panel;
    }
    public static void update(int filter){
        barChart.setTitle("Patient Visits " + (filter == 0 ? "(24-hrs)" : filter == 1 ? "(7-days)" : filter == 2 ? "(30-days)" :"(12-months)"));
        xAxis.setLabel(filter == 0 ? "Hours" : filter == 1 || filter == 2 ? "Days" : "Months");
        LinkedHashMap<String, Integer> data = AnalyticsDB.visitsToday(filter);
        ArrayList<String> keys = new ArrayList<>(data.keySet());
            series.getData().clear();
            for(String key:  keys){
                series.getData().add(new XYChart.Data<>(key, data.get(key)));
            }
            for (XYChart.Data<String, Number> data1 : series.getData()) {
                Tooltip tooltip = new Tooltip(data1.getYValue().toString());
                tooltip.setShowDelay(javafx.util.Duration.seconds(0.1));
                // The getNode() method returns the actual graphical bar/node
                Tooltip.install(data1.getNode(), tooltip);
            }
    }
}
