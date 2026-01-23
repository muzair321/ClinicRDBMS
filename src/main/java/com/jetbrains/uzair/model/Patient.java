package com.jetbrains.uzair.model;

import com.jetbrains.uzair.db.PatientDB;

import java.sql.SQLException;
import java.util.List;

public class Patient {
    //object parameters
    private int id;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private String createdAt;
    //constructors
    public Patient(){}

    public Patient(int id, String name, int age, String gender, String phone, String createdAt){
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public Patient(String name, int age, String gender, String phone, String createdAt){
        this(-1, name, age, gender, phone, createdAt);
    }

    //getters
    public int getId(){return id;}
    public int getAge(){return age;}
    public String getName(){return name;}
    public String getGender(){return gender;}
    public String getPhone(){return phone;}
    public String getCreatedAt(){return createdAt;}
    //setters
    public void setId(int id){this.id = id;}
    public void setAge(int age){this.age = age;}
    public void setName(String name){this.name = name;}
    public void setGender(String gender){this.gender = gender;}
    public void setPhone(String phone){this.phone = phone;}
    public void setCreatedAt(String createdAt){this.createdAt = createdAt;}

    //for ui data to turn into datatypes accepted by db
    public static Patient convArrayToOb(String[] raw){
        Patient p = new Patient();
        int x = (Integer.parseInt(raw[0]));
        if( x > 0) {
            p.setId(x);
        }
        p.setName(raw[1]);
        try {
            p.setAge(Integer.parseInt(raw[2]));
        } catch (NumberFormatException e) {
            throw new ValidationException("Enter Only Numbers");
        }
        p.setGender(raw[3]);
        p.setPhone(raw[4]);
        return p;
    }

    //checks for adding patient
    public static Patient check(Patient p, boolean b) throws ValidationException{
        if(p.getName().isEmpty()){
            throw new ValidationException("Name Cannot Be Null");
        }
        if(p.getAge() < 0 ){
            throw new ValidationException("Age Cannot Be Negative");
        }
        if(p.getGender().isEmpty()){
            throw new ValidationException("Gender Cannot Be Null");
        }
        if(!p.getGender().equals("Male") && !p.getGender().equals("Female") && !p.getGender().equals("Other")){
            throw new ValidationException("Gender Can Only Be: 'Male', 'Female', 'Other'");
        }
        if(b) {
            if (!PatientDB.checkPhone(p.getPhone())) {
                throw new ValidationException("Phone Number Already Exists In Records");
            }
        }
        if(!p.getPhone().isEmpty()) {
            if((p.getPhone().length() < 7)){
                throw new ValidationException("Phone Number Has To Be Longer Than 7");
            }
            if(!p.getPhone().matches("^[0-9-]+$")){
                throw new ValidationException("Phone Number Can Only Contain Dashes And Numbers");
            }
        }
        return p;
    }
}
