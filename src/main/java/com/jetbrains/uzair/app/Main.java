package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.Database;
import com.jetbrains.uzair.ui.LoginFrame;
import com.jetbrains.uzair.ui.MainFrame;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}