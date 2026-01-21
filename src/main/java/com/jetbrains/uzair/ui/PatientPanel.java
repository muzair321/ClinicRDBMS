package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.model.ValidationException;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientPanel {
    public static void mainWindow(){
        JFrame window = new JFrame("Patient Data");
        window.setSize(900, 700);
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
        JButton btnRef = new JButton("Refresh");
        btnRef.addActionListener(e -> {
            refresh(table);
        });
        JButton btnVew = viewPatient(table, window);

        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.add(scroll);
        window.add(btnDel);
        window.add(btnAdd);
        window.add(btnRef);
        window.add(btnVew);
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
            JOptionPane.showMessageDialog(table, "Failed To Refresh Data");
        }
    }
    public static JButton deleteButton(JTable table, JFrame window){
        JButton btnDel = new JButton("Delete");
        btnDel.addActionListener(e -> {

            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(window, "No Patient(s) Selected");
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
                    "Are You Sure You Want To Delete The Selected Patient(s)?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            try {
                PatientDB.deleteList(patientIds);
                JOptionPane.showMessageDialog(window, "Deleted Successfully");
                refresh(table);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Delete Failed");
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
    private static JTextField createReadOnlyField(String text, Font font) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        field.setBackground(UIManager.getColor("Panel.background"));
        field.setFont(font);
        field.setColumns(15);
        return field;
    }
    private static JButton viewPatient(JTable table, JFrame window){
        JButton btnVew = new JButton("View Patient Data");
        btnVew.addActionListener(e -> {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(window, "No Patient(s) Selected");
                return;
            }
            if (selectedRows.length > 1){
                JOptionPane.showMessageDialog(window, "Select Only 1 Patient");
                return;
            }

            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());
            try {
                Patient p = PatientDB.returnUISingle(id);
                JFrame disp = new JFrame("Patient Details");
                disp.setSize(400, 300);
                disp.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                disp.setLocationRelativeTo(null); // center on screen

                JPanel panel = new JPanel();
                panel.setLayout(new GridBagLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.anchor = GridBagConstraints.WEST;

                Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
                Font valueFont = new Font("Segoe UI", Font.PLAIN, 13);

                int row = 0;
                gbc.gridx = 0; gbc.gridy = row;
                panel.add(new JLabel("ID:"), gbc);

                gbc.gridx = 1;
                panel.add(createReadOnlyField(String.valueOf(p.getId()), valueFont), gbc);
                row++;

                gbc.gridx = 0; gbc.gridy = row;
                panel.add(new JLabel("Name:"), gbc);

                gbc.gridx = 1;
                panel.add(createReadOnlyField(p.getName(), valueFont), gbc);
                row++;

                gbc.gridx = 0; gbc.gridy = row;
                panel.add(new JLabel("Age:"), gbc);

                gbc.gridx = 1;
                panel.add(createReadOnlyField(String.valueOf(p.getAge()), valueFont), gbc);
                row++;

                gbc.gridx = 0; gbc.gridy = row;
                panel.add(new JLabel("Gender:"), gbc);

                gbc.gridx = 1;
                panel.add(createReadOnlyField(p.getGender(), valueFont), gbc);

                disp.add(panel);
                disp.setVisible(true);
            }catch (SQLException sqle){
                JOptionPane.showMessageDialog(window,  "Error Occured: " + sqle.getMessage());
                return;
            }
        });
        return btnVew;
    }
}