package com.jetbrains.uzair.model;

public class VisitInventory {
    private int visitId;
    private int inventoryId;
    private int quantity;
    private double unitPrice;

    public VisitInventory(int visitId, int inventoryId, int quantity, double unitPrice){
        this.visitId = visitId;
        this.inventoryId = inventoryId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

}