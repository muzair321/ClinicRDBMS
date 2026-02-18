package com.jetbrains.uzair.ui;


import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;

public class AnalyticsPanel {
    public static JPanel mainFrame(){
        JPanel panel = new JPanel();

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));

        JPanel scroll = new JPanel();
        scroll.setLayout(new BoxLayout(scroll, BoxLayout.Y_AXIS));
        scroll.add(ChartPanel.mainFrame());

//buttons
        JButton btnAdd = new JButton();
        JButton refreshButton = new JButton("Refresh Chart");
        refreshButton.addActionListener(e -> {
            Platform.runLater(() -> {
                BarChartFX.update();
                GenderPieChartFX.update();
                AgePieChartFX.update();
            });
        });

        btnAdd.setMnemonic('A');
        refreshButton.setMnemonic('R');
//toolbar panel
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        toolbar.setBackground(new Color(33, 69, 126));
        toolbar.add(btnAdd);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(refreshButton);
//main layout
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        panel.setVisible(true);
        return panel;
    }
}
