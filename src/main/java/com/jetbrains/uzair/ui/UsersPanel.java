package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.UsersDB;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class UsersPanel {
    public static JPanel mainWindow(JFrame window){
        JPanel panel = new JPanel();
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
//table
        JTable table = commonUI.getDBData(window, 5);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
//buttons
        JButton btnAdd = addButton(table, window);
        JButton btnEdit = editButton( window, table);
        JButton btnDel = commonUI.deleteButton(table, window, 5);
        JButton btnRef = new JButton("Refresh");
        btnRef.addActionListener(_ -> commonUI.refresh(table, 5));
        commonUI.buttonHighlight(btnDel, table);
        btnAdd.setMnemonic('A');
        btnRef.setMnemonic('R');
        btnDel.setMnemonic('D');
//toolbar panel
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        toolbar.setBackground(new Color(33, 69, 126));
        toolbar.add(btnAdd);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnEdit);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnDel);
        toolbar.add(btnRef);
        toolbar.add(userLogsButton(window));
//main layout
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.setVisible(true);
        return panel;
    }
    private static JButton addButton(JTable table, JFrame window){
        JButton btn = new JButton("Add New User");
        btn.addActionListener(_ -> returnAddForum( window, table));
        return btn;
    }
    private static void returnAddForum(JFrame window, JTable table){
        JDialog frame =  new JDialog( window, "Add User", true);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        //header
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setBackground(new Color(12, 38, 78));
        JLabel title = new JLabel("Add New User");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.add(title, BorderLayout.CENTER);
        //forum
        JPanel forum = commonUI.commonForum();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        int row = 0;
        //username
        gbc.gridx = 0; gbc.gridy = row;
        forum.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField name = new JTextField();
        forum.add(name, gbc);
        row++;
        //password
        gbc.gridx = 0; gbc.gridy = row;
        forum.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JTextField pass = new JTextField();
        forum.add(pass, gbc);
        row++;
        FocusUtils.enableArrowNavigation(name, pass);
        //admin access
        gbc.gridx = 0; gbc.gridy = row;
        forum.add(new JLabel("Admin Access:"), gbc);
        gbc.gridx = 1;
        JRadioButton yesBtn = new JRadioButton("Yes");
        JRadioButton noBtn  = new JRadioButton("No");
        ButtonGroup group = new ButtonGroup();
        group.add(yesBtn);
        group.add(noBtn);
        noBtn.setSelected(true); // default
        JPanel choicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        choicePanel.add(yesBtn);
        choicePanel.add(noBtn);
        forum.add(choicePanel, gbc);
        //buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        buttonPanel.setBackground(new Color(12, 38, 78));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        btnCancel.addActionListener(_ ->System.exit(0));
        btnSave.addActionListener(_ ->
        {
            if(name.getText().contains(" ")){
                JOptionPane.showMessageDialog(frame, "No Spaces In Username Allowed");
            }
            if(name.getText().isEmpty() || pass.getText().isEmpty()){
                JOptionPane.showMessageDialog(frame, "Username and Password Cannot Be Empty");
            }
            else{
                int admin = yesBtn.isSelected() ? 1: 0;
                try {
                    UsersDB.exists(name.getText());
                    UsersDB.insert(name.getText(), pass.getText(), admin);
                    commonUI.refresh(table, 5);
                    frame.dispose();
                } catch (SQLException sqle) {
                    JOptionPane.showMessageDialog(frame,  sqle.getMessage());
                }
            }
        });
        frame.getRootPane().setDefaultButton(btnSave);
        frame.add(header, BorderLayout.NORTH);
        frame.add(forum, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    private static JButton editButton(JFrame window, JTable table){
        JButton btn = new JButton("Change Admin Password");
        btn.addActionListener(e -> changePasswordForum(window, table));
        return btn;
    }
    private static void changePasswordForum(JFrame window, JTable table){
        JDialog frame =  new JDialog( window, "Change First User Password", true);
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        //header
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setBackground(new Color(12, 38, 78));
        JLabel title = new JLabel("Change First User Password (ID: 1)");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.add(title, BorderLayout.CENTER);
        //forum
        JPanel forum = commonUI.commonForum();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        int row = 0;
        //password
        gbc.gridx = 0; gbc.gridy = row;
        forum.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JTextField pass = new JTextField();
        forum.add(pass, gbc);
        //buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        buttonPanel.setBackground(new Color(12, 38, 78));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        btnCancel.addActionListener(_ ->System.exit(0));
        btnSave.addActionListener(_ ->
        {
            if(pass.getText().isEmpty()){
                JOptionPane.showMessageDialog(frame, "Username and Password Cannot Be Empty");
            }
            else{
                try {
                    UsersDB.changeAdminPassword(pass.getText());
                    commonUI.refresh(table, 5);
                    frame.dispose();
                } catch (SQLException sqle) {
                    JOptionPane.showMessageDialog(frame, "Error Inserting User Data" + sqle.getMessage());
                }
            }
        });
        frame.getRootPane().setDefaultButton(btnSave);
        frame.add(header, BorderLayout.NORTH);
        frame.add(forum, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    private static JButton userLogsButton(JFrame window){
        JButton btn = new JButton("User Logs");
        btn.addActionListener(_ -> userLogs(window));
        return btn;
    }
    private static void userLogs(JFrame window){
        JDialog frame = new JDialog(window, "User Logs", true);
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        //search
        PlaceholderTextField searchField = new PlaceholderTextField("Search User Logs");
        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(255, 255, 255));
        searchField.setOpaque(true);
        //header
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setBackground(new Color(12, 38, 78));
        JLabel title = new JLabel("User Logs");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.add(title, BorderLayout.CENTER);
        //table
        JTable t = commonUI.getDBData(window, 6);
        t.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scroll = new JScrollPane(t);
        //buttons
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        toolbar.setBackground(new Color(33, 69, 126));
        JButton btnRef = new JButton("Refresh");
        btnRef.addActionListener(_ -> commonUI.refresh(t, 6));
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> commonUI.search(t, searchField.getText(), 6));
        searchField.addActionListener(_ -> btnSearch.doClick());
        btnRef.setMnemonic('R');
        toolbar.add(btnRef);
        toolbar.add(searchField);
        toolbar.add(btnSearch);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.setVisible(true);
        frame.add(header, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
