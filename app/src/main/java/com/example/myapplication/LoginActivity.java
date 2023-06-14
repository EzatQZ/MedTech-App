package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.Admin.Admin;
import com.example.myapplication.Connecting.CreateAccountActivity;
import com.example.myapplication.Connecting.ForgotPasswordActivity;
import com.example.myapplication.Doctor.DoctorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditTEXT, passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView,forgotPasswordBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditTEXT = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);
        forgotPasswordBtnTextView=findViewById(R.id.forgot_password_text_view_btn);

        loginBtn.setOnClickListener((v) -> loginUser());
        createAccountBtnTextView.setOnClickListener((v) -> startActivity
                (new Intent(LoginActivity.this, CreateAccountActivity.class)));
        forgotPasswordBtnTextView.setOnClickListener((v) -> startActivity
                (new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

    }

    void loginUser() {
            String email=emailEditTEXT.getText().toString();
            String password=passwordEditText.getText().toString();
            boolean isValidated=validateData(email,password);
            if(!isValidated){
                return;
            }
            loginAccountInFirebase(email,password);

    }

    void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()) {
                            if (email.equals("ezatqz@gmail.com") && password.equals("admin1")) {
                                startActivity(new Intent(LoginActivity.this, Admin.class));
                            } else if (email.equals("ozzs@gmail.com") && password.equals("admin2")) {
                                startActivity(new Intent(LoginActivity.this, Admin.class));
                            } else if (email.equals("james@gmail.com") && password.equals("james12345")) {
                                startActivity(new Intent(LoginActivity.this, DoctorActivity.class));
                            } else if (email.equals("jackk@gmail.com") && password.equals("jackk12345")) {
                                startActivity(new Intent(LoginActivity.this, DoctorActivity.class));
                            } else if (email.equals("thomas@gmail.com") && password.equals("thomas12345")) {
                                startActivity(new Intent(LoginActivity.this, DoctorActivity.class));
                            } else if (email.equals("reidd@gmail.com") && password.equals("reidd12345")) {
                                startActivity(new Intent(LoginActivity.this, DoctorActivity.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            finish();
                        } else {
                            Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                });
    }





    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }

    }
    boolean validateData(String email,String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditTEXT.setError("Please enter a valid email");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("password is too short");
            return false;
        }
        return true;

    }
}
