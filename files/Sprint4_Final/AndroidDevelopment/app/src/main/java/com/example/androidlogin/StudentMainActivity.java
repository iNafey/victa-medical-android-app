package com.example.androidlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Author: Abdul Nafey Mohammed(anm30)

//This is the STUDENT DASHBOARD if the user doesn't have a isTeacher=true field in firebase
//This menu is has less privileges than a teacher's dashboard.
//There's no ability to add patient or scenario
public class StudentMainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView viewPatients;
    private TextView startScenario;
    private TextView sendScenario;
    private TextView displayCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);


        viewPatients = findViewById(R.id.viewPatientsStudent);
        viewPatients.setOnClickListener(this);

        startScenario =(TextView) findViewById(R.id.startScenarioStudent);
        startScenario.setOnClickListener(this);

        sendScenario = findViewById(R.id.sendScenarioStudent);
        sendScenario.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = user.getEmail();
        displayCurrentUser = (TextView) findViewById(R.id.textViewCurrentUserStudentMain);
        displayCurrentUser.setText("Student: " + currentUser);

    }
    public void goToLoginPage(View view) {
        Intent Intent = new Intent(this, LoginActivity.class);
        startActivity(Intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewPatientsStudent:
                startActivity(new Intent(StudentMainActivity.this, PatientViewActivity.class));
                break;
            case R.id.startScenarioStudent:
                startActivity(new Intent(StudentMainActivity.this, StartScenarioSearch.class));
                break;
            case R.id.sendScenarioStudent:
                startActivity(new Intent(StudentMainActivity.this, SendScenarioActivity.class));
                break;
        }
    }
}