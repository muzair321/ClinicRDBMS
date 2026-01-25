package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.Database;
import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.db.VisitDB;
import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.model.ValidationException;
import com.jetbrains.uzair.model.Visit;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VisitPanel {
    public static JPanel mainWindow(JFrame window){
        JPanel panel = new JPanel();

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        //search
        PlaceholderTextField searchField = new PlaceholderTextField("Search Visit");

        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(255, 255, 255));
        searchField.setOpaque(true);
        //table
        JTable table = commonUI.getDBData(window, 2);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        //buttons
        JButton btnVisit = editButton1(table, window);
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(_ -> commonUI.search(table, searchField.getText(), 2));
        JButton btnDelete = new JButton("Delete Visit");
        JButton btnView = viewVisit(table, window);
        //toolbar
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        toolbar.add(btnView);
        toolbar.add(btnVisit);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnDelete);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(searchField);
        toolbar.add(btnSearch);
        //main layout
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }
    public static void commonForum( JFrame window, int patientId, int id) throws SQLException{
        Visit v;
        String name = "Error";
        String treatS;
        String payS;
        String t;
        if(id != -1){
            v = VisitDB.returnUISingle(id);
            t = "Edit Visit Details";
            treatS = v.getTreatment();
            payS = Integer.toString(v.getPaid());
        }else{
            v = new Visit();
            t =  "Add Visit Details";
            treatS = v.getTreatment();
            payS = Integer.toString(v.getPaid());
        }

        Font valueFont = new Font("Segoe UI", Font.PLAIN, 13);
        JDialog forum = new JDialog(window,t, true);
        forum.setSize(500, 600);
        forum.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        forum.setLocationRelativeTo(null);
        forum.setLayout(new BorderLayout());

        try{name = PatientDB.getName(patientId);} catch (SQLException e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
        //header
        JPanel header = new JPanel();
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setBackground(new Color(12, 38, 78));

        JLabel head = new JLabel(t);
        head.setForeground(Color.WHITE);
        head.setFont(new Font("Segoe UI", Font.BOLD, 30));
        header.add(head);

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
        formPanel.add(commonUI.createReadOnlyField(name, valueFont), gbc);
        row++;
//treatment
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Treatment:"), gbc);
        gbc.gridx = 1;
        JTextField treat = new JTextField(treatS, 15);
        formPanel.add(treat, gbc);
        row++;
//payment
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Payment:"), gbc);

        gbc.gridx = 1;
        JTextField pay = new JTextField(payS, 15);
        formPanel.add(pay, gbc);
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
                        String.valueOf(v.getId()),
                        Integer.toString(patientId),
                        treat.getText(),
                        pay.getText()
                };
                if(id != -1){
                    VisitDB.edit(Visit.check(Visit.convArrayToOb(raw)));
                }else {
                    VisitDB.insert(Visit.check(Visit.convArrayToOb(raw)));
                }
                forum.dispose();
            } catch (ValidationException ve) {
                JOptionPane.showMessageDialog(forum, ve.getMessage());
            }catch (SQLException e){
                JOptionPane.showMessageDialog(forum, e.getMessage());
            }
        });
        btnCancel.addActionListener(ev -> forum.dispose());
        forum.add(header, BorderLayout.NORTH);
        forum.add(formPanel, BorderLayout.CENTER);
        forum.add(buttonPanel, BorderLayout.SOUTH);
        forum.setVisible(true);
    }
    private static JButton editButton1(JTable table, JFrame window) {
        JButton btn = new JButton("Edit");

        btn.addActionListener(e -> {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(window, "No Visit(s) Selected");
                return;
            }
            if (selectedRows.length > 1){
                JOptionPane.showMessageDialog(window, "Select Only 1 Visit");
                return;
            }
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());
            int patientId = Integer.parseInt(table.getModel().getValueAt(modelRow, 1).toString());

            try {commonForum(window, patientId, id);} catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Failed To Load Visit Data");
                return;
            }
        });

        return btn;
    }
    private static JButton viewVisit(JTable table, JFrame window){
        JButton btnVew = new JButton("Details");
        btnVew.addActionListener(e -> {
        int[] selectedRows = table.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(window, "No Visit(s) Selected");
            return;
        }
        if (selectedRows.length > 1){
            JOptionPane.showMessageDialog(window, "Select Only 1 Visit");
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());
        try {
            com.jetbrains.uzair.model.Visit v = VisitDB.returnUISingle(id);
            JDialog disp = new JDialog(window,"Visit Details", true);
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
            panel.add(new JLabel("Visit ID:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(String.valueOf(v.getId()), valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Patient ID:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(String.valueOf(v.getPatientId()), valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Patient Name:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(PatientDB.getName(v.getPatientId()), valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Treatement:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(v.getTreatment(), valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Date Of Visit:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(v.getDate(), valueFont), gbc);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
            buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

            JButton btnEdit = editButton2(v.getPatientId(), window, id);
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
                        "Are You Sure You Want To Delete The Selected Visit(s)?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice != JOptionPane.YES_OPTION) {
                    return;
                }
                try{
                    VisitDB.delete(id);
                    JOptionPane.showMessageDialog(window, "Visit Deleted Successfully");
                    disp.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(disp, ex.getMessage());
                }});
            JButton btnViewPatient = new JButton("View Patient");
            btnViewPatient.addActionListener(e1 -> {commonUI.patientDetails(commonUI.getDBData(window, 1), window, v.getPatientId());});

            buttonPanel.add(btnViewPatient);
            buttonPanel.add(btnEdit);
            buttonPanel.add(btnDel);
            buttonPanel.add(btnClose);

            //header
            JPanel header = new JPanel();
            header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            header.setBackground(new Color(12, 38, 78));

            JLabel head = new JLabel("Visit Data");
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
    });
        return btnVew;
    }
    private static JButton editButton2(int patientId, JFrame window, int id) {
        JButton btn = new JButton("Edit Visit");

        btn.addActionListener(e -> {
            try {
                commonForum(window, patientId, id);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Failed to load patient data");
            }
        });
        return btn;
    }
}