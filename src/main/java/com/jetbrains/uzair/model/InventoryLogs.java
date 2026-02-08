package com.jetbrains.uzair.model;

import com.jetbrains.uzair.db.UsersDB;

import java.sql.SQLException;

public class InventoryLogs {
    private int id;
    private int inventoryId;
    private int userId;
    private int amount;
    private String date;
    //constructors
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
    //setters
    public void setId(int id){this.id = id;}
    public void setInventoryId(int inventoryId){this.inventoryId = inventoryId;}
    public void setUserId(int userId){this.userId = userId;}
    public void setAmount(int amount){this.amount = amount;}
    public void setDate(String date){this.date = date;}
    //getters
    public int getId(){return id;}
    public int getInventoryId(){return inventoryId;}
    public int getUserId(){return userId;}
    public int getAmount(){return amount;}
    public String getDate(){return date;}

    public String getUserName() throws SQLException {
        return UsersDB.getName(this.userId);
    }
}
