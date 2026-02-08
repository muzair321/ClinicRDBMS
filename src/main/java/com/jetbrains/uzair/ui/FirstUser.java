package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.UsersDB;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class FirstUser {
    public static void frame(){
        JFrame frame =  new JFrame("Add Admin User");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        //header
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setBackground(new Color(12, 38, 78));

        ImageIcon logo = new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/Clinic-Uncolored.png")));
        Image scaled = logo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaled));

        JLabel title = new JLabel("Changez Clinic - Chak Beli Khan");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.X_AXIS));
        leftHeader.setOpaque(false); // keep header background visible
        leftHeader.add(logoLabel);
        leftHeader.add(Box.createHorizontalStrut(30));

        header.add(leftHeader, BorderLayout.WEST);
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

        //buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        btnCancel.addActionListener(_ ->System.exit(0));
        btnSave.addActionListener(_ ->
        {
            try {
                UsersDB.insert(name.getText(), pass.getText(), 1);
                frame.dispose();
                new LoginFrame();
            } catch (SQLException sqle) {
                JOptionPane.showMessageDialog(frame, "Error Inserting User Data" + sqle.getMessage());
                System.exit(0);
            }
        });

        frame.add(header, BorderLayout.NORTH);
        frame.add(forum, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
