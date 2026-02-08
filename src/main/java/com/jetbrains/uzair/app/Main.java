package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.*;
import com.jetbrains.uzair.model.*;
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
//        for(int i = 0; i < 2000; i++){
//            Patient p = new Patient("Uzaor" + i, 20, "Male", null , null );
//                try{
//                    PatientDB.insert(p);
//                    for(int j = 0; j < 10; j++ ) {
//                        Visit v = new Visit(i+1, "pa" + j , null, j*100, null);
//                        VisitDB.insert(v);
//                    }
//                } catch (SQLException e) {
//                    JOptionPane.showMessageDialog(null, "Fked");
//                }
//        }
//        for(int i = 0; i < 100; i++){
//            Inventory in = new Inventory("augmentin" + i, "Bottles", 100, null);
//            try{
//                User user = UsersDB.authenticate("Name", "123");
//                UserSession.login(user);
//                InventoryDB.insert(in);
//                for(int j = 1; j < 10; j++){
//                    int amount;
//                    if( j % 2 == 0){
//                        amount = -2;
//                    }else {
//                        amount = +15;
//                    }
//                    assert user != null;
//                    InventoryLogs il = new InventoryLogs(i+1, UserSession.getUserId(), amount, null);
//                    InventoryLogsDB.insert(il);
//                }
//            }catch (SQLException e) {
//                JOptionPane.showMessageDialog(null, "Fked" + e.getMessage());
//            }
//        }
    }
}