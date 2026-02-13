package com.jetbrains.uzair.ui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

import javax.swing.*;
import java.awt.*;

public class AnalyticsChartPanel extends JPanel {

    private JFXPanel fxPanel;
    private XYChart.Series<Number, Number> series;

    public AnalyticsChartPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        fxPanel = new JFXPanel(); // Bridge
        add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(this::initFX); // Initialize JavaFX safely
    }

    private void initFX() {

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Month");
        yAxis.setLabel("Visits");

        LineChart<Number, Number> chart =
                new LineChart<>(xAxis, yAxis);

        chart.setAnimated(true);
        chart.setLegendVisible(false);
        chart.setTitle("Clinic Visits");


        series = new XYChart.Series<>();
        chart.getData().add(series);

        StackPane root = new StackPane(chart);
        Scene scene = new Scene(root);

        fxPanel.setScene(scene);

        // Sample data
        addData(1, 15);
        addData(2, 30);
        addData(3, 22);
    }

    public void addData(int x, int y) {
        Platform.runLater(() ->
                series.getData().add(new XYChart.Data<>(x, y))
        );
    }
}
