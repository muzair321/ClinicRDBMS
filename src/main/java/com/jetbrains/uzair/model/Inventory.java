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
    //convert array into object 
    public static Inventory convArrayToOb(String[] raw){
        Inventory i = new Inventory();
        int x = Integer.parseInt(raw[0]);
        if(x >= 0){
            i.setId(x);
        }
    }
}
