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

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setBackground(new Color(12, 38, 78));

        ImageIcon logo = new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/img/Clinic-Uncolored.png")));
        Image scaled = logo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaled));

        JLabel title = new JLabel("Changez Clinic - Chak Beli Khan");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.X_AXIS));
        leftHeader.setOpaque(false); // keep header background visible
        leftHeader.add(logoLabel);
        leftHeader.add(Box.createHorizontalStrut(15));

        header.add(leftHeader, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);

        // ================= TABS =================
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 17));

        // Example panels (replace with your real panels)
        tabs.addTab("Patients", PatientPanel.mainWindow(frame));
        tabs.addTab("Visits", VisitPanel.mainWindow(frame));
        tabs.addTab("Inventory", InventoryPanel.mainWindow(frame));
        tabs.addTab("Inventory Logs", InventoryLogsPanel.mainWindow(frame));

        // ================= FOOTER =================
        JPanel footer = getJPanel();


        // ================= ADD TO FRAME =================
        frame.add(header, BorderLayout.NORTH);
        frame.add(tabs, BorderLayout.CENTER);
        frame.add(footer, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static JPanel getJPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        footer.setBackground(new Color(245, 245, 245));

// Left
        JLabel copyright =
                new JLabel("© 2026 Clinic Software by Muhammad Uzair");
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyright.setForeground(Color.GRAY);

// Center
        String roleText = UserSession.isAdmin()
                ? "Admin Interface"
                : "Standard Interface";

        JLabel roleLabel = new JLabel(roleText, SwingConstants.CENTER);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(90, 90, 90));
        if (UserSession.isAdmin()) {
            roleLabel.setForeground(new Color(0, 120, 0));
        }

// Right
        JButton helpButton = new JButton("?");
        helpButton.setToolTipText("Help");
        helpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                helpButton.setForeground(new Color(116, 117, 119));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                helpButton.setForeground(Color.DARK_GRAY);
            }
        });


// Add
        footer.add(copyright, BorderLayout.WEST);
        footer.add(roleLabel, BorderLayout.CENTER);
        footer.add(helpButton, BorderLayout.EAST);
        return footer;
    }
}
