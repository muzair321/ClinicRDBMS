package com.jetbrains.uzair.ui;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class ModernFXChart extends Application {

    private XYChart.Series<Number, Number> series;
    private int xValue = 1;
    private Random random = new Random();

    @Override
    public void start(Stage stage) {

        stage.setTitle("Clinic Analytics - Animated");

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Month");
        yAxis.setLabel("Visits");

        LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setAnimated(true);
        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(true);
        lineChart.setTitle("Live Clinic Visits");

        series = new XYChart.Series<>();
        lineChart.getData().add(series);

        // Timeline for animation
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.4), event -> addData())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        StackPane root = new StackPane(lineChart);
        Scene scene = new Scene(root, 900, 600);

//        // Optional modern styling
//        scene.getStylesheets().add(getClass().getResource("style.css") == null ? "" : getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    private void addData() {
        if (xValue > 12) return;

        int yValue = 20 + random.nextInt(50);
        series.getData().add(new XYChart.Data<>(xValue, yValue));
        xValue++;
    }

    public static void main(String[] args) {
        launch(args);
    }
}