package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.app.UserSession;
import com.jetbrains.uzair.db.InventoryDB;
import com.jetbrains.uzair.db.InventoryLogsDB;
import com.jetbrains.uzair.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
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
        JButton btnUpdate = update(table, window, 1);
        JButton btnView = viewItem(table, window, 1);
        JButton btnDel = commonUI.deleteButton(table, window, 3);
        JButton btnRef = new JButton("Refresh");
        btnRef.addActionListener(_ -> commonUI.refresh(table, 3));
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(_ -> commonUI.search(table, searchField.getText(), 3));
        searchField.addActionListener(_ -> btnSearch.doClick());
        commonUI.buttonHighlight(btnEdit, btnUpdate, btnView, btnDel, table);
        btnAdd.setMnemonic('A');
        btnRef.setMnemonic('R');
        btnDel.setMnemonic('D');
        btnUpdate.setMnemonic('S');
//toolbar panel
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        toolbar.setBackground(new Color(33, 69, 126));
        toolbar.add(btnAdd);
        toolbar.add(Box.createHorizontalStrut(20));
        if(UserSession.isAdmin()){toolbar.add(btnEdit);}
        toolbar.add(btnUpdate);
        toolbar.add(btnView);
        toolbar.add(Box.createHorizontalStrut(20));
        if(UserSession.isAdmin()){toolbar.add(btnDel);}
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
        btnAdd.addActionListener(_ -> {
            try{
                commonForum(table, -1, window, 1);
            }catch (SQLException sqle){
                JOptionPane.showMessageDialog(null, "Error Adding Patient: " + sqle.getMessage());
            }
        });
        return btnAdd;
    }
    private static void commonForum(JTable table, int id, JFrame window, int ui) throws SQLException{
        JDialog forum;
        Inventory i;
        String t;
        String nameS = null;
        String storageS = "Bottles";
        String amountS = null;
        String alertS = null;
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
            alertS = String.valueOf(i.getAlert());
        }
        forum = new JDialog(window, t, true);
        forum.setSize(500, 400);
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
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        JTextField amount = new JTextField(amountS, 15);
        formPanel.add(amount, gbc);
        row++;
        //alert
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Alert Amount:"), gbc);
        gbc.gridx = 1;
        JTextField alert = new JTextField(alertS, 15);
        formPanel.add(alert, gbc);
        //button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        buttonPanel.setBackground(new Color(12, 38, 78));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        btnSave.addActionListener(_ -> {
            try {
                String[] raw = {
                        String.valueOf(i.getId()),
                        name.getText(),
                        Objects.requireNonNull(storage.getSelectedItem()).toString(),
                        amount.getText(),
                        String.valueOf(LocalDateTime.now()),
                        alert.getText()
                };
                if(id != -1) {
                    Inventory newI = Inventory.convArrayToOb(raw, false);
                    InventoryDB.edit(newI);
                }else{
                    Inventory newI = Inventory.convArrayToOb(raw, true);
                    InventoryDB.insert(newI);
                }
                commonUI.refresh(table, (ui == 1  ? 3: 7));
                forum.dispose();
            } catch (ValidationException | SQLException ve) {
                JOptionPane.showMessageDialog(forum, ve.getMessage());
            }
        });
        forum.getRootPane().setDefaultButton(btnSave);
        FocusUtils.enableArrowNavigation(name, storage, amount, alert);
        btnCancel.addActionListener(_ -> forum.dispose());
        forum.add(header, BorderLayout.NORTH);
        forum.add(formPanel, BorderLayout.CENTER);
        forum.add(buttonPanel, BorderLayout.SOUTH);
        forum.setVisible(true);
    }
    private static JButton update(JTable table, JFrame window, int ui){
        JButton btn = new JButton("Update Stock");

        btn.addActionListener(_ -> {
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

            updateStock(id, window);
            if(ui == 1) {
                commonUI.refresh(table, 3);
            } else if (ui == 2) {
                commonUI.refresh(table, 7);
            }
        });
        return btn;
    }
    private static JButton editButton1(JTable table, JFrame window) {
        JButton btn = new JButton("Edit Item");

        btn.addActionListener(_ -> {
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

            try {commonForum(table, id, window, 1);} catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Failed To Load Item Data");
            }
        });
        return btn;
    }
    private static JButton viewItem(JTable table, JFrame window, int ui) {
        JButton btnVew = new JButton("View Item Data");
        btnVew.addActionListener(_ -> {
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
            itemDetails(table, window, id, ui);
        });
        return btnVew;
    }
    public static void itemDetails(JTable table, JFrame window, int id, int ui){
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
            buttonPanel.setBackground(new Color(12, 38, 78));

            JButton btnEdit = editButton2(table, id, window, ui);
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
                    if(ui == 1) commonUI.refresh(table, 3); else if (ui == 2) commonUI.refresh(table, 7);
                    disp.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(disp, ex.getMessage());
                }});
            JButton btnAddLog = new JButton("Update Stock");
            btnAddLog.addActionListener(_ ->{updateStock(i.getId(), window); if(ui == 1) commonUI.refresh(table, 3); else if (ui == 2) commonUI.refresh(table, 7);});
            disp.getRootPane().setDefaultButton(btnAddLog);
            if(UserSession.isAdmin()) {
                commonUI.commonAddBtn(buttonPanel, btnDel, btnAddLog, btnEdit, btnClose);
            }else {
                commonUI.commonAddBtn(buttonPanel, btnAddLog, btnClose);
            }
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
        String[] headers = { "Log ID", "User", "Amount", "Date" };
        DefaultTableModel model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<InventoryLogs> logs = InventoryLogsDB.returnLogs(Id);

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
        JTableHeader header1 = table.getTableHeader();

        header1.setDefaultRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );

                lbl.setBackground(new Color(22, 51, 83)); // dark blue
                lbl.setForeground(Color.WHITE);
                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
                lbl.setOpaque(true);

                return lbl;
            }
        });
        header1.setReorderingAllowed(false);
        header1.setResizingAllowed(true);

        int amountColumnIndex = 2; // "Amount" column

        AmountRowRenderer renderer = new AmountRowRenderer(amountColumnIndex);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        return new JScrollPane(table);
    }
    private static JButton editButton2(JTable table, int id, JFrame window, int ui){
        JButton btn = new JButton("Edit Item");
        btn.addActionListener(_ -> {
            try {
                commonForum(table, id, window, ui);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Failed To Load Item Data");
            }
            commonUI.refresh(table, (ui == 1  ? 3: 7));

        });
        return btn;
    }
    private static void updateStock(int inventoryId, JFrame window) {

        JDialog dialog = new JDialog(window, "Update Item Stock", true);
        dialog.setSize(450, 300);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(window);
        dialog.setLayout(new BorderLayout());

        if (!UserSession.isLoggedIn()) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Session Expired, Please Log In Again.",
                    "Session Error",
                    JOptionPane.ERROR_MESSAGE
            );
            dialog.dispose();
            return;
        }

        // Header
        JPanel header = commonUI.commonHeader("Update Item Stock");
        dialog.add(header, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = commonUI.commonForum();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        int row = 0;

        // Amount label
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Stock Change:"), gbc);

        // Amount field
        JTextField amountField = new JTextField();
        amountField.setToolTipText("Use positive to add, negative to remove stock");

        amountField.requestFocus();
        gbc.gridx = 1;
        formPanel.add(amountField, gbc);

        // Hint
        row++;
        gbc.gridx = 1;
        gbc.gridy = row;
        JLabel hint = new JLabel("Example: +10 to add, -5 to remove");
        hint.setFont(hint.getFont().deriveFont(Font.ITALIC, 11f));
        hint.setForeground(Color.GRAY);
        formPanel.add(hint, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(12, 38, 78));

        JButton updateBtn = new JButton("Update");
        JButton cancelBtn = new JButton("Cancel");

        buttonPanel.add(cancelBtn);
        buttonPanel.add(updateBtn);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        cancelBtn.addActionListener(_ -> dialog.dispose());

        updateBtn.addActionListener(_ -> {
            String text = amountField.getText().trim();

            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Please enter an amount.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int amount;
            try {
                amount = Integer.parseInt(text);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Amount must be a number.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (amount == 0) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Amount cannot be zero.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // 🔗 DB call (example)
            try {
                updateBtn.setEnabled(false);
                InventoryLogsDB.insert(new InventoryLogs(inventoryId, UserSession.getUserId(), amount, null));
                dialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Failed to update stock." + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
                updateBtn.setEnabled(true);
            }
        });
        dialog.getRootPane().setDefaultButton(updateBtn);

        dialog.setVisible(true);
    }
    public static JDialog alertDialog(JFrame window){
        JDialog panel = new JDialog(window, "Items Low On Stock", true);
        panel.setSize(770, 810);
        panel.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        panel.setLocationRelativeTo(null);
        panel.setLayout(new BorderLayout());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
//table
        JTable table = commonUI.getDBData(window, 7);
        if (table.getRowCount() == 0){return null;}
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
//buttons
        JButton btnUpdate = update(table, window, 2);
        JButton btnView = viewItem(table, window, 2);
        JButton btnRef = new JButton("Refresh");
        btnRef.addActionListener(_ -> commonUI.refresh(table, 7));
        commonUI.buttonHighlight( btnUpdate, btnView,  table);
        btnRef.setMnemonic('R');
        btnUpdate.setMnemonic('S');
//toolbar panel
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        toolbar.setBackground(new Color(33, 69, 126));
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(btnUpdate);
        toolbar.add(btnView);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(btnRef);
//main layout
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.setVisible(true);
        return panel;
    }
}
