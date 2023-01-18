package com.example.androidlogin;

import static com.example.androidlogin.CreatePatient.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class cp_pro_background extends AppCompatActivity implements View.OnClickListener{

    // Declare all the input fields for the professional background page
    private EditText inputProTraining, inputTrainingHistory, inputOccupationalHistory;

    // Declare the next button
    private Button btnCpPbNext;

    // Declare the back button
    private ImageButton btnCpPbBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp_pro_background);

        // Declare all the input fields for the Professional Background page
        inputProTraining = findViewById(R.id.editTextProfessionalTraining);
        inputTrainingHistory = findViewById(R.id.editTextEnterDiagnosis);
        inputOccupationalHistory = findViewById(R.id.editTextOccupationalHistory);

        btnCpPbBack = (ImageButton) findViewById(R.id.btnCpBack3);
        btnCpPbBack.setOnClickListener(this);

        btnCpPbNext = (Button) findViewById(R.id.btnCpNext3);
        btnCpPbNext.setOnClickListener(this);
    }

    // Define the actions for any button click
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCpBack3:
                startActivity(new Intent(this, cp_ethnicity_information.class));
                break;
            case R.id.btnCpNext3:
                validate();
                break;
        }
    }

    // Validate the required text fields on the XML page
    private void validate() {
        String proTraining = inputProTraining.getText().toString().trim();
        String trainingHistory = inputTrainingHistory.getText().toString().trim();
        String occupationalHistory = inputOccupationalHistory.getText().toString().trim();

        //Assign all the formatted user inputs to a Patient object's attributes
        patient.setProfessionalTraining(proTraining);
        patient.setTrainingHistory(trainingHistory);
        patient.setOccupationalHistory(occupationalHistory);

        //Implement the navigation between the XMl for this class to the XMl for the cp_geo_epi_factors.java class
        startActivity(new Intent(this, cp_geo_epi_factors.class));

    }


}