package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.PatientDB;

import javax.swing.*;
import java.sql.SQLException;

public class PatientPanel {
    public static void mainWindow(){
        JFrame window = new JFrame("Patient Data");
        window.setSize(200, 400);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        String[] headers = {"ID", "Name", "Age", "Gender"};
        String[][] data;
        try{ data = PatientDB.returnUI();} catch (SQLException e) {
            // add dialog menu
            throw new RuntimeException(e);
        }
        JTable table = new JTable(data, headers);
        JScrollPane scroll = new JScrollPane(table);
        window.add(scroll);
        window.setVisible(true);
    }
}