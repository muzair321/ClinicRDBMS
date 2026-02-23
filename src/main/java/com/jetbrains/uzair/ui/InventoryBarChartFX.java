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

import javax.swing.*;
import java.util.*;

public class InventoryBarChartFX{
    private static XYChart.Series<String, Number> series;
    public static JFXPanel start(int filter) {
        JFXPanel panel = new JFXPanel();
        // --- Step 1: Define the axes ---
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Items");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Item Used");

        // --- Step 2: Create the BarChart ---
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        barChart.setTitle("Items Most Used");
        // --- Step 3: Create a data series ---
        series = new XYChart.Series<>();
        series.setName("Clinic Item Usage");
        // Add data
        LinkedHashMap<String, Integer> data = AnalyticsDB.itemsToday(filter);
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
        LinkedHashMap<String, Integer> data = AnalyticsDB.itemsToday(filter);
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
