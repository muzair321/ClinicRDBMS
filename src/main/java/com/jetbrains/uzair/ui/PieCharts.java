package com.jetbrains.uzair.ui;

import javax.swing.*;
import java.awt.*;

public class PieCharts {
    public static JPanel mainFrame(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(GenderPieChartFX.start());
        panel.add(AgePieChartFX.start());
        return panel;
    }
}
