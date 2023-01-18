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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


//Author: Abdul Nafey Mohammed(anm30) and Dante Baptiste (db370)


//STARTING ACTIVITY of app.
//This activity lets the user login into the app.
//Access based roles and their privileges are authorised here i.e Student/Teacher roles
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText inputEmail, inputPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    public static String isTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inputEmail = (EditText) findViewById(R.id.inputLoginEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                Login();
                break;
        }
    }
    private void Login() {
        String inputemail = inputEmail.getText().toString().trim();
        String inputpassword = inputPassword.getText().toString().trim();

        if (inputemail.isEmpty()) {
            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputemail).matches()){
            inputEmail.setError("please provide a valid email address");
            inputEmail.requestFocus();
            return;
        }

        if (inputpassword.isEmpty()) {
            inputPassword.setError("Password is required");
            inputPassword.requestFocus();
            return;
        }

        if (inputpassword.length() < 6) {
            inputPassword.setError("Password should be at least 6 characters");
            inputPassword.requestFocus();
            return;
        }

        //During authentication show a loading circle to show responsiveness
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(inputemail, inputpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    //Find out what type of role the user has i.e a student or a teacher
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");

                    //Check if the user has a isTeacher role and if it's true to take them to the teacher's dashboard
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Result will be held Here
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                String userkey  = dsp.getKey();
                                String compareEmail = dsp.child("email").getValue().toString().trim();

                                if (compareEmail.matches(inputemail)) {
                                    if (!(dsp.child("isTeacher").getValue() == null)) {
                                        isTeacher = dsp.child("isTeacher").getValue().toString();

                                        if (isTeacher == "true"){
                                            System.out.println("User is a teacher?"+isTeacher);
                                            progressBar.setVisibility(View.GONE);
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        }else{
                                            //Since the user is student, take them to the student dashboard
                                            progressBar.setVisibility(View.GONE);
                                            startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                                        }

                                    }else{
                                        //Since the user is student, take them to the student dashboard
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                                    }


                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });

                }
                else{
                    Toast.makeText(LoginActivity.this,"Failed to login! Please check your credentials or internet!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    //on click go to main page
    public void goToMainPage(View view) {
        Intent Intent = new Intent(this, MainActivity.class);
        startActivity(Intent);
    }
    public void goToRegisterPage(View view) {
        Intent Intent = new Intent(this, RegisterActivity.class);
        startActivity(Intent);
    }
}