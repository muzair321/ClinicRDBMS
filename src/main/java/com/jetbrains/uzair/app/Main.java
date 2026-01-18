package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.Database;
import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.model.Inventory;
import com.jetbrains.uzair.db.InventoryDB;

public class Main {
    public static void main(String[] args){
        Database.initializeDatabase();
        Database.createTables();
        Database.testConnection();
        Inventory i = new Inventory("Panadol", "dosage", 37, 100.9);
        InventoryDB.addInventory(i);
//        Patient p = new Patient("Muhammad Bilal", 22, "09675676", "Male");
//        PatientDB.addPatient(p);
    }
}