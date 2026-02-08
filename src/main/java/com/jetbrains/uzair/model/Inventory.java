package com.jetbrains.uzair.model;

import com.jetbrains.uzair.db.InventoryDB;

import java.sql.SQLException;

public class Inventory {
    //object parameters
    private int id;
    private String name;
    private String storage;
    private int amount;
    private String updatedAt;
    //constructors
    public Inventory(){};
    public Inventory(int id, String name, String storage, int amount, String updatedAt){
        this.id = id;
        this.name =  name;
        this.storage = storage;
        this.amount = amount;
        this.updatedAt = updatedAt;
    }
    public Inventory(String name, String storage, int amount, String updatedAt){
        this(-1, name, storage, amount, updatedAt);
    }
    //setters
    public void setId(int id){this.id = id;}
    public void setName(String name){this.name = name;}
    public void setStorage(String storage){this.storage = storage;}
    public void setAmount(int amount){this.amount = amount;}
    public void setUpdatedAt(String updatedAt){this.updatedAt = updatedAt;}
    //getters
    public int getId(){return id;}
    public String getName(){return name;}
    public String getStorage(){return storage;}
    public int getAmount(){return amount;}
    public String getUpdatedAt(){return updatedAt;}
    //convert array into object with checks
    public static Inventory convArrayToOb(String[] raw, boolean b) throws SQLException {
        Inventory i = new Inventory();
        int x = Integer.parseInt(raw[0]);
        if(x >= 0){
            i.setId(x);
        }
        if(raw[1].contains(" ")){
            throw new ValidationException("Validation  Error: No Spaces In Name");
        }
        if(raw[1].isEmpty()){
            throw new SQLException("Name Can Not Be Empty");
        }
        if(InventoryDB.checkName(raw[1]) && b){
            throw new ValidationException("Name Already Exists");
        }
        i.setName(raw[1]);
        if(raw[2].equals("Bottles") || raw[2].equals("Strips") || raw[2].equals("Tablets") || raw[2].equals("Tubes") || raw[2].equals("Powder Packs")) {
            i.setStorage(raw[2]);
        }else{
            throw new ValidationException("Validation Error: Contact Developer (Error 101)");
        }
        if(raw[3].isEmpty()){
            throw new SQLException("Amount Can Not Be Empty");
        }
        int y;
        try {
            y = Integer.parseInt(raw[3]);
        } catch (NumberFormatException e) {
            throw new SQLException("Amount Can Only Have Integer Numbers");
        }
        if(y < 0){
            throw new ValidationException("Amount Can Not Be Less Than '0'");
        }
        i.setAmount(y);
        i.setUpdatedAt(raw[4]);
        return i;
    }
}
