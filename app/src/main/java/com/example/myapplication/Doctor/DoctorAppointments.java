package com.example.myapplication.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DoctorAppointments extends AppCompatActivity {

    private DatabaseReference appointmentsRef;
    private String currentDoctorId;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointments);

        // Initialize Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        appointmentsRef = database.getReference("Appointments"); // Reference to the "Appointments" branch

        // Get the currently logged-in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentDoctorId = currentUser.getUid();
        }

        // Find UI elements
        EditText dateEditText = findViewById(R.id.edit_text_date);
        EditText timeEditText = findViewById(R.id.edit_text_time);
        EditText nameEditText = findViewById(R.id.edit_text_name);
        Button confirmButton = findViewById(R.id.button_confirm);

        // Set click listener for the confirm button
        confirmButton.setOnClickListener(v -> {
            // Retrieve input values
            String date = dateEditText.getText().toString();
            String time = timeEditText.getText().toString();
            String name = nameEditText.getText().toString();

            // Validate input
            if (TextUtils.isEmpty(date)) {
                dateEditText.setError("Please enter a date");
                return;
            }

            if (TextUtils.isEmpty(time)) {
                timeEditText.setError("Please enter a time");
                return;
            }

            if (TextUtils.isEmpty(name)) {
                nameEditText.setError("Please enter a doctor name");
                return;
            }

            // Create Appointment object with an empty userID and appointmentID
            String appointmentId = appointmentsRef.push().getKey();
            if (appointmentId != null) {
                Appointment appointment = new Appointment(appointmentId, date, time, currentDoctorId, name, "");
                appointmentsRef.child(appointmentId).setValue(appointment)
                        .addOnSuccessListener(aVoid -> {
                            // Show success message or update UI
                            Toast.makeText(DoctorAppointments.this, "Appointment added successfully", Toast.LENGTH_SHORT).show();
                            dateEditText.setText("");
                            timeEditText.setText("");
                            nameEditText.setText("");
                        })
                        .addOnFailureListener(e -> {
                            // Show error message or handle failure
                            Toast.makeText(DoctorAppointments.this, "Failed to add appointment", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(DoctorAppointments.this, "Failed to generate appointment ID", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
