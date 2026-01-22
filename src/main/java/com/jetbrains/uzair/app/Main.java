package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.Database;
import com.jetbrains.uzair.ui.PatientPanel;

public class Main {
    public static void main(String[] args){
        Database.createTables();
        PatientPanel.mainWindow();
    }
}