package com.jetbrains.uzair.ui;

import javax.swing.*;
import java.awt.*;

public class PieCharts {
    public static JPanel mainFrame(int filter){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(GenderPieChartFX.start(filter));
        panel.add(AgePieChartFX.start(filter));
        return panel;
    }
}
