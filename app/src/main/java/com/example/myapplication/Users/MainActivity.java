package com.example.myapplication.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Connecting.LoginActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Declare the buttons
    private Button myPrescriptionsButton;

    private Button myProfileButton;
    private Button MyAppointments;
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
        MyAppointments=findViewById(R.id.MyAppointments);
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

        appointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserAppointmentsActivity.class);
                startActivity(intent);
            }
        });
        MyAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChosenAppointments.class);
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
