package com.example.victa_authenticator;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private EditText emailAdd, passwordAdd;
    private Button login;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.registerlog);
        register.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        emailAdd = (EditText) findViewById(R.id.emailAdd);
        passwordAdd = (EditText) findViewById(R.id.passwordAdd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerlog:
                startActivity(new Intent(this,RegisterUser.class));
                break;

            case R.id.login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = emailAdd.getText().toString().trim();
        String password = passwordAdd.getText().toString().trim();


        if (email.isEmpty()) {
            emailAdd.setError("Email is required");
            emailAdd.requestFocus();
            return;



        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        emailAdd.setError("please provide a valid email address");
        emailAdd.requestFocus();
        return;
        }
        if (password.isEmpty()) {
            passwordAdd.setError("Password is required");
            passwordAdd.requestFocus();
            return;


        }

        if(password.length() < 6) {
            passwordAdd.setError("Password should be at least 6 characters");
            passwordAdd.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, Profile.class));
                    //redirect to user profile

                }else{
                    Toast.makeText(MainActivity.this,"Failed to log it , please renter your username and password",Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}
