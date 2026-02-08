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

        String actionKey = "focusMove_" + key;

        InputMap im = from.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = from.getActionMap();

        // Use CTRL for JTextArea
        KeyStroke ks;
        if (from instanceof JTextArea) {
            ks = KeyStroke.getKeyStroke("ctrl " + key);
        } else {
            ks = KeyStroke.getKeyStroke(key);
        }

        im.put(ks, actionKey);
        am.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // JComboBox popup safety
                if (from instanceof JComboBox<?> combo && combo.isPopupVisible()) {
                    return;
                }

                to.requestFocusInWindow();
            }
        });
    }

}
