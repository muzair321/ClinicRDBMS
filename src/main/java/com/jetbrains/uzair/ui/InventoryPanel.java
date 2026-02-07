package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.InventoryDB;
import com.jetbrains.uzair.db.InventoryLogsDB;
import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.db.VisitDB;
import com.jetbrains.uzair.model.Inventory;
import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.model.ValidationException;
import com.jetbrains.uzair.model.Visit;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class InventoryPanel {
    public static JPanel mainWindow(JFrame window){
        JPanel panel = new JPanel();

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        //search
        PlaceholderTextField searchField = new PlaceholderTextField("Search Inventory");

        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(255, 255, 255));
        searchField.setOpaque(true);
//table
        JTable table = commonUI.getDBData(window, 3);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
//buttons
        JButton btnAdd = addButton(table, window);
        JButton btnEdit = editButton1(table, window);
        JButton btnView = viewItem(table, window);
        JButton btnDel = commonUI.deleteButton(table, window, 3);
        JButton btnRef = new JButton("Refresh");
        btnRef.addActionListener(e -> commonUI.refresh(table, 3));
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> commonUI.search(table, searchField.getText(), 3));
        commonUI.buttonHighlight(btnEdit, btnView, btnDel, table);
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
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.setVisible(true);
        return panel;
    }
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
    private static void commonForum(JTable table, int id, JFrame window) throws SQLException{
        JDialog forum;
        Inventory i;
        String t;
        String nameS = null;
        String storageS = "Bottles";
        String amountS = null;
        if(id == -1){
            t = "Add To Inventory";
            i = new Inventory();
        }else{
            try {
                SoundPlayer.play("popup.wav");
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
            i = InventoryDB.returnUISingle(id);
            t = "Edit Inventory Item";
            nameS = i.getName();
            storageS = i.getStorage();
            amountS = String.valueOf(i.getAmount());
        }
        forum = new JDialog(window, t, true);
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
        //name
        JTextField name = new JTextField(nameS, 15);
        formPanel.add(name, gbc);
        row++;
        //storage
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Storage:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> storage =
                new JComboBox<>(new String[]{"Bottles", "Strips", "Tablets","Tubes", "Powder Packs"});
        storage.setSelectedItem(storageS);
        formPanel.add(storage, gbc);
        row++;
        //amount
        row++;gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        JTextField amount = new JTextField(amountS, 15);
        formPanel.add(amount, gbc);
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
                        String.valueOf(i.getId()),
                        name.getText(),
                        Objects.requireNonNull(storage.getSelectedItem()).toString(),
                        amount.getText(),
                        String.valueOf(LocalDateTime.now())
                };
                if(id != -1) {
                    Inventory newI = Inventory.convArrayToOb(raw, false);
                    InventoryDB.edit(newI);
                }else{
                    Inventory newI = Inventory.convArrayToOb(raw, true);
                    InventoryDB.insert(newI);
                }
                commonUI.refresh(table, 3);
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
    }
    private static JButton editButton1(JTable table, JFrame window) {
        JButton btn = new JButton("Edit Item");

        btn.addActionListener(e -> {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(window, "No Item(s) Selected");
                return;
            }
            if (selectedRows.length > 1){
                JOptionPane.showMessageDialog(window, "Select Only 1 Item");
                return;
            }
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());

            try {commonForum(table, id, window);} catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Failed To Load Item Data");
            }
        });
        return btn;
    }
    private static JButton viewItem(JTable table, JFrame window) {
        JButton btnVew = new JButton("View Item Data");
        btnVew.addActionListener(e -> {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(window, "No Item(s) Selected");
                return;
            }
            if (selectedRows.length > 1) {
                JOptionPane.showMessageDialog(window, "Select Only 1 Item");
                return;
            }

            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());
            itemDetails(table, window, id);
        });
        return btnVew;
    }
    public static void itemDetails(JTable table, JFrame window, int id){
        try {
            Inventory i = InventoryDB.returnUISingle(id);
            JDialog disp = commonUI.commonDialog("Item Details", window);
            JPanel panel = commonUI.commonPanel();

            disp.setSize(770, 810);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            int row = 0;
            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("ID:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(String.valueOf(i.getId()), commonUI.valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Name:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(i.getName(), commonUI.valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Storage:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(i.getStorage(), commonUI.valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Amount:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(String.valueOf(i.getAmount()), commonUI.valueFont), gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Last Updated At:"), gbc);

            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(i.getUpdatedAt(), commonUI.valueFont), gbc);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
            buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

            JButton btnEdit = editButton2(table, id, window);
            JButton btnClose = new JButton("Close");
            btnClose.addActionListener(_ -> disp.dispose());
            JButton btnDel = new JButton("Delete");
            btnDel.addActionListener(_ -> {
                try {
                    SoundPlayer.play("popup.wav");
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(window, ex.getMessage());
                }
                int choice = JOptionPane.showConfirmDialog(
                        window,
                        "Are You Sure You Want To Delete The Selected Item(s)?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice != JOptionPane.YES_OPTION) {
                    return;
                }
                try{
                    InventoryDB.delete(id);
                    JOptionPane.showMessageDialog(window, "Item Deleted Successfully");
                    commonUI.refresh(table, 3);
                    disp.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(disp, ex.getMessage());
                }});
            JButton btnAddLog = new JButton("Update Stock");
            btnAddLog.addActionListener(_ -> {
                try {
                    commonForum(table, -1, window);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(window, ex.getMessage());
                }
            });
            commonUI.commonAddBtn(buttonPanel, btnDel, btnAddLog, btnEdit, btnClose);
            //header
            JPanel header = commonUI.commonHeader("Item Data");
            //panels
            JPanel centerPanel = new JPanel();
            JScrollPane bottom = getLogTables(id);
            centerPanel.setLayout(new BorderLayout());
            centerPanel.add(panel, BorderLayout.NORTH);
            centerPanel.add(bottom, BorderLayout.CENTER);
            disp.setLayout(new BorderLayout());
            disp.add(header, BorderLayout.NORTH);
            disp.add(centerPanel, BorderLayout.CENTER);
            disp.add(buttonPanel, BorderLayout.SOUTH);
            disp.setVisible(true);
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(window,  "Error Occured: " + sqle.getMessage());
        }
    }
    private static JScrollPane getLogTables(int Id) throws SQLException {
//todo: modify according to users table
        String[] headers = { "Log ID", "User", "Amount", "Date" };
        DefaultTableModel model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Visit> logs = InventoryLogsDB.returnVisits(Id);

        for (InventoryLogs l : logs) {
            model.addRow(new Object[]{
                    l.getId(),
                    l.getUserName(),
                    l.getAmount(),
                    l.getDate()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);

        return new JScrollPane(table);
    }
    private static JButton editButton2(JTable table, int id, JFrame window){
        JButton btn = new JButton("Edit Item");
        btn.addActionListener(e -> {
            try {
                commonForum(table, id, window);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Failed To Load Item Data");
            }
        });
        return btn;
    }
}
