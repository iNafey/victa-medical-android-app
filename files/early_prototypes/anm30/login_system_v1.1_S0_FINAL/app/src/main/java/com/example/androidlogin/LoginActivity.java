package com.example.androidlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialise the register button, progress bar, Firebase and the login details
        register = (TextView) findViewById(R.id.signUp);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.btnLogin);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.loginEmail);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    //Check which button has been pressed
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //If signUp text link pressed then go to Register page
            case R.id.signUp:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            //If btnlogin (Login button) is pressed then call the userLogin() function to validate and authenticate the user.
            case R.id.btnLogin:
                userLogin();
                break;
        }
    }

    //Validate and authenticate the user's identity (if they are in the firebase real-time database)
    private void userLogin() {

        //Retrieve text from input boxes to the program
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //Make sure email is not empty
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email is required.");
            editTextEmail.requestFocus();
            return;
        }

        //Make sure input is of a correct email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        //Make sure password field is not empty
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return;
        }

        //Password can't be less than 6 characters
        if (password.length() < 6) {
            editTextPassword.setError("Password must have 6 or more characters.");
            editTextPassword.requestFocus();
            return;
        }

        //We want to show the loading circle during the authentication process - So user knows some processes are happening
        progressBar.setVisibility(View.VISIBLE);

        //Check if login credentials are in the firebase real-time database
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Redirect to dashboard
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                }else {
                    Toast.makeText(LoginActivity.this,"Failed to login! Please input the correct details!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}