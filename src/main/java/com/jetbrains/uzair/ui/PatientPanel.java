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
    public static JPanel mainWindow(JFrame window){
        JPanel panel = new JPanel();

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        //search
        PlaceholderTextField searchField = new PlaceholderTextField("Search Patient");

        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(255, 255, 255));
        searchField.setOpaque(true);
//table
        JTable table = commonUI.getDBData(window, 1);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
//buttons
        JButton btnAdd = addButton(table, window);
        JButton btnEdit = editButton1(table, window);
        JButton btnView = viewPatient(table, window);
        JButton btnVisit = addVisit(table, window);
        JButton btnDel = commonUI.deleteButton(table, window, 1);
        JButton btnRef = new JButton("Refresh");
        btnRef.addActionListener(e -> commonUI.refresh(table, 1));
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> commonUI.search(table, searchField.getText(), 1));
        searchField.addActionListener(_ -> btnSearch.doClick());
        commonUI.buttonHighlight(btnEdit, btnVisit, btnView, btnDel, table);

        btnAdd.setMnemonic('A');
        btnVisit.setMnemonic('S');
        btnRef.setMnemonic('R');
        btnDel.setMnemonic('D');
//toolbar panel
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        toolbar.add(btnAdd);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnEdit);
        toolbar.add(btnVisit);
        toolbar.add(btnView);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnDel);
        toolbar.add(btnRef);
        toolbar.add(searchField);
        toolbar.add(btnSearch);
//main layout
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        panel.setVisible(true);
        return panel;
    }
    //add new record forum
    private static JButton addButton(JTable table, JFrame window){
        JButton btnAdd = new JButton("Add New");
        btnAdd.addActionListener(e -> {
            try{
                commonForum(table, -1, window);
            }catch (SQLException sqle){
                JOptionPane.showMessageDialog(null, "Error Adding Patient: " + sqle.getMessage());
            }
        });
        return btnAdd;
    }
    private static JButton addVisit(JTable table, JFrame window) {
        JButton btn = new JButton("Add Visit");

        btn.addActionListener(e -> {
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
                VisitPanel.commonForum(window, id, -1, table);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, ex.getMessage());
            }
        });

        return btn;
    }
    //view selected patient
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
            commonUI.patientDetails(table, window, id);
        });
        return btnVew;
    }
    //edit button for patient info frame
    public static JButton editButton2(JTable table, JFrame window, int id) {
        JButton btn = new JButton("Edit Patient");
        btn.addActionListener(e -> {
            try {
                commonForum(table, id, window);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Failed to load patient data");
                return;
            }
        });
        return btn;
    }
    //  edit button for main frame
    private static JButton editButton1(JTable table, JFrame window) {
        JButton btn = new JButton("Edit Patient");

        btn.addActionListener(e -> {
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

            try {commonForum(table, id, window);} catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Failed to load patient data");
                return;
            }
        });

        return btn;
    }
    // common forum for both add and edit
    private static JDialog commonForum(JTable table, int id, JFrame window)  throws SQLException{
        Patient p;
        String t = "Error";
        String nameS;
        String ageF;
        String genderS;
        String phoneS;

        if(id != -1) {
            try {
                SoundPlayer.play("popup.wav");
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
            t = "Edit Patient";
            p = PatientDB.returnUISingle(id);
            nameS = p.getName();
            ageF = String.valueOf(p.getAge());
            genderS = p.getGender();
            phoneS = p.getPhone();
        }else{
            t = "Add Patient";
            p = new Patient();
            p.setId(-1);
            nameS = null;
            genderS = "Other";
            ageF = null;
            phoneS = null;
        }
        JDialog forum = new JDialog(window, t, true);
        forum.setSize(500, 600);
        forum.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        forum.setLocationRelativeTo(null);
        forum.setLayout(new BorderLayout());
        //header
        JPanel header = commonUI.commonHeader(t);
//Form panel
        JPanel formPanel = commonUI.commonForum();

        int row = 0;
        GridBagConstraints gbc = commonUI.commonFormGrid(formPanel, row);

        JTextField name = new JTextField(nameS, 15);
        formPanel.add(name, gbc);
        row++;
//age
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        JTextField age = new JTextField(ageF, 15);
        formPanel.add(age, gbc);
        row++;
//gender
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Gender:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> gender =
                new JComboBox<>(new String[]{"Male", "Female", "Other"});
        gender.setSelectedItem(genderS);
        formPanel.add(gender, gbc);
        row++;
//phone
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        JTextField phone = new JTextField(phoneS, 15);
        formPanel.add(phone, gbc);
//button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        btnSave.addActionListener(ev -> {
            try {
                String[] raw = {
                        String.valueOf(p.getId()),
                        name.getText(),
                        age.getText(),
                        gender.getSelectedItem().toString(),
                        phone.getText()
                };

                if(id != -1) {
                    Patient newP = Patient.check(Patient.convArrayToOb(raw), false);
                    PatientDB.edit(newP);
                }else{
                    Patient newP = Patient.check(Patient.convArrayToOb(raw), true);
                    VisitPanel.commonForum(window, PatientDB.insert(newP), -1, null);
                }
                commonUI.refresh(table, 1);
                forum.dispose();

            } catch (ValidationException ve) {
                JOptionPane.showMessageDialog(forum, ve.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(forum, "Modification Failed:" + ex.getMessage());
            }
        });
        btnCancel.addActionListener(ev -> forum.dispose());
        forum.add(header, BorderLayout.NORTH);
        forum.add(formPanel, BorderLayout.CENTER);
        forum.add(buttonPanel, BorderLayout.SOUTH);
        forum.setVisible(true);
        return forum;
    }
}