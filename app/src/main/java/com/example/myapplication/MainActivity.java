package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Users.MapsActivity;
import com.example.myapplication.Users.MedicineLocator;
import com.example.myapplication.Users.MyPrescriptions;
import com.example.myapplication.Users.MyProfile;
import com.example.myapplication.Users.UserAppointmentsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Declare the buttons
    private Button myPrescriptionsButton;

    private Button myProfileButton;
    private Button medicineLocatorButton;
    private Button appointmentsButton;


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
        logoutButton = findViewById(R.id.logout_button);
        appointmentsButton=findViewById(R.id.Appointments_button);
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
                Intent intent = new Intent(MainActivity.this, MedicineLocator.class);
                startActivity(intent);
            }
        });
        appointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserAppointmentsActivity.class);
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
