package com.jetbrains.uzair.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AmountRowRenderer extends DefaultTableCellRenderer {

    private final int amountColumn;

    public AmountRowRenderer(int amountColumn) {
        this.amountColumn = amountColumn;
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
    ) {
        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
        );

        // Default background
        if (!isSelected) {
            int amount = Integer.parseInt(
                    table.getModel().getValueAt(row, amountColumn).toString()
            );

            if (amount > 0) {
                c.setBackground(new Color(220, 245, 220)); // light green
            } else if (amount < 0) {
                c.setBackground(new Color(245, 220, 220)); // light red
            } else {
                c.setBackground(Color.WHITE);
            }
            if (column == amountColumn) {
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            }
        }

        return c;
    }
}
