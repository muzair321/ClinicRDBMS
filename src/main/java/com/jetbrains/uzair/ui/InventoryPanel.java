package com.jetbrains.uzair.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

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
    private static JDialog commonForum(JTable table, int id, JFrame window){
        JDialog form;
        if(id == -1){
            form = new JDialog(window, "Add To Inventory", true);
        }else{
            form = new JDialog(window, "Edit Inventory Item", true);
        }
        return form;
    }
}
