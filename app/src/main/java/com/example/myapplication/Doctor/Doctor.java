package com.example.myapplication.Doctor;

public class Doctor {
    private String id;
    private String email;
    private String password;
    private String name;
    private String specialty;

    public Doctor() {
        // Default constructor required for Firebase Realtime Database
    }

    public Doctor(String id, String email, String password, String name, String specialty) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.specialty = specialty;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}


