package com.jetbrains.uzair.model;

public class Visit {
    private int id;
    private int patient_id;
    private String treatment;
    private int paid;
    private String date;
    //constructors
    public Visit(){}
    public Visit(int id, int patient_id, String treatment, int paid, String date){
        this.id = id;
        this.patient_id = patient_id;
        this.treatment = treatment;
        this.paid = paid;
        this.date = date;
    }
    public Visit(int patient_id, String treatment, int paid, String date){
        this(-1, patient_id, treatment, paid, date);
    }
    //getters
    public int getId(){return id;}
    public int getPatient_id(){return patient_id;}
    public int getPaid(){return paid;}
    public String getTreatment(){return treatment;}
    public String getDate(){return date;}
    //setters
    public void setId(int id) {this.id = id;}
    public void setPatient_id(int patient_id) {this.patient_id = patient_id;}
    public void setDate(String date) {this.date = date;}
    public void setPaid(int paid) {this.paid = paid;}
    public void setTreatment(String treatment) {this.treatment = treatment;}
    //for ui input
    p
}