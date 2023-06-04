package com.example.myapplication.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Admin.Medicine;
import com.example.myapplication.R;
import com.example.myapplication.User;
import com.example.myapplication.Users.MyPrescriptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DoctorPrescriptions extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private DatabaseReference medicinesRef;

    private Spinner userSpinner;
    private Spinner medicineSpinner;
    private EditText quantityEditText;
    private ListView medicineListView;
    private Button addMedicineButton;
    private Button addPrescriptionButton;
    private TextView totalPriceTextView;

    private User selectedUser;
    private ArrayAdapter<Medicine> medicineAdapter;
    private Map<Medicine, Integer> selectedMedicines;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_prescriptions);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        medicinesRef = FirebaseDatabase.getInstance().getReference("medicines");

        userSpinner = findViewById(R.id.user_spinner);
        medicineSpinner = findViewById(R.id.medicine_spinner);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        medicineListView = findViewById(R.id.medicine_list_view);
        addMedicineButton = findViewById(R.id.add_medicine_button);
        addPrescriptionButton = findViewById(R.id.add_prescription_button);
        totalPriceTextView = findViewById(R.id.total_price_text_view);

        selectedMedicines = new HashMap<>();

        loadUsers();
        loadMedicines();

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUser = (User) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedUser = null;
            }
        });

        addMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMedicine();
            }
        });

        addPrescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPrescription();
            }
        });
    }

    private void loadUsers() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    userList.add(user);
                }

                ArrayAdapter<User> userAdapter = new ArrayAdapter<>(DoctorPrescriptions.this,
                        android.R.layout.simple_spinner_item, userList);
                userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userSpinner.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DoctorPrescriptions", "Error retrieving users: " + databaseError.getMessage());
            }
        });
    }

    private void loadMedicines() {
        medicinesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Medicine> medicineList = new ArrayList<>();

                for (DataSnapshot medicineSnapshot : dataSnapshot.getChildren()) {
                    Medicine medicine = medicineSnapshot.getValue(Medicine.class);
                    medicineList.add(medicine);
                }

                // Sort the medicine list by name
                Collections.sort(medicineList, (m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));

                medicineAdapter = new ArrayAdapter<>(DoctorPrescriptions.this,
                        android.R.layout.simple_spinner_item, medicineList);
                medicineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                medicineSpinner.setAdapter(medicineAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DoctorPrescriptions", "Error retrieving medicines: " + databaseError.getMessage());
            }
        });
    }

    private void addMedicine() {
        Medicine selectedMedicine = (Medicine) medicineSpinner.getSelectedItem();
        String quantityStr = quantityEditText.getText().toString().trim();

        if (selectedMedicine == null || quantityStr.isEmpty()) {
            Toast.makeText(DoctorPrescriptions.this, "Please select a medicine and enter a quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityStr);
        if (quantity <= 0) {
            Toast.makeText(DoctorPrescriptions.this, "Quantity must be greater than zero", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedMedicines.put(selectedMedicine, quantity);
        updateMedicineList();

        // Clear the input fields
        medicineSpinner.setSelection(0);
        quantityEditText.setText("");
    }

    private void updateMedicineList() {
        List<String> medicineInfoList = new ArrayList<>();
        double totalPrice = 0;

        for (Map.Entry<Medicine, Integer> entry : selectedMedicines.entrySet()) {
            Medicine medicine = entry.getKey();
            int quantity = entry.getValue();
            double medicinePrice = medicine.getPrice();

            medicineInfoList.add(medicine.getName() + " x " + quantity);
            totalPrice += (medicinePrice * quantity);
        }

        ArrayAdapter<String> medicineInfoAdapter = new ArrayAdapter<>(DoctorPrescriptions.this,
                android.R.layout.simple_list_item_1, medicineInfoList);
        medicineListView.setAdapter(medicineInfoAdapter);

        totalPriceTextView.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));
    }

    private void addPrescription() {
        if (selectedUser == null) {
            Toast.makeText(DoctorPrescriptions.this, "Please select a user", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedMedicines.isEmpty()) {
            Toast.makeText(DoctorPrescriptions.this, "Please add at least one medicine", Toast.LENGTH_SHORT).show();
            return;
        }

        Prescription prescription = new Prescription(selectedUser);

        // Add the selected medicines to the prescription and count their quantities
        Map<String, Integer> medicineQuantityMap = new HashMap<>();
        for (Map.Entry<Medicine, Integer> entry : selectedMedicines.entrySet()) {
            Medicine medicine = entry.getKey();
            int quantity = entry.getValue();

            prescription.addMedicine(medicine);
            medicineQuantityMap.put(medicine.getName(), medicineQuantityMap.getOrDefault(medicine.getName(), 0) + quantity);
        }

        prescription.setMedicineQuantityMap(medicineQuantityMap);

        double totalPrice = Double.parseDouble(totalPriceTextView.getText().toString());
        prescription.setTotalPrice(totalPrice);

        // Set the prescription date in "yyyy-MM-dd" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        long currentTimeMillis = 0;

        try {
            Date date = dateFormat.parse(currentDate);
            currentTimeMillis = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        prescription.setPrescriptionDate(currentTimeMillis);

        DatabaseReference prescriptionsRef = FirebaseDatabase.getInstance().getReference("Prescriptions").push();
        prescriptionsRef.setValue(prescription);

        // Display the quantity for each medicine prescribed
        for (Map.Entry<String, Integer> entry : medicineQuantityMap.entrySet()) {
            String medicineName = entry.getKey();
            int quantity = entry.getValue();
            Toast.makeText(DoctorPrescriptions.this, "Prescribed quantity of " + medicineName + ": " + quantity, Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(DoctorPrescriptions.this, "Prescription added successfully", Toast.LENGTH_SHORT).show();

        // Clear the selected medicines
        selectedMedicines.clear();
        updateMedicineList();

        // Clear the input fields
        userSpinner.setSelection(0);
        medicineSpinner.setSelection(0);
        quantityEditText.setText("");
    }
}
