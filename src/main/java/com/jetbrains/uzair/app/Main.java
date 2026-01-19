package com.jetbrains.uzair.app;

import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.model.ValidationException;

public class Main {
    public static void main(String[] args){
        try {
            Patient.addPatient(new Patient("Muhammad Uzair", 20, "Me"));
        }catch (ValidationException e){
            System.err.println("Error: " + e.getMessage());
        }
    }
}