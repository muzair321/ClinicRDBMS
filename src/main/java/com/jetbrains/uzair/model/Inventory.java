package com.jetbrains.uzair.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Inventory{
    private int id;
    private String name;
    private String dosage;
    private int amount;
    private double unitPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Inventory(int id, String name, String dosage, int amount, double unitPrice, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.id = id;
        this.amount = amount;
        this.name = name;
        this.dosage = dosage;
        this.unitPrice = unitPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Inventory(String name, String dosage, int amount, double unitPrice) {
        this(-1, name, dosage, amount, unitPrice, null, null);
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public String getDosage() {return dosage;}
    public int getAmount() {return amount;}
    public double getUnitPrice() {return unitPrice;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public LocalDateTime getUpdatedAt() {return updatedAt;}

    public void setAmount(int amount) {this.amount = amount;}
    public void setUnitPrice(double unitPrice) {this.unitPrice = unitPrice;}
}