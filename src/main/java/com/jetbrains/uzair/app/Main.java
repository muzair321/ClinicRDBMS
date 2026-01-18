package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.Database;
import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.model.Patient;

public class Main {
    public static void main(String[] args){
        Database.initializeDatabase();
        Database.createTables();
        Database.testConnection();
        Patient p = new Patient("Muhammad Bilal", 22, "09675676", "Male");
        PatientDB.addPatient(p);

    }
}