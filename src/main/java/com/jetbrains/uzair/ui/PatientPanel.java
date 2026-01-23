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
        window.setSize(1200, 900);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        //search
        PlaceholderTextField searchField = new PlaceholderTextField("Search Patient");

        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(255, 255, 255));
        searchField.setOpaque(true);
//table
        JTable table = getDBData(window, searchField.getText());
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);

//buttons
        JButton btnAdd = addButton(table);
        JButton btnEdit = editButton1(table, window);
        JButton btnView = viewPatient(table, window);
        JButton btnDel = deleteButton(table, window);
        JButton btnRef = new JButton("Refresh");
        btnRef.addActionListener(e -> refresh(table));
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> search(table, searchField.getText()));
        btnEdit.setEnabled(false);
        btnView.setEnabled(false);
        btnDel.setEnabled(false);

        table.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table.getSelectedRowCount() > 0;
            btnView.setEnabled(table.getSelectedRowCount() == 1);
            btnEdit.setEnabled(table.getSelectedRowCount() == 1);
            btnDel.setEnabled(selected);
        });

//toolbar panel
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        toolbar.add(btnAdd);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnEdit);
        toolbar.add(btnView);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnDel);
        toolbar.add(btnRef);
        toolbar.add(searchField);
        toolbar.add(btnSearch);
//main layout
        window.setLayout(new BorderLayout());
        window.add(toolbar, BorderLayout.NORTH);
        window.add(scroll, BorderLayout.CENTER);

        window.setVisible(true);

    }
    //refresh the table
    private static void refresh(JTable table) {
        SwingWorker<String[][], Void> worker = new SwingWorker<>() {
            @Override
            protected String[][] doInBackground() throws SQLException {
                return PatientDB.returnUI(); // Database call
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
    // selected delete
    private static JButton deleteButton(JTable table, JFrame window){
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
    //add new record forum
    private static JButton addButton(JTable table){
        JButton btnAdd = new JButton("Add New");
        btnAdd.addActionListener(e -> {
            try{
                commonForum(table, -1);
            }catch (SQLException sqle){
                JOptionPane.showMessageDialog(null, "Error Adding Patient: " + sqle.getMessage());
            }
        });
        return btnAdd;
    }
    //make text field read only
    private static JTextField createReadOnlyField(String text, Font font) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        field.setBackground(UIManager.getColor("Panel.background"));
        field.setFont(font);
        field.setColumns(15);
        return field;
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
            try {
                Patient p = PatientDB.returnUISingle(id);
                JFrame disp = new JFrame("Patient Details");
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
                row++;

                gbc.gridx = 0; gbc.gridy = row;
                panel.add(new JLabel("Phone Number:"), gbc);

                gbc.gridx = 1;
                panel.add(createReadOnlyField(p.getPhone(), valueFont), gbc);
                row++;

                gbc.gridx = 0; gbc.gridy = row;
                panel.add(new JLabel("Date Of Creation:"), gbc);

                gbc.gridx = 1;
                panel.add(createReadOnlyField(p.getCreatedAt(), valueFont), gbc);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
                buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

                JButton btnEdit = editButton2(table, window, id);
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
                    refresh(table);
                    disp.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(disp, ex.getMessage());
                    return;
                }});

                buttonPanel.add(btnEdit);
                buttonPanel.add(btnDel);
                buttonPanel.add(btnClose);

                disp.setLayout(new BorderLayout());
                disp.add(panel, BorderLayout.CENTER);
                disp.add(buttonPanel, BorderLayout.SOUTH);
                disp.setVisible(true);
            }catch (SQLException sqle){
                JOptionPane.showMessageDialog(window,  "Error Occured: " + sqle.getMessage());
                return;
            }
        });
        return btnVew;
    }
    //edit button for patient info frame
    private static JButton editButton2(JTable table, JFrame window, int id) {
        JButton btn = new JButton("Edit Patient");

        btn.addActionListener(e -> {
            try {
                commonForum(table, id);
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

            try {commonForum(table, id);} catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Failed to load patient data");
                return;
            }
        });

        return btn;
    }
    //get data base table into GUI table
    private static JTable getDBData(JFrame window, String search){
        String[] headers = {"ID", "Name", "Age", "Gender", "Phone Number", "Created At"};
        String[][] data;
        try {
            data = PatientDB.returnUI();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
            window.dispose();
            throw new RuntimeException("Error While Reading Data");
        }
        DefaultTableModel model = new DefaultTableModel(data, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // prevent accidental edits
            }
        };
        return new JTable(model);
    }
    // common forum for both add and edit
    private static JFrame commonForum(JTable table, int id)  throws SQLException{
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
        JFrame forum = new JFrame(t);
        forum.setSize(320, 260);
        forum.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        forum.setLocationRelativeTo(null);
        forum.setLayout(new BorderLayout());
//Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        int row = 0;
//name
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
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

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
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
                    PatientDB.insert(newP);
                }
                refresh(table);
                forum.dispose();

            } catch (ValidationException ve) {
                JOptionPane.showMessageDialog(forum, ve.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(forum, "Modification Failed:" + ex.getMessage());
            }
        });
        btnCancel.addActionListener(ev -> forum.dispose());

        forum.add(formPanel, BorderLayout.CENTER);
        forum.add(buttonPanel, BorderLayout.SOUTH);
        forum.setVisible(true);
        return forum;
    }
    //return search data
    private static void search(JTable table, String search) {
        SwingWorker<String[][], Void> worker = new SwingWorker<>() {
            @Override
            protected String[][] doInBackground() throws SQLException {
                return PatientDB.returnUI(search); // Database call
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
}