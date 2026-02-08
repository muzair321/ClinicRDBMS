package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.Database;
import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.model.Inventory;
import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.ui.FirstUser;
import com.jetbrains.uzair.ui.LoginFrame;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        if(!FileChecker.exists()) {
            FirstUser.frame();
            Database.createTables();
            try {
                Database.addTriggers();
                Database.addIndexes();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            new LoginFrame();
        }
//        for(int i = 0; i < 12000; i++){
//            Patient p = new Patient("Uzaor" + i, 20, "Male", null , null );
//                try{PatientDB.insert(p);} catch (SQLException e) {
//                    JOptionPane.showMessageDialog(null, "Fked");
//                }
//        }
    }
}