package com.jetbrains.uzair.ui;

import javax.swing.*;
import java.awt.*;

public class ChartPanel {
    public static JPanel mainFrame(){
        JPanel frame = new JPanel(new BorderLayout());
        AnalyticsChartPanel chart = new AnalyticsChartPanel();
        frame.add(chart, BorderLayout.CENTER);
        return frame;
    }
}
