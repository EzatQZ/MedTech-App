package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Users.ContactDoctor;
import com.example.myapplication.Users.MapsActivity;
import com.example.myapplication.Users.MyPrescriptions;
import com.example.myapplication.Users.MyProfile;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Declare the buttons
    private Button myPrescriptionsButton;

    private Button myProfileButton;
    private Button medicineLocatorButton;
    private Button contactADoctorButton;
    private Button logoutButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Initialize the buttons
        myPrescriptionsButton = findViewById(R.id.my_prescriptions_button);
        myProfileButton = findViewById(R.id.my_profile_button);
        medicineLocatorButton = findViewById(R.id.medicine_locator_button);
        contactADoctorButton = findViewById(R.id.contact_a_doctor_button);
        logoutButton = findViewById(R.id.logout_button);

        // Set click listeners for each button
        myPrescriptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyPrescriptions.class);
                startActivity(intent);
            }
        });



        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyProfile.class);
                startActivity(intent);
            }
        });

        medicineLocatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        contactADoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContactDoctor.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
