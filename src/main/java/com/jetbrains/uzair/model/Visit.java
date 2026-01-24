package com.jetbrains.uzair.model;

import com.jetbrains.uzair.db.PatientDB;

public class Visit {
    private int id;
    private int patientId;
    private String treatment;
    private int paid;
    private String date;
    //constructors
    public Visit(){}
    public Visit(int id, int patientId, String treatment, int paid, String date){
        this.id = id;
        this.patientId = patientId;
        this.treatment = treatment;
        this.paid = paid;
        this.date = date;
    }
    public Visit(int patient_id, String treatment, int paid, String date){
        this(-1, patient_id, treatment, paid, date);
    }
    //getters
    public int getId(){return id;}
    public int getPatientId(){return patientId;}
    public int getPaid(){return paid;}
    public String getTreatment(){return treatment;}
    public String getDate(){return date;}
    //setters
    public void setId(int id) {this.id = id;}
    public void setPatientId(int patient_id) {this.patientId = patient_id;}
    public void setDate(String date) {this.date = date;}
    public void setPaid(int paid) {this.paid = paid;}
    public void setTreatment(String treatment) {this.treatment = treatment;}
    //for ui input
    public static Visit convArrayToOb(String[] raw){
        Visit v = new Visit();
        int x = Integer.parseInt(raw[0]);
        if( x > 0){
            v.setId(x);
        }
        v.setPatientId(Integer.parseInt(raw[1]));
        v.setTreatment(raw[2]);
        try {
            v.setPaid(Integer.parseInt(raw[3]));
        } catch (NumberFormatException e) {
            throw new ValidationException("Enter Numbers Only In Paid");
        }
        return v;
    }
    public static Visit check(Visit v) throws ValidationException{
        if(v.getPaid() < 0){
            throw new ValidationException("Payment Can Not Be Negative");
        }
        if(v.getPatientId() <= 0){
            throw new ValidationException("ERROR: Patient ID Is Invalidated Contact Support");
        }
        if(!PatientDB.exists(v.getPatientId())){
            throw new ValidationException("ERROR: Patient Does Not Exist Contact Support");
        }
        return v;
    }
}