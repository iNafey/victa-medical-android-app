package com.example.androidlogin;

import static com.example.androidlogin.CreatePatient.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class cp_geo_epi_factors extends AppCompatActivity implements View.OnClickListener{

    // Declare all the input fields for the Geographical/Epidemiological Factors page
    private EditText inputAmbientTemp, inputWeatherTransition, inputRelHumidity, inputEndemicDiseaseReg,
            inputDisease, inputRecentTravel, inputCommunicableDiseaseContact, inputRecentSrcOfFood, inputRecentSrcOfWater,
            inputRecentSanitationSystem, inputLongTermEnvExposure, inputNaturalCatastropheExposure;

    // Declare back button
    private ImageButton btnGEFback;

    // Declare next button
    private Button btnGEFSubmit;

    // Declare progress bar
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp_geo_epi_factors);

        //Connect the input field variables to the Edit Text elements in the corresponding activity_cp_ethnicity_information.xml
        inputAmbientTemp = findViewById(R.id.editTextAmbientTemperature);
        inputWeatherTransition = findViewById(R.id.editTextWeatherTransition);
        inputRelHumidity = findViewById(R.id.editTextRelativeHumidity);
        inputEndemicDiseaseReg = findViewById(R.id.editTextEndemicDiseaseRegion);
        inputDisease = findViewById(R.id.editTextDisease);
        inputRecentTravel = findViewById(R.id.editTextRecentTravel);
        inputCommunicableDiseaseContact = findViewById(R.id.editTextCommunicableDiseaseContact);
        inputRecentSrcOfFood = findViewById(R.id.editTextRecentSourceOfFood);
        inputRecentSrcOfWater = findViewById(R.id.editTextRecentSourceOfWater);
        inputRecentSanitationSystem = findViewById(R.id.editTextRecentSanitationSystem);
        inputLongTermEnvExposure = findViewById(R.id.editTextLongTermEnvExposure);
        inputNaturalCatastropheExposure = findViewById(R.id.editTextNaturalCatastropheExposure);

        // Connect back button variable to the Image Button in the corresponding XML file
        // and set an onclick listener to check for any click on the button
        btnGEFback = findViewById(R.id.btnCpBack4);
        btnGEFback.setOnClickListener(this);

        // Connect next button variable to Button element in the XML file
        // and set an onclick listener to check for any click on the button
        btnGEFSubmit = findViewById(R.id.btnCpSubmit);
        btnGEFSubmit.setOnClickListener(this);

        // Connect the back-end progress bar to the processing bar UI element within the corresponding XML file.
        progressBar = findViewById(R.id.processingBar);
    }

    // Define the actions for any button click
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCpBack4:
                startActivity(new Intent(this, cp_pro_background.class));
                break;
            case  R.id.btnCpSubmit:
                validate();
                break;
        }
    }

    // Validate the required text fields on the XML page
    private void validate() {

        // Assign all the inputs (after some formatting) to variables that can be assigned to patient object's attributes
        String ambientTemp = inputAmbientTemp.getText().toString().trim();
        String weatherTransition = inputWeatherTransition.getText().toString().trim();
        String relativeHumidity = inputRelHumidity.getText().toString().trim();
        String endemicDiseaseRegion = inputEndemicDiseaseReg.getText().toString().trim();
        String disease = inputDisease.getText().toString().trim();
        String recentTravel = inputRecentTravel.getText().toString().trim();
        String communicableDiseaseContact = inputCommunicableDiseaseContact.getText().toString().trim();
        String recentSrcOfFood = inputRecentSrcOfFood.getText().toString().trim();
        String recentSrcOfWater = inputRecentSrcOfWater.getText().toString().trim();
        String recentSanitationSystem = inputRecentSanitationSystem.getText().toString().trim();
        String longTermEnvExposure = inputLongTermEnvExposure.getText().toString().trim();
        String naturalCatastropheExposure = inputNaturalCatastropheExposure.getText().toString().trim();

        // Validate the disease input field
        if (disease.isEmpty()) {
            inputDisease.setError("Patient's illness is required!");
            inputDisease.requestFocus();
            return;
        }

        // Assign the input variables to the Patient object's attributes
        patient.setAmbientTemperature(ambientTemp);
        patient.setWeatherTransition(weatherTransition);
        patient.setRelativeHumidity(relativeHumidity);
        patient.setEndemicDiseaseRegion(endemicDiseaseRegion);
        patient.setDiseaseVectors(disease);
        patient.setRecentSanitationSystem(recentTravel);
        patient.setCommunicableDiseaseContact(communicableDiseaseContact);
        patient.setRecentSourceOfFood(recentSrcOfFood);
        patient.setRecentSourceOfWater(recentSrcOfWater);
        patient.setRecentSanitationSystem(recentSanitationSystem);
        patient.setLongTermEnvironmentExposure(longTermEnvExposure);
        patient.setNaturalCatastropheExposure(naturalCatastropheExposure);

        // When the patient object is being written to Firebase Real-time Database, make the progress bar visible to give UI responsiveness
        progressBar.setVisibility(View.VISIBLE);

        //Find the Patients node in the database, create auto-generated UID and write a patient object with all its attributes.
        //Implement an onCompleteListener to check if the data write is successful.
        FirebaseDatabase.getInstance().getReference().child("Patients").push().setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                // If successful, tell the user the patient has been added
                if (task.isSuccessful()) {
                    Toast.makeText(cp_geo_epi_factors.this, "Patient added successfully!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(cp_geo_epi_factors.this, MainActivity.class));

                // Else, give them an error message
                } else {
                    Toast.makeText(cp_geo_epi_factors.this, "Patient could not be added! Check internet and try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}