package com.example.myapplication.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Admin.Admin;
import com.example.myapplication.Admin.MedicineActivity;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class doctor extends AppCompatActivity {

    private Button prescriptionButton;
    private Button patientInquiriesButton;
    private Button settingsButton;
    private Button logoutButton;

    private Button reviewsButton;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        // Initialize buttons
        prescriptionButton = findViewById(R.id.prescription_button);
        patientInquiriesButton = findViewById(R.id.patient_inquiries_button);
        settingsButton = findViewById(R.id.settings_button);
        logoutButton = findViewById(R.id.logout_button);
        reviewsButton=findViewById(R.id.reviews_button);
        mAuth = FirebaseAuth.getInstance();


        // Set click listeners for buttons
        prescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctor.this, DoctorPrescriptions.class);
                startActivity(intent);
            }
        });

        patientInquiriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctor.this, PatientInq.class);
                startActivity(intent);
            }
        });
        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctor.this, PatientReviews.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctor.this, DoctorSettings.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(doctor.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
