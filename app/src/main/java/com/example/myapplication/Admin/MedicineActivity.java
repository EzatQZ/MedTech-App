package com.example.myapplication.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Users.MyPrescriptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MedicineActivity extends AppCompatActivity {
    private EditText medicineNameInput;
    private EditText medicineQuantityInput;
    private EditText medicinePriceInput;

    private Button addMedicineButton,viewMedicineButton;
    private ProgressBar medicineProgressBar;

    private DatabaseReference medicineDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        // Initialize Firebase database reference
        medicineDatabase = FirebaseDatabase.getInstance().getReference().child("medicines");

        // Find views by ID
        medicineNameInput = findViewById(R.id.medicine_name_input);
        medicineQuantityInput = findViewById(R.id.medicine_quantity_input);
        medicinePriceInput = findViewById(R.id.medicine_price_input);
        addMedicineButton = findViewById(R.id.add_medicine_button);
        medicineProgressBar = findViewById(R.id.medicine_progress_bar);
        viewMedicineButton=findViewById(R.id.view_medicine_button);

        // Set click listener for add medicine button
        addMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedicineToFirebase();
            }
        });

        viewMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicineActivity.this, ViewMedicine.class);
                startActivity(intent);
            }
        });

    }

    // Add medicine to Firebase
    private void addMedicineToFirebase() {
        // Get input values
        String name = medicineNameInput.getText().toString().trim();
        String quantityString = medicineQuantityInput.getText().toString().trim();
        String priceString = medicinePriceInput.getText().toString().trim();


        // Validate input values
        if (TextUtils.isEmpty(name)) {
            medicineNameInput.setError("Medicine name is required");
            medicineNameInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(quantityString)) {
            medicineQuantityInput.setError("Quantity is required");
            medicineQuantityInput.requestFocus();
            return;
        }

        int quantity = Integer.parseInt(quantityString);
        if (quantity < 0) {
            medicineQuantityInput.setError("Invalid quantity");
            medicineQuantityInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(priceString)) {
            medicinePriceInput.setError("Price is required");
            medicinePriceInput.requestFocus();
            return;
        }

        double price = Double.parseDouble(priceString);
        if (price < 0) {
            medicinePriceInput.setError("Invalid price");
            medicinePriceInput.requestFocus();
            return;
        }

        // Check if medicine already exists by name
        Query query = medicineDatabase.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Medicine already exists, update quantity and price
                    for (DataSnapshot medicineSnapshot : snapshot.getChildren()) {
                        Medicine medicine = medicineSnapshot.getValue(Medicine.class);
                        medicine.setQuantity(quantity);
                        medicine.setPrice(price);

                        // Show progress bar
                        medicineProgressBar.setVisibility(View.VISIBLE);

                        // Update medicine in Firebase
                        medicineDatabase.child(medicineSnapshot.getKey()).setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Hide progress bar
                                medicineProgressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // Clear input fields
                                    medicineNameInput.setText("");
                                    medicineQuantityInput.setText("");
                                    medicinePriceInput.setText("");

                                    Toast.makeText(getApplicationContext(), "Medicine updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to update medicine. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    // Medicine doesn't exist, add new medicine
                    // Create Medicine object
                    Medicine medicine = new Medicine(name, quantity, price);

                    // Show progress bar
                    medicineProgressBar.setVisibility(View.VISIBLE);

                    // Generate unique key for medicine
                    String medicineId = medicineDatabase.push().getKey();

                    // Add medicine to Firebase
                    medicineDatabase.child(medicineId).setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Hide progress bar
                            medicineProgressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {
                                // Clear input fields
                                medicineNameInput.setText("");
                                medicineQuantityInput.setText("");
                                medicinePriceInput.setText("");

                                Toast.makeText(getApplicationContext(), "Medicine added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to add medicine. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hide progress bar
                medicineProgressBar.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Failed to add medicine. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }}