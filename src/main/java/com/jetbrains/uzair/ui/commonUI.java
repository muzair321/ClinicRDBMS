package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.db.VisitDB;
import com.jetbrains.uzair.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    public static JTextField createReadOnlyField(String text, Font font) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        field.setBackground(UIManager.getColor("Panel.background"));
        field.setFont(font);
        field.setColumns(15);
        return field;
    }
    public static void patientDetails(JTable table, JFrame window, int id){
        try {
            Patient p = PatientDB.returnUISingle(id);
            JDialog disp = new JDialog(window, "Patient Details", true);
            disp.setSize(500, 700);
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
            panel.add(commonUI.createReadOnlyField(String.valueOf(p.getId()), valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Name:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(p.getName(), valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Age:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(String.valueOf(p.getAge()), valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Gender:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(p.getGender(), valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Phone Number:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(p.getPhone(), valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Date Of Creation:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(p.getCreatedAt(), valueFont), gbc);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
            buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

            JButton btnEdit = PatientPanel.editButton2(table, window, id);
            JButton btnClose = new JButton("Close");
            btnClose.addActionListener(e1 -> {disp.dispose();});
            JButton btnDel = new JButton("Delete");
            btnDel.addActionListener(e1 -> {
                try {
                    SoundPlayer.play("popup.wav");
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(window, ex.getMessage());
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
                try{
                    PatientDB.delete(id);
                    JOptionPane.showMessageDialog(window, "Patient Deleted Successfully");
                    PatientPanel.refresh(table);
                    disp.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(disp, ex.getMessage());
                    return;
                }});
            JButton btnAddVisit = new JButton("Add Visit");
            btnAddVisit.addActionListener(e1 -> {
                try {
                    VisitPanel.commonForum(window, id, -1);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(window, ex.getMessage());
                }
            });

            buttonPanel.add(btnAddVisit);
            buttonPanel.add(btnEdit);
            buttonPanel.add(btnDel);
            buttonPanel.add(btnClose);

            //header
            JPanel header = new JPanel();
            header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            header.setBackground(new Color(12, 38, 78));

            JLabel head = new JLabel("Patient Data");
            head.setForeground(Color.WHITE);
            head.setFont(new Font("Segoe UI", Font.BOLD, 30));
            header.add(head);

            //panels
            disp.setLayout(new BorderLayout());
            disp.add(header, BorderLayout.NORTH);
            disp.add(panel, BorderLayout.CENTER);
            disp.add(buttonPanel, BorderLayout.SOUTH);
            disp.setVisible(true);
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(window,  "Error Occured: " + sqle.getMessage());
            return;
        }
    }
}