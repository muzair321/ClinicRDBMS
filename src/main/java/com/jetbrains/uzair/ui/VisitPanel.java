package com.jetbrains.uzair.ui;

import javax.swing.*;
import java.awt.*;

public class VisitPanel {
    public static void mainWindow(){
        JFrame window = new JFrame("Patient Data");
        window.setSize(1200, 900);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        //search
        PlaceholderTextField searchField = new PlaceholderTextField("Search Visit");

        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(255, 255, 255));
        searchField.setOpaque(true);
        //table
        JTable table = commonUI.getDBData(window, 2);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        //buttons
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(_ -> commonUI.search(table, searchField.getText(), 2));
        //toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        toolbar.add(searchField);
        toolbar.add(btnSearch);
        //main layout
        window.setLayout(new BorderLayout());
        window.add(toolbar, BorderLayout.NORTH);
        window.add(scroll, BorderLayout.CENTER);

        window.setVisible(true);
    }
}