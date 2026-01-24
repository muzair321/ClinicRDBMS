package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.Database;
import com.jetbrains.uzair.db.VisitDB;
import com.jetbrains.uzair.model.Visit;
import com.jetbrains.uzair.ui.PatientPanel;
import com.jetbrains.uzair.ui.VisitPanel;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
//        VisitPanel.mainWindow();

        PatientPanel.mainWindow();
    }
}