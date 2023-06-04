package com.example.myapplication.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.Doctor.Prescription;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyPrescriptions extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference prescriptionsRef;

    private ListView prescriptionsListView;
    private ArrayAdapter<Prescription> prescriptionAdapter;
    private List<Prescription> prescriptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_prescriptions);

        mAuth = FirebaseAuth.getInstance();
        prescriptionsRef = FirebaseDatabase.getInstance().getReference("Prescriptions");

        prescriptionsListView = findViewById(R.id.prescriptions_list_view);
        prescriptionList = new ArrayList<>();
        prescriptionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, prescriptionList);
        prescriptionsListView.setAdapter(prescriptionAdapter);

        loadPrescriptions();

        prescriptionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Prescription selectedPrescription = prescriptionList.get(position);
                Map<String, Integer> medicineQuantityMap = selectedPrescription.getMedicineQuantityMap();
                double totalPrice = selectedPrescription.getTotalPrice();

                // Create a list to store the medicine details
                ArrayList<String> medicineList = new ArrayList<>();

                // Iterate over the medicine quantity map to get the details
                for (Map.Entry<String, Integer> entry : medicineQuantityMap.entrySet()) {
                    String medicineName = entry.getKey();
                    int quantity = entry.getValue();
                    String details = "Medicine: " + medicineName + ", Quantity: " + quantity;
                    medicineList.add(details);
                }

                // Launch the PrescriptionDetailsActivity and pass the prescription details
                Intent intent = new Intent(MyPrescriptions.this, PrescriptionDetails.class);
                intent.putStringArrayListExtra("medicineList", medicineList);
                intent.putExtra("totalPrice", totalPrice);
                startActivity(intent);
            }
        });

    }

    private void loadPrescriptions() {
        DatabaseReference prescriptionsRef = FirebaseDatabase.getInstance().getReference("Prescriptions");
        String currentUserEmail = mAuth.getCurrentUser().getEmail(); // Get the current user's email

        prescriptionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                prescriptionList.clear();

                for (DataSnapshot prescriptionSnapshot : dataSnapshot.getChildren()) {
                    Prescription prescription = prescriptionSnapshot.getValue(Prescription.class);
                    String prescriptionUserEmail = prescription.getUser().getEmail();

                    if (prescriptionUserEmail.equals(currentUserEmail)) {
                        prescriptionList.add(prescription);
                    }
                }

                prescriptionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("LoadPrescriptions", "Error: " + databaseError.getMessage());
            }
        });
    }


}

