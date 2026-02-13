package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.db.FolderCreation;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ChartPanel {
    public static JPanel mainFrame(){
        JPanel frame = new JPanel(new BorderLayout());
        String userHome = null;
        try {
            userHome = FolderCreation.createDirectories();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        String URL = userHome + "/clinicData.db";
        BarChartFX chart = new BarChartFX(URL);
        frame.add(chart, BorderLayout.CENTER);
        return frame;
    }
}
