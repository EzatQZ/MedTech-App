package com.example.myapplication.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DoctorAppointments extends AppCompatActivity {

    private DatabaseReference appointmentsRef;
    private String currentDoctorId;
    private int year, month, day;
    private int hour, minute;

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

        // Set click listener for the date EditText to show DatePickerDialog
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dateEditText);
            }
        });

        // Set click listener for the time EditText to show TimePickerDialog
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(timeEditText);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                    // Check for appointment clashes
                    checkForAppointmentClash(appointment);
                } else {
                    Toast.makeText(DoctorAppointments.this, "Failed to generate appointment ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDatePickerDialog(final EditText dateEditText) {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int monthOfYear, int dayOfMonth) {
                        // Save the selected date
                        year = selectedYear;
                        month = monthOfYear;
                        day = dayOfMonth;

                        // Display the selected date in the EditText
                        String selectedDate = String.format("%d-%02d-%02d", year, month + 1, day);
                        dateEditText.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(final EditText timeEditText) {
        final Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        // Save the selected time
                        hour = selectedHour;
                        minute = selectedMinute;

                        // Display the selected time in the EditText
                        String selectedTime = String.format("%02d-%02d", hour, minute); // Modify the format to "HH-mm"
                        timeEditText.setText(selectedTime);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }


    private void checkForAppointmentClash(Appointment newAppointment) {
        // Retrieve existing appointments for the current doctor from the database
        Query query = appointmentsRef.orderByChild("doctorId").equalTo(currentDoctorId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Loop through all appointments for the doctor
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    Appointment existingAppointment = appointmentSnapshot.getValue(Appointment.class);

                    // Check for clash with the new appointment
                    if (existingAppointment != null && isAppointmentClash(existingAppointment, newAppointment)) {
                        // There is a clash with an existing appointment
                        Toast.makeText(DoctorAppointments.this, "This time slot is not available. Please choose another time.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // No clash with any existing appointment, save the new appointment in the database
                saveNewAppointment(newAppointment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error here, if needed
                Toast.makeText(DoctorAppointments.this, "Failed to retrieve appointments.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isAppointmentClash(Appointment existingAppointment, Appointment newAppointment) {
        // Extract date and time information from both appointments
        String existingDate = existingAppointment.getDate();
        String existingTime = existingAppointment.getTime();
        String newDate = newAppointment.getDate();
        String newTime = newAppointment.getTime();

        // Convert the existing time and new time to minutes
        int existingMinutes = convertTimeToMinutes(existingTime);
        int newMinutes = convertTimeToMinutes(newTime);

        // Check for clash by considering a buffer of 30 minutes before and after each appointment
        if (existingDate.equals(newDate) &&
                Math.abs(existingMinutes - newMinutes) <= 30) {
            return true; // Clash found
        }

        return false; // No clash
    }

    private int convertTimeToMinutes(String time) {
        String[] timeArray = time.split(":");
        int hours = Integer.parseInt(timeArray[0]);
        int minutes = Integer.parseInt(timeArray[1]);
        return hours * 60 + minutes;
    }

    private void saveNewAppointment(Appointment newAppointment) {
        // Save the new appointment in the database
        String appointmentId = newAppointment.getAppointmentId();
        appointmentsRef.child(appointmentId).setValue(newAppointment)
                .addOnSuccessListener(aVoid -> {
                    // Show success message or update UI
                    Toast.makeText(DoctorAppointments.this, "Appointment added successfully", Toast.LENGTH_SHORT).show();
                    // Clear the input fields
                    EditText dateEditText = findViewById(R.id.edit_text_date);
                    EditText timeEditText = findViewById(R.id.edit_text_time);
                    EditText nameEditText = findViewById(R.id.edit_text_name);
                    dateEditText.setText("");
                    timeEditText.setText("");
                    nameEditText.setText("");
                })
                .addOnFailureListener(e -> {
                    // Show error message or handle failure
                    Toast.makeText(DoctorAppointments.this, "Failed to add appointment", Toast.LENGTH_SHORT).show();
                });
    }
}
