package com.jetbrains.uzair.model;

import com.jetbrains.uzair.db.PatientDB;

public class Patient {
    //object parameters
    private int id;
    private String name;
    private int age;
    private String gender;
    //constructors
    public Patient(int id, String name, int age, String gender){
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
    public Patient(String name, int age, String gender){
        this(-1, name, age, gender);
    }
    //getters
    public int getId(){return id;}
    public int getAge(){return age;}
    public String getName(){return name;}
    public String getGender(){return gender;}
    //setters
    public void setAge(int age){this.age = age;}
    public void setName(String name){this.name = name;}
    public void setGender(String gender){this.gender = gender;}
    //checks for adding patient
    public static void addPatient(Patient p){
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
        PatientDB.insert(p);
    }
}
