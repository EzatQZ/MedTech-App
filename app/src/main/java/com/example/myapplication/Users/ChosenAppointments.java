package com.example.myapplication.Users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.Doctor.Appointment;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChosenAppointments extends AppCompatActivity implements ChosenAppointmentsAdapter.OnCancelAppointmentClickListener {

    private RecyclerView chosenAppointmentsRecyclerView;
    private ChosenAppointmentsAdapter chosenAppointmentsAdapter;
    private List<Appointment> chosenAppointments;

    private DatabaseReference appointmentsRef;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_appointments);

        // Get the current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }

        // Initialize Firebase Database reference
        appointmentsRef = FirebaseDatabase.getInstance().getReference("Appointments");

        // Initialize chosen appointments list and adapter
        chosenAppointments = new ArrayList<>();
        chosenAppointmentsAdapter = new ChosenAppointmentsAdapter(chosenAppointments, this, this);

        // Configure the RecyclerView
        chosenAppointmentsRecyclerView = findViewById(R.id.chosen_appointments_recycler_view);
        chosenAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chosenAppointmentsRecyclerView.setAdapter(chosenAppointmentsAdapter);

        // Retrieve the chosen appointments from Firebase
        retrieveChosenAppointmentsFromFirebase();
    }

    private void retrieveChosenAppointmentsFromFirebase() {
        appointmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chosenAppointments.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if (appointment != null && appointment.isChosen() && appointment.getUserID().equals(currentUserId)) {
                        chosenAppointments.add(appointment);
                    }
                }

                chosenAppointmentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChosenAppointments.this, "Failed to retrieve chosen appointments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCancelAppointmentClick(Appointment appointment) {
        String appointmentId = appointment.getAppointmentId();

        if (appointmentId != null) {
            appointment.setChosen(false);
            appointment.setUserID("");

            appointmentsRef.child(appointmentId).setValue(appointment)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ChosenAppointments.this, "Appointment canceled successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ChosenAppointments.this, "Failed to cancel appointment", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
