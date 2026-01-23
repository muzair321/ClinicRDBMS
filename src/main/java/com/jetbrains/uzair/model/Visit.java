package com.jetbrains.uzair.model;

public class Visit {
    private int id;
    private int patient_id;
    private String treatment;
    private int paid;
    private String date;

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

}