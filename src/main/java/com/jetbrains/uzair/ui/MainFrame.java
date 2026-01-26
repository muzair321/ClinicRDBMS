package com.jetbrains.uzair.ui;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridBagLayout;

public class MainFrame {

    public static void main(String[] args) {
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

        ImageIcon logo = new ImageIcon(MainFrame.class.getResource("/img/Clinic-Uncolored.png"));
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
        tabs.addTab("Inventory", createPlaceholderPanel("Inventory Panel"));
        tabs.addTab("Reports", createPlaceholderPanel("Reports Panel"));

        // ================= FOOTER =================
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        footer.setBackground(new Color(245, 245, 245));

        JLabel copyright =
                new JLabel("© 2026 Clinic Software by Muhammad Uzair");
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton helpButton = new JButton("?");
        helpButton.setFocusPainted(false);
        helpButton.setPreferredSize(new Dimension(45, 25));

        footer.add(copyright, BorderLayout.WEST);
        footer.add(helpButton, BorderLayout.EAST);

        // ================= ADD TO FRAME =================
        frame.add(header, BorderLayout.NORTH);
        frame.add(tabs, BorderLayout.CENTER);
        frame.add(footer, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Simple placeholder panel
    private static JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(label);
        return panel;
    }
}
