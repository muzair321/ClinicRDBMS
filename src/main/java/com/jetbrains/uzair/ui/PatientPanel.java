package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.model.ValidationException;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
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
        DefaultTableModel model = new DefaultTableModel(data, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // prevent accidental edits
            }
        };

        JTable table = new JTable(model);

        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scroll = new JScrollPane(table);

        JButton btnDel = deleteButton(table, window);
        JButton btnAdd = addButton(table);

        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.add(scroll);
        window.add(btnDel);
        window.add(btnAdd);
        window.setVisible(true);
    }
    public static void refresh(JTable table) {
        try {
            String[][] data = PatientDB.returnUI();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            for (String[] row : data) {
                model.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(table, "Failed to refresh data");
        }
    }
    public static JButton deleteButton(JTable table, JFrame window){
        JButton btnDel = new JButton("Delete");
        btnDel.addActionListener(e -> {

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
                refresh(table);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Delete failed");
            }
        });
        return btnDel;
    }
    public static JButton addButton(JTable table){
        JButton btnAdd = new JButton("Add New");
        btnAdd.addActionListener(e -> {
            JFrame forum = new JFrame("Add New Patient");
            forum.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            forum.setSize(200,400);
            JLabel t1 = new JLabel("Name: ");
            JTextField name = new JTextField();
            JLabel t2 = new JLabel("Age: ");
            JTextField age = new JTextField();
            JLabel t3 = new JLabel("Gender: ");
            JTextField gender = new JTextField();

            JButton btnCon = new JButton("Confirm");
            JButton btnCan = new JButton("Cancel");
            btnCon.addActionListener(e1 -> {
                String[] raw = {"-1", name.getText(), age.getText(), gender.getText()};
                try {
                    PatientDB.insert(Patient.check(Patient.convArrayToOb(raw)));
                    refresh(table);
                    forum.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(forum, ex.getMessage());
                }catch (ValidationException ve){
                    JOptionPane.showMessageDialog(forum, ve.getMessage());
                }
            });
            btnCan.addActionListener(e1 -> {
                forum.dispose();
            });
            forum.setLayout(new BoxLayout(forum.getContentPane(), BoxLayout.Y_AXIS));
            forum.add(t1);
            forum.add(name);
            forum.add(t2);
            forum.add(age);
            forum.add(t3);
            forum.add(gender);
            forum.add(btnCon);
            forum.add(btnCan);
            forum.setVisible(true);
        });
        return btnAdd;
    }
}