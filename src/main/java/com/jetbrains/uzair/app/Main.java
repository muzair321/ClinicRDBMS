package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.*;
import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.model.Visit;
import com.jetbrains.uzair.ui.FirstUser;
import com.jetbrains.uzair.ui.LoginFrame;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        if(!FileChecker.exists()) {
            FirstUser.frame();
            try {
                Database.createTables();
                Database.addTriggers();
                Database.addIndexes();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {new LoginFrame();}
    }

}