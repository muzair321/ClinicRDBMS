package com.jetbrains.uzair.app;

import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.model.ValidationException;
import com.jetbrains.uzair.ui.PatientPanel;

public class Main {
    public static void main(String[] args){
        String[] a = {"4", "Zara",  "16", "Female"};
        PatientDB.edit(Patient.check(Patient.convArrayToOb(a)));
        PatientDB.insert(Patient.check(Patient.convArrayToOb(a)));
        PatientPanel.mainWindow();
    }
}