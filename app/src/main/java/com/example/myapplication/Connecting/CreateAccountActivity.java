package com.example.myapplication.Connecting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {
    EditText emailEditTEXT,passwordEditText,idEditText,full_nameEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditTEXT=findViewById(R.id.email_edit_text);
        passwordEditText=findViewById(R.id.password_edit_text);
        idEditText=findViewById(R.id.id_edit_text);
        full_nameEditText=findViewById(R.id.FullName_edit_text);
        createAccountBtn=findViewById(R.id.create_account_btn);
        progressBar=findViewById(R.id.progress_bar);
        loginBtnTextView=findViewById(R.id.login_text_view_btn);

        createAccountBtn.setOnClickListener(v-> createAccount() );
        loginBtnTextView.setOnClickListener(v-> finish());

    }
    void createAccount(){
        String email=emailEditTEXT.getText().toString();
        String password=passwordEditText.getText().toString();
        String id=idEditText.getText().toString();
        String full_name=full_nameEditText.getText().toString();

        boolean isValidated=validateData(email,password,id);
        if(!isValidated){
            return;
        }
        createAccountInFirebase(email,password,id,full_name);

    }
    void createAccountInFirebase(String email,String password,String id,String full_name){
        changeInProgress(true);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           User user=new User(email,id,full_name,password);
                           FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid())
                                   .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()){
                                               Toast.makeText(CreateAccountActivity.this, "Account Created Successfully", Toast.LENGTH_LONG).show();
                                               progressBar.setVisibility(View.GONE);
                                               firebaseAuth.signOut();
                                               finish();
                                           }
                                           else {
                                               Toast.makeText(CreateAccountActivity.this,"Register failed",Toast.LENGTH_LONG).show();
                                               progressBar.setVisibility(View.GONE);
                                           }
                                       }
                                   });
                        }
                    }
                });
    }
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }

    }
    boolean validateData(String email,String password,String id){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditTEXT.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("password is too short");
            return false;
        }
        if(id.length()<9){
            idEditText.setError("id is too short");
            return false;
        }
        return true;

    }
}