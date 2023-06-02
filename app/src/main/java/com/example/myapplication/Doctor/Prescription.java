package com.example.myapplication.Doctor;

import com.example.myapplication.Admin.Medicine;
import com.example.myapplication.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prescription {
    private User user;
    private List<Medicine> selectedMedicines;
    private Map<String, Integer> selectedQuantities;
    private double totalPrice;
    private long prescriptionDate;

    public Prescription(User user) {
        this.user = user;
        selectedMedicines = new ArrayList<>();
        selectedQuantities = new HashMap<>();
    }

    public User getUser() {
        return user;
    }

    public List<Medicine> getSelectedMedicines() {
        return selectedMedicines;
    }

    public void addMedicine(Medicine medicine, int quantity) {
        selectedMedicines.add(medicine);
        selectedQuantities.put(medicine.getUid(), quantity);
    }

    public Map<String, Integer> getSelectedQuantities() {
        return selectedQuantities;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(long prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }
}


