package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.*;
import com.jetbrains.uzair.ui.FXInitializer;
import com.jetbrains.uzair.ui.FirstUser;
import com.jetbrains.uzair.ui.LoginFrame;
import javafx.application.Platform;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        if(!FileChecker.exists()) {
            FirstUser.frame();
            try {
                Database.createTables();
                Database.addTriggers();
                Database.addIndexes();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            Platform.setImplicitExit(false);
            new FXInitializer();
            SwingUtilities.invokeLater(LoginFrame::new);}
    }

}