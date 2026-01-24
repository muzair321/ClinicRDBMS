package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.db.VisitDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;

public class commonUI {
    public static JTable getDBData(JFrame window, int ui){
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
}