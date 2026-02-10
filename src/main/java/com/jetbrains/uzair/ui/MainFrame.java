package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.app.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class MainFrame {

    public static void window() {
        SwingUtilities.invokeLater(MainFrame::createUI);
    }

    private static void createUI() {
        JFrame frame = new JFrame("Changez Clinic");
        frame.setSize(1200, 900);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setBackground(new Color(12, 38, 78));
        ImageIcon logo1 = new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/Clinic-Colored.png")));
        Image img = logo1.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        frame.setIconImage(img);

        //HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setBackground(new Color(12, 38, 78));

        ImageIcon logo = new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/Clinic-Uncolored.png")));
        Image scaled = logo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaled));

        JLabel title = new JLabel("Changez Clinic - Chak Beli Khan");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        JLabel user = new JLabel("Logged In As " + UserSession.getUser().getUsername());
        user.setForeground(Color.WHITE);
        user.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.X_AXIS));
        leftHeader.setOpaque(false);
        leftHeader.add(logoLabel);
        leftHeader.add(Box.createHorizontalStrut(15));


        header.add(leftHeader, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(user, BorderLayout.EAST);

        //TABS
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        tabs.setForeground(new Color(29, 60, 112));
        tabs.addTab("Patients", PatientPanel.mainWindow(frame));
        tabs.addTab("Visits", VisitPanel.mainWindow(frame));
        tabs.addTab("Inventory", InventoryPanel.mainWindow(frame));
        tabs.addTab("Inventory Logs", InventoryLogsPanel.mainWindow(frame));
        if(UserSession.isAdmin()) {
            tabs.addTab("Manage Users", UsersPanel.mainWindow(frame));
        }

        // FOOTER
        JPanel footer = getJPanel(frame);
        footer.setBackground(new Color(12, 38, 78));


        // ADD TO FRAME
        frame.add(header, BorderLayout.NORTH);
        frame.add(tabs, BorderLayout.CENTER);
        frame.add(footer, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static JPanel getJPanel(JFrame window) {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        footer.setBackground(new Color(245, 245, 245));

        // Left
        JLabel copyright =
                new JLabel("© 2026 Clinic Software by Muhammad Uzair");
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyright.setForeground(Color.WHITE);

        // Center
        String roleText = UserSession.isAdmin() ? "Admin Interface" : "Standard Interface";

        JLabel roleLabel = new JLabel(roleText, SwingConstants.CENTER);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(UserSession.isAdmin()
                ? new Color(158, 232, 145)
                : new Color(147, 176, 255));

        // Right buttons panel
        JPanel rightPanel = getPanel(footer, window);

        // Add to footer
        footer.add(copyright, BorderLayout.WEST);
        footer.add(roleLabel, BorderLayout.CENTER);
        footer.add(rightPanel, BorderLayout.EAST);

        return footer;
    }

    private static JPanel getPanel(JPanel footer, JFrame window) {
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setOpaque(false);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setToolTipText("Log out");
        logoutButton.setFocusPainted(false);

        JButton helpButton = new JButton("?");
        helpButton.setToolTipText("Help");
        helpButton.setFocusPainted(false);

        // Hover effect (shared)
        MouseAdapter hover = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JButton) e.getSource()).setForeground(new Color(116, 117, 119));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                ((JButton) e.getSource()).setForeground(Color.DARK_GRAY);
            }
        };
        logoutButton.addMouseListener(hover);
        helpButton.addMouseListener(hover);
        logoutButton.addActionListener(_ -> {
            int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Are You Sure You Want To Log Out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                UserSession.logout();
                new LoginFrame();
                SwingUtilities.getWindowAncestor(footer).dispose();
            }
        });
        helpButton.addActionListener(_ -> new AboutDialog(null).setVisible(true));
        rightPanel.add(logoutButton);
        rightPanel.add(helpButton);
        return rightPanel;
    }
}
