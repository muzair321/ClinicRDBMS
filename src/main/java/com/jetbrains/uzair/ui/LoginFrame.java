package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.app.UserSession;
import com.jetbrains.uzair.db.UserLogsDB;
import com.jetbrains.uzair.db.UsersDB;
import com.jetbrains.uzair.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        ImageIcon logo1 = new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/Clinic-Colored.png")));
        Image img = logo1.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        setIconImage(img);
        setTitle("Clinic Login");
        setSize(450, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setResizable(false);
        add(header(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel header(){
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setBackground(new Color(12, 38, 78));

        ImageIcon logo = new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/Clinic-Uncolored.png")));
        Image scaled = logo.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaled));

        JLabel title = new JLabel("User Log In");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.X_AXIS));
        leftHeader.setOpaque(false); // keep header background visible
        leftHeader.add(logoLabel);
        leftHeader.add(Box.createHorizontalStrut(70));

        header.add(leftHeader, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        return header;
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

        // Password row
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;

// Container for password + eye button
        JPanel passwordPanel = new JPanel(new BorderLayout(5, 0));
        passwordPanel.setOpaque(false);

        passwordField = new JPasswordField(15);

        FocusUtils.enableArrowNavigation(usernameField, passwordField);
// Eye button
        JButton toggleBtn = new JButton();
        toggleBtn.setToolTipText("Show / hide password");
        toggleBtn.setFocusPainted(false);
        toggleBtn.setBorderPainted(false);
        toggleBtn.setContentAreaFilled(false);
        toggleBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ImageIcon showRawIcon = new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/showIcon.png")));
        final ImageIcon showIcon =new ImageIcon(showRawIcon.getImage().getScaledInstance(18, 10, Image.SCALE_SMOOTH));
        ImageIcon hideRawIcon = new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/hideIcon.png")));
        final ImageIcon hideIcon = new ImageIcon(hideRawIcon.getImage().getScaledInstance(20, 11, Image.SCALE_SMOOTH));

        toggleBtn.setIcon(showIcon);
        final char defaultEcho = passwordField.getEchoChar();

        toggleBtn.addActionListener(e -> {
            if (passwordField.getEchoChar() != 0) {
                passwordField.setEchoChar((char) 0); // show
                toggleBtn.setIcon(hideIcon);
            } else {
                passwordField.setTransferHandler(null);
                passwordField.setEchoChar(defaultEcho); // hide
                toggleBtn.setIcon(showIcon);
            }
        });

        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(toggleBtn, BorderLayout.EAST);

        panel.add(passwordPanel, gbc);

        return panel;
    }

    private JPanel buildButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(12, 38, 78));
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(_ -> login());
        getRootPane().setDefaultButton(loginBtn);
        panel.add(Box.createHorizontalStrut(20));
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
            try {
                UserLogsDB.insert(UserSession.getUserId());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
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
