package com.example.myapplication.Connecting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText emailEditTEXT;
    Button resetpassword;
    ProgressBar progressBar;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailEditTEXT = findViewById(R.id.email_edit_text);
        resetpassword = findViewById(R.id.reset_password);
        progressBar=findViewById(R.id.progress_bar);
        auth=FirebaseAuth.getInstance();

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpassword();
            }
        });
    }
    void resetpassword(){
        String email=emailEditTEXT.getText().toString().trim();
        if(email.isEmpty()){
            emailEditTEXT.setError("Email Required!");
            emailEditTEXT.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditTEXT.setError("Enter Valid Email!");
            emailEditTEXT.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Please check your email", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ForgotPasswordActivity.this, "Oops Something Went Wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            resetpassword.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            resetpassword.setVisibility(View.VISIBLE);
        }

    }



}