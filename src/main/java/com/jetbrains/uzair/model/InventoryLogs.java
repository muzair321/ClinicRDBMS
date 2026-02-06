package com.jetbrains.uzair.model;

import com.jetbrains.uzair.db.InventoryLogsDB;

public class InventoryLogs {
    private int id;
    private int inventoryId;
    private int userId;
    private int amount;
    private String date;

    public InventoryLogs(){}
    public InventoryLogs(int id, int inventoryId, int userId, int amount, String date){
        this.id = id;
        this.inventoryId = inventoryId;
        this.userId = userId;
        this.amount = amount;
        this.date = date;
    }
    public InventoryLogs(int inventoryId, int userId, int amount, String date){
        this(-1, inventoryId, userId, amount, date);
    }
}
