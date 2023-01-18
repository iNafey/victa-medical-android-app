package com.example.androidlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

//IMP: This is actually the Dashboard of the app
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise the logout text on the Dashboard
        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(this);
    }

    //Check which button has been pressed
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //If logout pressed then go to login page
            case R.id.logout:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}