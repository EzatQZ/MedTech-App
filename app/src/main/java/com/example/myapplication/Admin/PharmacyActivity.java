package com.example.myapplication.Admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PharmacyActivity extends AppCompatActivity {

    private EditText pharmacyNameEditText;
    private EditText pharmacyAddressEditText;
    private EditText pharmacyManagerEditText;
    private Spinner medicineSpinner;
    private ArrayAdapter<String> medicineAdapter;
    private ArrayList<Medicine> medicineList;
    private DatabaseReference pharmacyDatabase;
    private DatabaseReference medicineDatabase;
    private ArrayList<String> selectedMedicineNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        // Initialize database references
        pharmacyDatabase = FirebaseDatabase.getInstance().getReference("Pharmacies");
        medicineDatabase = FirebaseDatabase.getInstance().getReference("medicines");

        // Initialize UI elements
        pharmacyNameEditText = findViewById(R.id.pharmacy_name_edit_text);
        pharmacyAddressEditText = findViewById(R.id.pharmacy_address_edit_text);
        pharmacyManagerEditText = findViewById(R.id.pharmacy_manager_edit_text);
        medicineSpinner = findViewById(R.id.medicine_spinner);

        // Initialize medicine list and adapter
        medicineList = new ArrayList<>();
        selectedMedicineNames = new ArrayList<>();
        medicineAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        medicineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicineSpinner.setAdapter(medicineAdapter);

        // Set item selected listener to update selected medicine names
        medicineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedMedicine = medicineList.get(position).getName();
                if (!selectedMedicineNames.contains(selectedMedicine)) {
                    selectedMedicineNames.add(selectedMedicine);
                    Toast.makeText(PharmacyActivity.this, "Added " + selectedMedicine + " to selected medicines", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Get medicine list from database
        getMedicineList();

        // Set add pharmacy button click listener
        Button addPharmacyButton = findViewById(R.id.add_pharmacy_button);
        addPharmacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user inputs
                String pharmacyName = pharmacyNameEditText.getText().toString().trim();
                String pharmacyAddress = pharmacyAddressEditText.getText().toString().trim();
                String pharmacyManager = pharmacyManagerEditText.getText().toString().trim();

                // Validate user inputs
                if (TextUtils.isEmpty(pharmacyName)) {
                    Toast.makeText(PharmacyActivity.this, "Please enter a pharmacy name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pharmacyAddress)) {
                    Toast.makeText(PharmacyActivity.this, "Please enter a pharmacy address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pharmacyManager)) {
                    Toast.makeText(PharmacyActivity.this, "Please enter a pharmacy manager", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedMedicineNames.isEmpty()) {
                    Toast.makeText(PharmacyActivity.this, "Please select at least one medicine to add to inventory", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create pharmacy object
                String pharmacyId = pharmacyDatabase.push().getKey();
                Pharmacy pharmacy = new Pharmacy(pharmacyId, pharmacyName, pharmacyAddress, pharmacyManager);

                // Add selected medicines to the pharmacy's inventory
                pharmacy.addMedicinesToInventory(selectedMedicineNames);

                // Add pharmacy to database
                pharmacyDatabase.child(pharmacyId).setValue(pharmacy, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, DatabaseReference ref) {
                        if (error == null) {
                            // Display success message and clear form
                            Toast.makeText(PharmacyActivity.this, "Pharmacy added successfully", Toast.LENGTH_SHORT).show();
                            clearForm();
                        } else {
                            // Display error message
                            Toast.makeText(PharmacyActivity.this, "Error adding pharmacy: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void getMedicineList() {
        medicineDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicineList.clear();
                medicineAdapter.clear();

                for (DataSnapshot medicineSnapshot : snapshot.getChildren()) {
                    Medicine medicine = medicineSnapshot.getValue(Medicine.class);
                    medicineList.add(medicine);
                    medicineAdapter.add(medicine.getName());
                }

                if (medicineList.isEmpty()) {
                    Toast.makeText(PharmacyActivity.this, "No medicines found in database", Toast.LENGTH_SHORT).show();
                } else {
                    // Update selected medicine names to the first medicine in the list
                    selectedMedicineNames.clear();
                    String selectedMedicine = medicineList.get(0).getName();
                    selectedMedicineNames.add(selectedMedicine);
                    Toast.makeText(PharmacyActivity.this, "Added " + selectedMedicine + " to selected medicines", Toast.LENGTH_SHORT).show();
                    medicineSpinner.setSelection(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PharmacyActivity.this, "Failed to retrieve medicine list: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm() {
        pharmacyNameEditText.getText().clear();
        pharmacyAddressEditText.getText().clear();
        pharmacyManagerEditText.getText().clear();
        selectedMedicineNames.clear();
        medicineSpinner.setSelection(0);
    }
}
