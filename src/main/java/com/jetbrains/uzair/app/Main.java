package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.model.ValidationException;

public class Main {
    public static void main(String[] args){
        try {
            Patient.addPatient(new Patient("Lisa", 26, "Female"));
        }catch (ValidationException e){
            System.err.println("Error: " + e.getMessage());
        }
        String[][] print = PatientDB.returnUI();
        for(String[] sets: print){
            for(String attrs: sets){
                System.out.print(attrs + "  ");
            }
            System.out.println();
        }
    }
}