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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    //Declare all the user details which will be stored in the database
    String fName, lName, email, password, rePassword,uniName, yearOfStudy;

    //Declare all of the text input boxes, button, text links & progress bar from the corresponding XML file of this class
    private EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputReEnterPassword, inputUniversity, inputYearInUniversity;

    private Button btnRegister;
    private TextView btnLogin;
    ProgressBar progressBar;

    //Declare a Firebase Authenticator variable
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Connect the inputs to the corresponding ones in the XML files via their unique ID
        inputFirstName = (EditText) findViewById(R.id.inputFirstName);
        inputLastName = (EditText) findViewById(R.id.inputLastName);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.password);
        inputReEnterPassword = (EditText) findViewById(R.id.inputReEnterPassword);
        inputUniversity = (EditText) findViewById(R.id.inputUniversity);
        inputYearInUniversity = (EditText) findViewById(R.id.inputYearInUniversity);

        btnLogin = (TextView) findViewById(R.id.loginText);
        btnLogin.setOnClickListener(this);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);

    }

    //Check which button has been pressed
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            //If login link is pressed then go to login page
            case R.id.loginText:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            //If register button is pressed then call registerUser function
            case R.id.btnRegister:
                registerUser();

        }
    }

    // Validate the user's input and update the firebase real-time database accordingly
    private void registerUser() {

        //Connect the inputs to the program variables for validation
        fName = inputFirstName.getText().toString().trim();
        lName = inputLastName.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();
        rePassword = inputReEnterPassword.getText().toString().trim();
        uniName = inputUniversity.getText().toString().trim();
        yearOfStudy = inputYearInUniversity.getText().toString().trim();

        //Make sure first name is not blank
        if (TextUtils.isEmpty(fName)) {
            inputFirstName.setError("Enter your first name!");
            inputFirstName.requestFocus();
            return;
        }

        //Make sure last name is not empty
        if (TextUtils.isEmpty(lName)) {
            inputLastName.setError("Enter your last name!");
            inputLastName.requestFocus();
            return;
        }

        //Make sure email is not empty
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Email is required.");
            inputEmail.requestFocus();
            return;
        }

        //Make sure input is of a correct email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Please provide valid email!");
            inputEmail.requestFocus();
            return;
        }

        //Make sure password field is not empty
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Password is required.");
            inputPassword.requestFocus();
            return;
        }

        //Password can't be less than 6 characters
        if (password.length() < 6) {
            inputPassword.setError("Password must have 6 or more characters.");
            inputPassword.requestFocus();
            return;
        }

        //Check if both the passwords match to confirm the user's intent of the password they want to set
        if (rePassword == password) {
            inputReEnterPassword.setError("Passwords don't match");
            inputReEnterPassword.requestFocus();
            return;
        }

        //Make sure user enters a uni name
        if (TextUtils.isEmpty(uniName)) {
            inputUniversity.setError("Your current university required.");
            inputUniversity.requestFocus();
            return;
        }

        //Make sure user enters a numerical year of study
        if (TextUtils.isEmpty(yearOfStudy)) {
            inputYearInUniversity.setError("Enter your school year (as a number)");
            inputYearInUniversity.requestFocus();
            return;
        }

        //Make the progress bar visible to inform user background processes are occurring
        progressBar.setVisibility(View.VISIBLE);

        //Create an email and password for a user in Firebase
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Write all the user details into the real-time database (can use only upto 30 days)
                        if(task.isSuccessful()) {
                            User user = new User(fName,lName,email,password,uniName,yearOfStudy);

                            //This returns the unique id of the registered user to the "User" object above
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    //Once user is created and their details are stored, redirect them to the Login page
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));


                                    }

                                    //If user isn't registered correctly then prompt them to try and register again
                                    else {
                                        Toast.makeText(RegisterActivity.this,"Failed to register, Try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }
                        //If user isn't registered correctly then prompt them to try and register again
                        else {
                            Toast.makeText(RegisterActivity.this,"Failed to register, Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}