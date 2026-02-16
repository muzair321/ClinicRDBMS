package com.jetbrains.uzair.ui;

import javax.swing.*;
import java.awt.*;

public class ChartPanel {
    public static JPanel mainFrame(){
        JPanel frame = new JPanel(new BorderLayout());
        frame.setBounds(new Rectangle(1920, 785));

        frame.add(BarChartFX.start(), BorderLayout.NORTH );
        frame.add(PieCharts.mainFrame(), BorderLayout.CENTER);
        return frame;
    }
}
