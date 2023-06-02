package com.example.myapplication.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorPrescriptions extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private DatabaseReference medicinesRef;

    private Spinner userSpinner;
    private ListView medicineListView;
    private Button addPrescriptionButton;

    private User selectedUser;
    private Map<String, Integer> selectedQuantities;
    private double totalPrice;
    private ArrayAdapter<Medicine> medicineAdapter;
    private Map<Medicine, Integer> selectedMedicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_prescriptions);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        medicinesRef = FirebaseDatabase.getInstance().getReference("medicines");

        userSpinner = findViewById(R.id.user_spinner);
        medicineListView = findViewById(R.id.medicine_list_view);
        addPrescriptionButton = findViewById(R.id.add_prescription_button);

        selectedQuantities = new HashMap<>();
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

        medicineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Medicine selectedMedicine = medicineAdapter.getItem(position);
                int quantity = selectedQuantities.get(selectedMedicine.getUid()) != null
                        ? selectedQuantities.get(selectedMedicine.getUid()) : 0;
                quantity++;
                selectedQuantities.put(selectedMedicine.getUid(), quantity);
                selectedMedicines.put(selectedMedicine, quantity);
                totalPrice += selectedMedicine.getPrice();
                updateMedicineList();
            }
        });

        addPrescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUser == null) {
                    Toast.makeText(DoctorPrescriptions.this, "Please select a user", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedMedicines.isEmpty()) {
                    Toast.makeText(DoctorPrescriptions.this, "Please select at least one medicine", Toast.LENGTH_SHORT).show();
                    return;
                }

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

                medicineAdapter = new ArrayAdapter<>(DoctorPrescriptions.this,
                        android.R.layout.simple_list_item_1, medicineList);
                medicineListView.setAdapter(medicineAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DoctorPrescriptions", "Error retrieving medicines: " + databaseError.getMessage());
            }
        });
    }

    private void updateMedicineList() {
        medicineAdapter.notifyDataSetChanged();
    }

    private void addPrescription() {
        Prescription prescription = new Prescription(selectedUser);

        for (Map.Entry<Medicine, Integer> entry : selectedMedicines.entrySet()) {
            Medicine medicine = entry.getKey();
            int quantity = entry.getValue();
            prescription.addMedicine(medicine, quantity);
        }

        prescription.setTotalPrice(totalPrice);
        prescription.setPrescriptionDate(new Date().getTime());

        DatabaseReference prescriptionsRef = usersRef.child(selectedUser.getId()).child("prescriptions").push();
        prescriptionsRef.setValue(prescription);

        Toast.makeText(DoctorPrescriptions.this, "Prescription added successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(DoctorPrescriptions.this, MyPrescriptions.class);
        intent.putExtra("userId", selectedUser.getId());
        startActivity(intent);
    }
}
