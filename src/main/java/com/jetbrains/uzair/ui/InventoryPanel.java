package com.jetbrains.uzair.ui;

import javax.swing.*;
import java.awt.*;

public class InventoryPanel {
    public static JPanel mainWindow(JFrame window){
        JPanel panel = new JPanel();

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        //search
        PlaceholderTextField searchField = new PlaceholderTextField("Search Inventory");

        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(255, 255, 255));
        searchField.setOpaque(true);
//table
        JTable table = commonUI.getDBData(window, 1);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
//buttons
        JButton btnAdd =
        JButton btnEdit =
        JButton btnView =
        JButton btnDel =
        JButton btnRef = new JButton("Refresh");
        btnRef.addActionListener(e -> );
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> );
        commonUI.buttonHighlight();

//toolbar panel
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

//main layout
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        panel.setVisible(true);
        return panel;
    }
}
