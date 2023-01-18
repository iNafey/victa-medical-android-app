package com.example.androidlogin;

import static com.example.androidlogin.CreatePatient.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class cp_ethnicity_information extends AppCompatActivity implements View.OnClickListener{

    // Declare all the input fields for the Ethnicity Info page
    private EditText inputRace, inputClan, inputTotem, inputTribe;

    // Declare back button
    private ImageButton btnCpEiBack;

    // Declare next button
    private Button btnCpEiNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp_ethnicity_information);

        //Connect the input field variables to the Edit Text elements in the corresponding activity_cp_ethnicity_information.xml
        inputRace = (EditText) findViewById(R.id.editTextRace);
        inputClan = (EditText) findViewById(R.id.editTextClan);
        inputTribe = (EditText) findViewById(R.id.editTextTribe);
        inputTotem = (EditText) findViewById(R.id.editTextTotem);

        // Connect back button variable to the Image Button in the corresponding XML file
        // and set an onclick listener to check for any click on the button
        btnCpEiBack = (ImageButton) findViewById(R.id.btnCpBack2);
        btnCpEiBack.setOnClickListener(this);

        // Connect next button variable to Button element in the XML file
        // and set an onclick listener to check for any click on the button
        btnCpEiNext = (Button) findViewById(R.id.btnCpNext02);
        btnCpEiNext.setOnClickListener(this);


    }

    // Define the actions for any button click
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCpBack2:
                startActivity(new Intent(this, CreatePatient.class));
                break;
            case R.id.btnCpNext02:
                validate();
                break;
        }
    }

    // Validate the required text fields on the XML page
    public void validate(){

        // Assign all the inputs (after some formatting) to variables that can be assigned to patient object's attributes
        String race = inputRace.getText().toString().trim();
        String clan = inputClan.getText().toString().trim();
        String tribe = inputTribe.getText().toString().trim();
        String totem = inputTotem.getText().toString().trim();

        //Validation for required field: race
        if (race.isEmpty()) {
            inputRace.setError("Patient's race is required!");
            inputRace.requestFocus();
            return;
        }

        //Assign all the formatted user inputs to a Patient object's attributes
        patient.setRace(race);
        patient.setClan(clan);
        patient.setTribe(tribe);
        patient.setTotem(totem);

        // Start the new activity: Professional Background, when all validation is done
        startActivity(new Intent(this, cp_pro_background.class));

    }
}