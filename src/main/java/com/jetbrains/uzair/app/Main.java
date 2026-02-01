package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.Database;
import com.jetbrains.uzair.ui.MainFrame;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        Database.createTables();
        try {
            Database.addTriggers();
            Database.addIndexes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        MainFrame.main(args);
    }
}