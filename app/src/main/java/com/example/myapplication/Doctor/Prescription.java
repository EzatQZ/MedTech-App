package com.example.myapplication.Doctor;

import com.example.myapplication.Admin.Medicine;
import com.example.myapplication.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Prescription  {
    private User user;
    private List<Medicine> medicines;
    private Map<String, Integer> medicineQuantityMap;
    private double totalPrice;
    private long prescriptionDate;

    public Prescription() {
        // Default constructor required for Firebase
    }

    public Prescription(User user) {
        this.user = user;
        this.medicines = new ArrayList<>();
        this.medicineQuantityMap = new HashMap<>();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public void addMedicine(Medicine medicine) {
        this.medicines.add(medicine);
        incrementMedicineQuantity(medicine);
    }

    private void incrementMedicineQuantity(Medicine medicine) {
        String medicineName = medicine.getName();
        int quantity = medicineQuantityMap.getOrDefault(medicineName, 0);
        medicineQuantityMap.put(medicineName, quantity + 1);
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

    public Map<String, Integer> getMedicineQuantityMap() {
        return medicineQuantityMap;
    }

    public void setMedicineQuantityMap(Map<String, Integer> medicineQuantityMap) {
        this.medicineQuantityMap = medicineQuantityMap;
    }
}
