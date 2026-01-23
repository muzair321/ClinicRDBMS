package com.jetbrains.uzair.ui;

import java.awt.*;
import javax.swing.JTextField;

public class PlaceholderTextField extends JTextField {
    private String placeholder;

    public PlaceholderTextField(String placeholder) {
        this.placeholder = placeholder;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getText().isEmpty() && !isFocusOwner()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(150, 150, 150)); // light gray
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            g2.drawString(
                    placeholder,
                    getInsets().left + 5,
                    g.getFontMetrics().getAscent() + getInsets().top + 2
            );
            g2.dispose();
        }
    }
}