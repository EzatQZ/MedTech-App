package com.example.myapplication.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfile extends AppCompatActivity {

    private EditText emailEditText, fullNameEditText;
    private ImageView userIconImageView;
    private Button saveButton;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        emailEditText = findViewById(R.id.email_edit_text);
        fullNameEditText = findViewById(R.id.full_name_edit_text);
        userIconImageView = findViewById(R.id.user_icon_image_view);
        saveButton = findViewById(R.id.save_button);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue(String.class);
                String fullName = dataSnapshot.child("fullName").getValue(String.class);

                emailEditText.setText(email);
                fullNameEditText.setText(fullName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("MyProfile", "Database error: " + databaseError.getMessage());
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String fullName = fullNameEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(MyProfile.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(MyProfile.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                    return;
                }



                mAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userRef.child("email").setValue(email);
                            userRef.child("fullName").setValue(fullName);

                            Toast.makeText(MyProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MyProfile.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MyProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

