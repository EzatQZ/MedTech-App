package com.example.myapplication.Admin;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Pharmacy {
    private String uid;
    private String name;
    private String address;
    private String manager;
    private double latitude;
    private double longitude;
    private List<String> inventory;

    public Pharmacy() {
        // Required empty constructor
    }

    public Pharmacy(String uid, String name, String address, String manager, double latitude, double longitude) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.manager = manager;
        this.latitude = latitude;
        this.longitude = longitude;
        this.inventory = new ArrayList<>();
    }

    public Pharmacy(String uid, String name, String address, String manager)
    {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.manager = manager;
        this.inventory = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    public void addMedicineToInventory(String medicineId) {
        inventory.add(medicineId);
    }

    public void addMedicinesToInventory(List<String> medicineIds) {
        inventory.addAll(medicineIds);
    }
}
