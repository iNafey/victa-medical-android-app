package com.example.androidlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText firstName, lastName, inputUniversity, inputYear, inputEmail,inputPassword;
    private Button btnRegister;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //getActionBar().hide();

        firstName = (EditText) findViewById(R.id.inputFirstName);
        lastName = (EditText) findViewById(R.id.inputLastName);
        inputUniversity = (EditText) findViewById(R.id.inputUniversity);
        inputYear = (EditText) findViewById(R.id.inputYearInUniversity);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }
    public void goToLoginPage(View view) {
        Intent Intent = new Intent(this, LoginActivity.class);
        startActivity(Intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:

                register();
                break;

        }
    }

    private void register() {
        String firstname =  firstName.getText().toString().trim();
        String lastname =  lastName.getText().toString().trim();
        String university =  inputUniversity.getText().toString().trim();
        String year =  inputYear.getText().toString().trim();
        String  email =  inputEmail.getText().toString().trim();
        String password =  inputPassword.getText().toString().trim();


        if (firstname.isEmpty()) {
            firstName.setError("First name is required");
            firstName.requestFocus();
            return;
        }

        if (lastname.isEmpty()) {
            lastName.setError("Last name is required");
            lastName.requestFocus();
            return;
        }

        if (university.isEmpty()) {
            inputUniversity.setError("University name is required");
            inputUniversity.requestFocus();
            return;
        }

        if (year.isEmpty()) {
            inputYear.setError("University year is required");
            inputYear.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("please provide a valid email address");
            inputEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            inputPassword.setError("Password is required");
            inputPassword.requestFocus();
            return;
        }
        if(password.length() < 6) {
            inputPassword.setError("Password should be at least 6 characters");
            inputPassword.requestFocus();
            return;
        }
            progressBar.setVisibility(View.VISIBLE);


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(firstname, lastname, email, password, university, year);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "User has been register successfully!", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    });

    }
}