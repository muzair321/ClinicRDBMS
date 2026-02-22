package com.jetbrains.uzair.ui;


import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;

public class AnalyticsPanel {
    public static JPanel mainFrame(){
        JPanel panel = new JPanel();

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));

        JComboBox<String> filter = new JComboBox<>(new String[]{"Today", "Last 7 Days", "Last 30 Days", "All Time"});
        filter.setSelectedItem("Today");

        JPanel scroll = new JPanel();
        scroll.setLayout(new BoxLayout(scroll, BoxLayout.Y_AXIS));
        assert filter.getSelectedItem() != null;
        scroll.add(ChartPanel.mainFrame(filter.getSelectedItem().equals("Today")? 0 : filter.getSelectedItem().equals("Last 7 Days") ? 1 : 2 ));
        filter.addActionListener(e -> {
            Platform.runLater(() -> {
                BarChartFX.update(filter.getSelectedItem().equals("Today")? 0 : filter.getSelectedItem().equals("Last 7 Days") ? 1 : 2 );
            });
        });

//buttons
        JButton btnAdd = new JButton();
        JButton refreshButton = new JButton("Refresh Chart");
        refreshButton.addActionListener(e -> {
            Platform.runLater(() -> {
                BarChartFX.update(filter.getSelectedItem().equals("Today")? 0 : filter.getSelectedItem().equals("Last 7 Days") ? 1 :filter.getSelectedItem().equals("Last 30 Days") ? 2 : 3);
                GenderPieChartFX.update();
                AgePieChartFX.update();
                InventoryBarChartFX.update();
            });
        });

        btnAdd.setMnemonic('A');
        refreshButton.setMnemonic('R');
//toolbar panel
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        toolbar.setBackground(new Color(33, 69, 126));
        toolbar.add(filter);
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
