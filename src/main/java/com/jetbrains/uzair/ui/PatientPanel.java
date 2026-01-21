package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.PatientDB;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientPanel {
    public static void mainWindow(){
        JFrame window = new JFrame("Patient Data");
        window.setSize(400, 800);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        String[] headers = {"ID", "Name", "Age", "Gender"};
        String[][] data;
        try{ data = PatientDB.returnUI();} catch (SQLException e) {
            // todo: add dialog menu
            throw new RuntimeException(e);
        }
        JTable table = new JTable(data, headers);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scroll = new JScrollPane(table);

        JButton btn = new JButton("Delete");
        btn.addActionListener(e -> {

            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(window, "No patients selected");
                return;
            }

            List<Integer> patientIds = new ArrayList<>();

            for (int viewRow : selectedRows) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());
                patientIds.add(id);
            }

            int choice = JOptionPane.showConfirmDialog(
                    window,
                    "Are you sure you want to delete the selected patient(s)?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            try {
                PatientDB.deleteList(patientIds);
                JOptionPane.showMessageDialog(window, "Deleted successfully");
                // todo: refresh table data
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Delete failed");
            }
        });

        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.add(scroll);
        window.add(btn);
        window.setVisible(true);
    }
}