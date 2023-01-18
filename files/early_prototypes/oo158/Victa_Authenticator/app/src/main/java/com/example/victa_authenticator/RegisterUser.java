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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView  logo, register ;
    private EditText registerName , registerEmail, registerID, editTextTextPassword;


    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        logo = (TextView) findViewById(R.id.logo);
        logo.setOnClickListener(this);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);


        registerName = (EditText) findViewById(R.id.registerName);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerID = (EditText) findViewById(R.id.registerID);
        editTextTextPassword  = (EditText) findViewById(R.id.editTextTextPassword);
        progressBar =  (ProgressBar) findViewById(R.id.progressBar);






    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerLogo:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.register:
                register();
                break;


        }

    }

    private void register() {
        String email= registerEmail.getText().toString().trim();
        String password = editTextTextPassword.getText().toString().trim();
        String fullname = registerName.getText().toString().trim();
        String ID = registerID.getText().toString().trim();


        if (fullname.isEmpty()) {
            registerName.setError("Full name is required");
            registerName.requestFocus();
            return;


        }
        if (ID.isEmpty()) {
            registerID.setError("ID is required");
            registerID.requestFocus();
            return;
    }
        if (email.isEmpty()) {
            registerEmail.setError("Email is required");
            registerEmail.requestFocus();
            return;

}
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        registerEmail.setError("please provide a valid email address");
        registerEmail.requestFocus();
        return;
        }

        if (password.isEmpty()) {
            editTextTextPassword.setError("Password is required");
            editTextTextPassword.requestFocus();
            return;


    }
        if(password.length() < 6) {
            editTextTextPassword.setError("Password should be at least 6 characters");
            editTextTextPassword.requestFocus();
            return;
        }

       progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(fullname, ID, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        ProgressBar.setVisibility(View.GONE);

                                    } else {
                                        Toast.makeText(RegisterUser.this, "Failed to register! try again ", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);


                                    }
                                }

                            });
                        }else{
                            Toast.makeText(RegisterUser.this, "Failed to register! try again ", Toast.LENGTH_LONG).show();
                            ProgressBar.setVisibility(View.GONE);
                            }

                        }


                });

        }
    }
