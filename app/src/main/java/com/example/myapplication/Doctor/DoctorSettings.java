package com.example.myapplication.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorSettings extends AppCompatActivity {

    private EditText doctorNameEditText;
    private EditText specialtyEditText;
    private Button saveButton;

    private DatabaseReference doctorsRef;
    private FirebaseUser loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_settings);

        doctorNameEditText = findViewById(R.id.doctor_name_edit_text);
        specialtyEditText = findViewById(R.id.specialty_edit_text);
        saveButton = findViewById(R.id.save_button);

        // Get the logged-in user
        loggedInUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get the reference to the doctors node in the database
        doctorsRef = FirebaseDatabase.getInstance().getReference("Doctor");

        // Retrieve the logged-in doctor's information from the database
        doctorsRef.child(loggedInUser.getEmail().replace(".", ",")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String doctorName = dataSnapshot.child("name").getValue(String.class);
                    String specialty = dataSnapshot.child("specialty").getValue(String.class);

                    // Set the retrieved values to the EditText fields
                    doctorNameEditText.setText(doctorName);
                    specialtyEditText.setText(specialty);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DoctorSettings.this, "Failed to retrieve doctor information", Toast.LENGTH_SHORT).show();
            }
        });

        // Save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the updated values from the EditText fields
                String updatedName = doctorNameEditText.getText().toString().trim();
                String updatedSpecialty = specialtyEditText.getText().toString().trim();

                // Update the name and specialty fields in the database for the logged-in doctor
                doctorsRef.child(loggedInUser.getEmail().replace(".", ",")).child("name").setValue(updatedName);
                doctorsRef.child(loggedInUser.getEmail().replace(".", ",")).child("specialty").setValue(updatedSpecialty);

                Toast.makeText(DoctorSettings.this, "Doctor information updated successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
