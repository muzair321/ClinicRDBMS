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
    //constructors
    public Patient(){}

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
    public void setId(int id){this.id = id;}
    public void setAge(int age){this.age = age;}
    public void setName(String name){this.name = name;}
    public void setGender(String gender){this.gender = gender;}

    //for ui data to turn into datatypes accepted by db
    public static Patient convArrayToOb(String[] raw){
        Patient p = new Patient();
        int x = (Integer.parseInt(raw[0]));
        if( x > 0) {
            p.setId(x);
        }
        p.setName(raw[1]);
        p.setAge(Integer.parseInt(raw[2]));
        p.setGender(raw[3]);
        return p;
    }

    //checks for adding patient
    public static Patient check(Patient p) throws ValidationException{
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
        return p;
    }

    //checks of list of patients for deletion
    public static void checkListAndDelete (List<Integer> ids) throws ValidationException, SQLException {
        if(ids == null || ids.isEmpty()){
            throw new ValidationException("No Patients Selected");
        }
        for(int id: ids){
            if(id <= 0 ){
                throw new ValidationException("Invalid ID(s)");
            }
        }
        PatientDB.deleteList(ids);
    }

}
