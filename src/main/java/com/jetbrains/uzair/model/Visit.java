package com.jetbrains.uzair.model;

import java.time.LocalDateTime;

public class Visit{
    private int id;
    private int patientId;
    private String treatment;
    private String notes;
    private int paidAmount;
    private int totalAmount;
    private LocalDateTime visitDate;

    public Visit(int id, int patientId, String treatment, String notes,
                 int paidAmount, int totalAmount, LocalDateTime visitDate){
        this.id = id;
        this.patientId = patientId;
        this.treatment = treatment;
        this.notes = notes;
        this.paidAmount = paidAmount;
        this.totalAmount = totalAmount;
        this.visitDate = visitDate;
    }
    public Visit(int patientId, String treatment, String notes){
        this(-1, patientId, treatment, notes, 0, 0, LocalDateTime.now());
    }
    public int getId(){ return id; }
    public int getPatientId(){ return patientId;}
    public String getTreatment(){ return treatment;}
    public String getNotes(){ return notes;}
    public int getPaidAmount(){ return paidAmount;}
    public int getTotalAmount(){ return totalAmount;}
    public LocalDateTime getVisitDate(){ return visitDate;}
}