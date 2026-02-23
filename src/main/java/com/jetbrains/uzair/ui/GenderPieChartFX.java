package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.AnalyticsDB;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;

import java.util.LinkedHashMap;

public class GenderPieChartFX {
    private static ObservableList<PieChart.Data> pieData;
    private static PieChart chart;
    public static JFXPanel start(int filter){
        JFXPanel panel = new JFXPanel();
        LinkedHashMap<String, Integer> chartMap = AnalyticsDB.genderToday(filter);
        pieData = FXCollections.observableArrayList();
        chartMap.forEach((name, value) -> {
            pieData.add(new PieChart.Data(name, value));
        });
        chart = new PieChart(pieData);
        chart.setLabelsVisible(false);
        chart.setAnimated(false);
        chart.setTitle("Gender Distribution");
        for (PieChart.Data data1 : chart.getData()) {
            Tooltip tooltip = new Tooltip(data1.getName() + " : " + (int)(data1.getPieValue()));
            tooltip.setShowDelay(javafx.util.Duration.seconds(0.1));
            // The getNode() method returns the actual graphical bar/node
            Tooltip.install(data1.getNode(), tooltip);
        }
        Scene scene = new Scene(chart, 960, 300);
        String css = BarChartFX.class.getResource("/dashboard.css").toExternalForm();
        scene.getStylesheets().add(css);
        panel.setScene(scene);
        return panel;
    }
    public static void update(int filter) {
        // Get fresh data from DB on the calling thread
        LinkedHashMap<String, Integer> newData = AnalyticsDB.genderToday(filter);


            // 1. Clear existing data points
            pieData.clear();
            // 2. Add new data points (Chart updates automatically)
            newData.forEach((name, value) -> {
                pieData.add(new PieChart.Data(name, value));
            });
            for (PieChart.Data data1 : chart.getData()) {
                Tooltip tooltip = new Tooltip(data1.getName() + " : " + (int)(data1.getPieValue()));
                tooltip.setShowDelay(javafx.util.Duration.seconds(0.1));
                // The getNode() method returns the actual graphical bar/node
                Tooltip.install(data1.getNode(), tooltip);
            }

    }
}
