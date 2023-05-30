package com.example.myapplication;

public class User {
    private String email;
    private String id;
    private String fullName;
    private String password;

    public User() {}

    public User(String email, String id, String fullName, String password) {
        this.email = email;
        this.id = id;
        this.fullName = fullName;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return fullName + " - " + email;
    }
}

