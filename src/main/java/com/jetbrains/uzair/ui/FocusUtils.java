package com.jetbrains.uzair.ui;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;

public class FocusUtils {

    public static void enableArrowNavigation(JComponent... components) {

        for (int i = 0; i < components.length; i++) {
            JComponent current = components[i];

            // DOWN → next
            if (i < components.length - 1) {
                JComponent next = components[i + 1];
                bind(current, "DOWN", next);
            }

            // UP → previous
            if (i > 0) {
                JComponent prev = components[i - 1];
                bind(current, "UP", prev);
            }
        }
    }

    private static void bind(JComponent from, String key, JComponent to) {
        from.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(key),
                "focusMove"
        );

        from.getActionMap().put("focusMove",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        // JComboBox special handling
                        if (from instanceof JComboBox<?>) {
                            JComboBox<?> combo = (JComboBox<?>) from;
                            if (combo.isPopupVisible()) {
                                return; // allow normal selection navigation
                            }
                        }

                        to.requestFocusInWindow();
                    }
                });
    }
}
