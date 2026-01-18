package com.jetbrains.uzair.model;

public class Patient {
    private int id;
    private String name;
    private int age;
    private String phone;
    private String gender;

    public Patient(int id, String name, int age, String phone, String gender) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.gender = gender;
    }

    public Patient(String name, int age, String phone, String gender) {
        this(-1, name, age, phone, gender);
    }

    // getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
}
