package com.example.myapplication.Admin;

import java.util.Date;

public class Medicine {
    private String name;
    private int quantity;
    private double price;
    private String uid;

    public Medicine() {
        // Default constructor required for calls to DataSnapshot.getValue(Medicine.class)
    }

    public Medicine(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;

    }

    // getters and setters



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return name; // Assuming 'name' is the field that represents the medicine name
    }

}
