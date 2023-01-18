package com.example.authenticator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    //On click, runs the register class and goes to Sign Up form
    public void goToSignUp(View view) {
        Intent Intent = new Intent(this, Register.class);
        startActivity(Intent);
    }
}