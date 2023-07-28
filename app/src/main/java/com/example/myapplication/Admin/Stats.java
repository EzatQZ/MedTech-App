package com.example.myapplication.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Admin.Medicine;
import com.example.myapplication.Doctor.Prescription;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Call the method to fetch and display the top 5 prescribed medicines and total prescriptions released
        fetchTop5PrescribedMedicines();
    }

    private void fetchTop5PrescribedMedicines() {
        DatabaseReference prescriptionsRef = FirebaseDatabase.getInstance().getReference("Prescriptions");
        prescriptionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Calculate the total prescriptions released
                int totalPrescriptionsReleased = (int) dataSnapshot.getChildrenCount();

                // Display the total prescriptions released
                TextView totalPrescriptionsTextView = findViewById(R.id.total_prescriptions_text_view);
                totalPrescriptionsTextView.setText("Total Prescriptions Released: " + totalPrescriptionsReleased);

                // Create a map to count the occurrences of each medicine and get their quantities
                Map<String, Integer> medicineOccurrencesMap = new HashMap<>();

                // Loop through all prescriptions
                for (DataSnapshot prescriptionSnapshot : dataSnapshot.getChildren()) {
                    Prescription prescription = prescriptionSnapshot.getValue(Prescription.class);

                    if (prescription != null) {
                        Map<String, Integer> medicineQuantityMap = prescription.getMedicineQuantityMap();

                        // Loop through the medicines in the medicineQuantityMap
                        for (String medicineName : medicineQuantityMap.keySet()) {
                            int quantity = medicineQuantityMap.get(medicineName);
                            int currentCount = medicineOccurrencesMap.getOrDefault(medicineName, 0);
                            medicineOccurrencesMap.put(medicineName, currentCount + quantity);
                        }
                    }
                }

                // Get the top 5 prescribed medicines
                List<String> top5Medicines = getTop5Medicines(medicineOccurrencesMap);

                // Display the BarChart with the top 5 prescribed medicines
                displayTop5PrescribedMedicinesBarChart(top5Medicines, medicineOccurrencesMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if necessary
            }
        });
    }

    private List<String> getTop5Medicines(Map<String, Integer> medicineOccurrencesMap) {
        List<Map.Entry<String, Integer>> sortedMedicineList = new ArrayList<>(medicineOccurrencesMap.entrySet());

        // Custom sorting based on the occurrence count in descending order
        Collections.sort(sortedMedicineList, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Now, extract the names of the top 5 prescribed medicines
        List<String> top5Medicines = new ArrayList<>();
        for (int i = 0; i < Math.min(sortedMedicineList.size(), 5); i++) {
            top5Medicines.add(sortedMedicineList.get(i).getKey());
        }

        // Reverse the list to get the medicines in descending order
        Collections.reverse(top5Medicines);

        // Return the list of top 5 medicines
        return top5Medicines;
    }


    // Inside the displayTop5PrescribedMedicinesBarChart method
    private void displayTop5PrescribedMedicinesBarChart(List<String> top5Medicines, Map<String, Integer> medicineOccurrencesMap) {
        BarChart barChart = findViewById(R.id.bar_chart);

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < top5Medicines.size(); i++) {
            String medicineName = top5Medicines.get(i);
            int occurrences = medicineOccurrencesMap.get(medicineName);
            entries.add(new BarEntry(i, occurrences));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Top 5 Prescribed Medicines");

        // Customize the appearance of the bars
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);

        // Customize the appearance of the BarChart
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setValueFormatter(new BarValueFormatter(top5Medicines)); // Set the custom ValueFormatter for X-axis labels
        barChart.getXAxis().setTextSize(10f); // Set the text size for X-axis labels (you can adjust the value as needed)
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisRight().setEnabled(false);

        barChart.setData(barData);
        barChart.invalidate();
    }
}