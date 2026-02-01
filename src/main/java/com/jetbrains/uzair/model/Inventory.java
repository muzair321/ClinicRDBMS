package com.jetbrains.uzair.model;

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
}