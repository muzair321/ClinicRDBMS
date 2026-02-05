package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.InventoryDB;
import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.model.Inventory;
import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.model.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
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
        JTable table = commonUI.getDBData(window, 1);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
//buttons
        JButton btnAdd = addButton(table, window);
        JButton btnEdit =
        JButton btnView =
        JButton btnDel = commonUI.deleteButton(table, window, 3);
        JButton btnRef = new JButton("Refresh");
        btnRef.addActionListener(e -> commonUI.refresh(table, 3));
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> );
        commonUI.buttonHighlight();

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

    }
}
