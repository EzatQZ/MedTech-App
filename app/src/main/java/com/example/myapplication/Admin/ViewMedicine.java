package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewMedicine extends AppCompatActivity {

    private ListView medicineListView;
    private DatabaseReference medicineDatabase;
    private List<Medicine> medicineList;
    private MedicineAdapter medicineAdapter;
    private TextView emptyListView;
    private ProgressBar medicineProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicine);

        // Initialize Firebase database reference
        medicineDatabase = FirebaseDatabase.getInstance().getReference().child("medicines");

        // Find views by ID
        medicineListView = findViewById(R.id.medicine_list_view);
        emptyListView = findViewById(R.id.empty_list_text_view);
        medicineProgressBar = findViewById(R.id.medicine_progress_bar);

        // Initialize medicine list and adapter
        medicineList = new ArrayList<>();
        medicineAdapter = new MedicineAdapter((Context) this, (List<Medicine>) medicineList, medicineDatabase); // pass the medicineDatabase reference
        medicineListView.setAdapter(medicineAdapter);

        // Retrieve medicines from Firebase
        retrieveMedicinesFromFirebase();
    }
    private void retrieveMedicinesFromFirebase() {
        // Show progress bar
        medicineProgressBar.setVisibility(View.VISIBLE);

        // Add value event listener for medicines node
        medicineDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear medicine list
                medicineList.clear();

                // Add medicines to medicine list
                for (DataSnapshot medicineSnapshot : dataSnapshot.getChildren()) {
                    Medicine medicine = medicineSnapshot.getValue(Medicine.class);
                    medicine.setUid(medicineSnapshot.getKey()); // set the uid field to the Firebase key
                    medicineList.add(medicine);
                }

                // Hide progress bar
                medicineProgressBar.setVisibility(View.GONE);

                // Notify adapter that data set has changed
                medicineAdapter.notifyDataSetChanged();

                // Show empty list text view if list is empty
                if (medicineList.isEmpty()) {
                    emptyListView.setVisibility(View.VISIBLE);
                } else {
                    emptyListView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hide progress bar
                medicineProgressBar.setVisibility(View.GONE);

                // Show error message
                Toast.makeText(ViewMedicine.this, "Failed to retrieve medicines. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
