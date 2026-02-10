package com.jetbrains.uzair.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class AboutDialog extends JDialog {

    public AboutDialog(JFrame parent) {
        super(parent, "About", true);
        setSize(420, 320);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel name = title("Muhammad Uzair");
        JLabel role = subtitle("Software Developer");

        panel.add(name);
        panel.add(Box.createVerticalStrut(4));
        panel.add(role);
        panel.add(Box.createVerticalStrut(15));

        panel.add(label("Email:"));
        panel.add(link("uzair@example.com", "mailto:uzair@example.com"));
        panel.add(Box.createVerticalStrut(10));

        panel.add(label("GitHub:"));
        panel.add(link("github.com/yourusername", "https://github.com/yourusername"));
        panel.add(Box.createVerticalStrut(10));

        panel.add(label("LinkedIn:"));
        panel.add(link("linkedin.com/in/yourprofile", "https://linkedin.com/in/yourprofile"));
        panel.add(Box.createVerticalStrut(15));

        panel.add(label("License:"));
        JTextArea license = new JTextArea(
                "This software is licensed for use by the client.\n" +
                        "Unauthorized distribution or modification is prohibited."
        );
        license.setEditable(false);
        license.setWrapStyleWord(true);
        license.setLineWrap(true);
        license.setOpaque(false);
        license.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        license.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        panel.add(license);
        panel.add(Box.createVerticalGlue());

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        close.setAlignmentX(Component.RIGHT_ALIGNMENT);

        panel.add(Box.createVerticalStrut(10));
        panel.add(close);

        add(panel);
    }

    private JLabel title(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        return lbl;
    }

    private JLabel subtitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(Color.GRAY);
        return lbl;
    }

    private JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return lbl;
    }

    private JLabel link(String text, String url) {
        JLabel lbl = new JLabel("<html><a href=''>" + text + "</a></html>");
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ignored) {}
            }
        });

        return lbl;
    }
}