package com.example.myapplication.Users;

import androidx.annotation.NonNull;
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
import java.util.Calendar;
import java.util.List;

public class UserAppointmentsActivity extends AppCompatActivity implements AppointmentsAdapter.OnChooseAppointmentClickListener {

    private RecyclerView appointmentsRecyclerView;
    private AppointmentsAdapter appointmentsAdapter;
    private List<Appointment> appointments;

    private DatabaseReference appointmentsRef;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointments);

        // Get the current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }

        // Initialize Firebase Database reference
        appointmentsRef = FirebaseDatabase.getInstance().getReference("Appointments");

        // Initialize appointments list and adapter
        appointments = new ArrayList<>();
        appointmentsAdapter = new AppointmentsAdapter(appointments, this, this);

        // Configure the RecyclerView
        appointmentsRecyclerView = findViewById(R.id.appointments_recycler_view);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentsRecyclerView.setAdapter(appointmentsAdapter);

        // Retrieve the appointments from Firebase
        retrieveAppointmentsFromFirebase();
    }

    private void retrieveAppointmentsFromFirebase() {
        appointmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointments.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if (appointment != null) {
                        appointments.add(appointment);
                    }
                }

                appointmentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserAppointmentsActivity.this, "Failed to retrieve appointments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isAppointmentClashing(Appointment newAppointment) {
        for (Appointment existingAppointment : appointments) {
            if (existingAppointment.isChosen() && currentUserId.equals(existingAppointment.getUserID())) {
                if (existingAppointment.getDate().equals(newAppointment.getDate())
                        && existingAppointment.getTime().equals(newAppointment.getTime())) {
                    // Appointments have the same date and time, so there is a clash
                    return true;
                }
            }
        }
        return false;
    }


    private boolean areAppointmentsWithinRange(Appointment appointment1, Appointment appointment2) {
        String[] time1 = appointment1.getTime().split(":");
        String[] time2 = appointment2.getTime().split(":");

        int hour1 = Integer.parseInt(time1[0]);
        int minute1 = Integer.parseInt(time1[1]);

        int hour2 = Integer.parseInt(time2[0]);
        int minute2 = Integer.parseInt(time2[1]);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, hour1);
        calendar1.set(Calendar.MINUTE, minute1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, hour2);
        calendar2.set(Calendar.MINUTE, minute2);

        long differenceInMillis = Math.abs(calendar1.getTimeInMillis() - calendar2.getTimeInMillis());
        final long MINUTES_IN_MILLIS = 60000;
        final int HALF_HOUR_IN_MINUTES = 30;

        return differenceInMillis < (HALF_HOUR_IN_MINUTES * MINUTES_IN_MILLIS);
    }

    @Override
    public void onChooseAppointmentClick(Appointment appointment) {
        if (isAppointmentClashing(appointment)) {
            // Display an error message to the user indicating the appointment clashes
            Toast.makeText(this, "This appointment clashes with an existing appointment", Toast.LENGTH_SHORT).show();
        } else {
            String appointmentId = appointment.getAppointmentId();

            if (appointmentId != null) {
                appointment.setChosen(true);
                appointment.setUserID(currentUserId);

                appointmentsRef.child(appointmentId).setValue(appointment)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UserAppointmentsActivity.this, "Appointment chosen successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserAppointmentsActivity.this, "Failed to choose appointment", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
}
