package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.app.UserSession;
import com.jetbrains.uzair.db.UsersDB;
import com.jetbrains.uzair.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Clinic Login");
        setSize(350, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildForm(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.requestFocusInWindow();
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        return panel;
    }

    private JPanel buildButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(_ -> login());

        // Press ENTER to log in
        getRootPane().setDefaultButton(loginBtn);

        panel.add(loginBtn);
        return panel;
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        User user = null;

        try {
            user = UsersDB.authenticate(username, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        if (user != null) {
            UserSession.login(user);
            MainFrame.window();
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
