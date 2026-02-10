package com.jetbrains.uzair.ui;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class AboutDialog extends JDialog {

    public AboutDialog(JFrame parent) {
        super(parent, "About", true);
        setSize(420, 320);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Clinic Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(12));

        panel.add(info("Developer: ", "Muhammad Uzair"));
        panel.add(info("Email: ", "muzairasim323@gmail.com"));
        panel.add(link("GitHub: ", "https://github.com/muzair321"));
        panel.add(link("LinkedIn: ", "https://www.linkedin.com/in/muhammad-uzair-80a27b384/"));

        panel.add(Box.createVerticalStrut(16));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(12));

        JTextArea license = new JTextArea(
                "© 2026 Muhammad Uzair\n" +
                        "Licensed for internal clinical use under Changez Clinic.\n" +
                        "Unauthorized distribution or any other form of copyright infringement is prohibited."
        );
        license.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        license.setEditable(false);
        license.setOpaque(false);
        license.setFocusable(false);
        license.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(license);

        add(panel);
    }

    private JPanel info(String label, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        p.add(l, BorderLayout.WEST);
        p.add(v, BorderLayout.CENTER);
        return p;
    }

    private JPanel link(String label, String url) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel link = new JLabel("<html><a href=''>" + url + "</a></html>");
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        link.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ignored) {}
            }
        });

        p.add(l, BorderLayout.WEST);
        p.add(link, BorderLayout.CENTER);
        return p;
    }
}
