package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.Database;
import com.jetbrains.uzair.db.VisitDB;
import com.jetbrains.uzair.model.Visit;
import com.jetbrains.uzair.ui.PatientPanel;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        try {
            String[][] data = VisitDB.returnUI();
            for(String[] rows: data){
                for (String comp: rows){
                    System.out.print(" " + comp);
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

//        PatientPanel.mainWindow();
    }
}