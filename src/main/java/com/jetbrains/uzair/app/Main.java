package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.Database;
import com.jetbrains.uzair.db.InventoryDB;
import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.db.VisitDB;
import com.jetbrains.uzair.model.Inventory;
import com.jetbrains.uzair.model.InventoryLogs;
import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.model.Visit;
import com.jetbrains.uzair.ui.FirstUser;
import com.jetbrains.uzair.ui.LoginFrame;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
//        if(!FileChecker.exists()) {
//            FirstUser.frame();
//            Database.createTables();
//            try {
//                Database.addTriggers();
//                Database.addIndexes();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }else {
//            new LoginFrame();
//        }
        for(int i = 0; i < 2000; i++){
            Patient p = new Patient("Uzaor" + i, 20, "Male", null , null );
                try{
                    PatientDB.insert(p);
                    for(int j = 0; j < 10; j++ ) {
                        Visit v = new Visit(i+1, "pa" + j , null, j*100, null);
                        VisitDB.insert(v);
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Fked");
                }
        }
        for(int i = 0; i < 100; i++){
            Inventory in = new Inventory("panadolx" + i, "Bottles", i, null);
            try{
                InventoryDB.insert(in);
                for(int j = 0; j < 1000; j++){
                    int amount = 0;
                    if( j % 2 == 0){
                        amount = -5;
                    }else {
                        amount = +15;
                    }
                    InventoryLogs il = new InventoryLogs(i+1, 1, amount, null);
                }
            }catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Fked");
            }
        }
    }
}