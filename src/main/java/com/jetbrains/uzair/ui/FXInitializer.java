package com.jetbrains.uzair.ui;

import javafx.embed.swing.JFXPanel;

public class FXInitializer {
    static {
        new JFXPanel(); // initialize JavaFX toolkit once
    }
}