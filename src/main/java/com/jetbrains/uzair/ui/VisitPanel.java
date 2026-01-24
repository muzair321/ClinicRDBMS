package com.jetbrains.uzair.ui;

import javax.swing.*;

public class VisitPanel {
    public static void mainWindow(){
        JFrame window = new JFrame("Patient Data");
        window.setSize(1200, 900);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        JTable table = commonUI.getDBData(window, 2);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        window.add(scroll);
        window.setVisible(true);
    }
}