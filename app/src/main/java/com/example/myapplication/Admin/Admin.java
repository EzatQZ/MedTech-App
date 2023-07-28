package com.example.myapplication.Admin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Connecting.LoginActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class Admin extends AppCompatActivity {

    // Declare the buttons
    private Button medicineButton;
    private Button pharmacyButton;
    private Button usersButton;
    private Button logoutButton;
    private Button statisticsButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();

        // Initialize the buttons
        medicineButton = findViewById(R.id.medicine_button);
        pharmacyButton = findViewById(R.id.pharmacy_button);
        usersButton = findViewById(R.id.users_button);
        logoutButton = findViewById(R.id.logout_button);
        statisticsButton=findViewById(R.id.statistics_button);

        // Set click listeners for each button
        medicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, MedicineActivity.class);
                startActivity(intent);
            }
        });

        pharmacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, PharmacyActivity.class);
                startActivity(intent);
            }
        });

        usersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, UsersActivity.class);
                startActivity(intent);
            }
        });
        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, Stats.class);
                startActivity(intent);
            }
        });



        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(Admin.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

