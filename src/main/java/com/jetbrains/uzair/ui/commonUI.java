package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.db.VisitDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;

public class commonUI {
    public static JTable getDBData(JFrame window, int ui){
        var headers = getStrings(ui);
        String[] header = headers.toArray(new String[0]);
        String[][] data;
        try {
            if(ui == 1) {
                data = PatientDB.returnUI();
            } else if (ui == 2) {
                data = VisitDB.returnUI();
            }else{
                throw new SQLException("Error Reading");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
            window.dispose();
            throw new RuntimeException("Error While Reading Data");
        }
        DefaultTableModel model = new DefaultTableModel(data, header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // prevent accidental edits
            }
        };
        return new JTable(model);
    }

    private static ArrayList<String> getStrings(int ui) {
        var headers = new ArrayList<String>();
        if(ui == 1) {
            headers.add("ID");
            headers.add("Name");
            headers.add("Age");
            headers.add("Gender");
            headers.add("Phone Number");
            headers.add("Created At");
        }
        else if(ui == 2){
            headers.add("Visit ID");
            headers.add("Patient ID");
            headers.add("Patient Name");
            headers.add("Treatment");
            headers.add("Payment");
            headers.add("Date Of Visit");
        }else{
            headers.add("Error");
        }
        return headers;
    }

    public static void search(JTable table, String search, int ui) {
        SwingWorker<String[][], Void> worker = new SwingWorker<>() {
            @Override
            protected String[][] doInBackground() throws SQLException {
                if(ui == 1) {
                    return PatientDB.returnUI(search);
                } else if (ui == 2) {
                    return VisitDB.returnUI(search);
                } else {
                    return null;
                }
            }
            @Override
            protected void done() {
                try {
                    String[][] data = get();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0);
                    for (String[] row : data) {
                        model.addRow(row);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(table, e.getMessage() + "Failed To Search");
                }
            }
        };
        worker.execute();
    }
    public static void refresh(JTable table, int ui) {
        SwingWorker<String[][], Void> worker = new SwingWorker<>() {
            @Override
            protected String[][] doInBackground() throws SQLException {
                if(ui == 1) {
                    return PatientDB.returnUI();
                } else if (ui == 2) {
                    return VisitDB.returnUI();
                }
                return null; // Database call
            }
            @Override
            protected void done() {
                try {
                    String[][] data = get();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0);
                    for (String[] row : data) {
                        model.addRow(row);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(table, "Failed To Refresh Data");
                }
            }
        };
        worker.execute();
    }
}