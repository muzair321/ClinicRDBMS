package com.jetbrains.uzair.ui;

import javax.swing.*;
import java.awt.*;

public class InventoryLogsPanel {
    public static JPanel mainWindow(JFrame window){
        JPanel panel = new JPanel();

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        //search
        PlaceholderTextField searchField = new PlaceholderTextField("Search Inventory Logs");

        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(255, 255, 255));
        searchField.setOpaque(true);
//table
        JTable table = commonUI.getDBData(window, 4);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
//buttons
        JButton btnRef = new JButton("Refresh");
        btnRef.addActionListener(_ -> commonUI.refresh(table, 4));
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(_ -> commonUI.search(table, searchField.getText(), 4));
//toolbar panel
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        toolbar.add(btnRef);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(searchField);
        toolbar.add(btnSearch);
//main layout
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.setVisible(true);
        return panel;
    }
}
