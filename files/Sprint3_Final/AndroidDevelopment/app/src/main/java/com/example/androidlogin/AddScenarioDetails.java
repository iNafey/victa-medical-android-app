package com.example.androidlogin;

import static com.example.androidlogin.CreatePatient.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddScenarioDetails extends AppCompatActivity implements View.OnClickListener{

    //Declare a scenario object accessible from all class in the directory
    public static Scenario scenario;

    //Declare all front end elements and treatment object
    private FloatingActionButton addAllergy, addPastDiagnosis, addPastTreatments;
    private Button btnAddSymptom, btnCreateScenario;
    private EditText inputSmokingHabit, inputConsumptionHabit;
    private ProgressBar progressBar;

    //Declare a database reference, will be used later to connect/point to our database
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scenario_details);

        //Connect UI elements to corresponding defined variables
        btnAddSymptom = (Button) findViewById(R.id.addSymptonBttn);
        btnAddSymptom.setOnClickListener(this);

        btnCreateScenario = (Button) findViewById(R.id.createScenarioBttn);
        btnCreateScenario.setOnClickListener(this);

        progressBar = findViewById(R.id.ASProgressBar);

        addAllergy = findViewById(R.id.floatingActionAllergies);
        addAllergy.setOnClickListener(this);

        addPastDiagnosis = findViewById(R.id.floatingActionPastDiagnosis);
        addPastDiagnosis.setOnClickListener(this);

        addPastTreatments = findViewById(R.id.floatingActionPastTreatments);
        addPastTreatments.setOnClickListener(this);

        inputConsumptionHabit = findViewById(R.id.editTextConsumptionHabits);
        inputSmokingHabit = findViewById(R.id.editTextSmokingHabit);

        //Instantiate the declared scenario object
        scenario = new Scenario();

        //Get the intent passed from AddScenarioSearch.java class
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        //Set up a reference to our realtime firebase database.
        //Search for the Patients node and get UID of patient selected in search patient
        ref = FirebaseDatabase.getInstance().getReference("Patients");
        Query query = ref.orderByKey().equalTo(key);
        scenario.setPatient(key);

    }

    //When any button is pressed...
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.addSymptonBttn:
                startActivity(new Intent(AddScenarioDetails.this, SymptomPopUp.class));
                break;
            case R.id.floatingActionAllergies:
                startActivity(new Intent(AddScenarioDetails.this, AddAllergyPopUp.class));
                break;
            case R.id.floatingActionPastDiagnosis:
                startActivity(new Intent(AddScenarioDetails.this, AddPastDiagnosisPopUp.class));
                break;
            case R.id.floatingActionPastTreatments:
                startActivity(new Intent(AddScenarioDetails.this, AddPastTreatmentPopUp.class));
                break;
            case R.id.createScenarioBttn:
                validate();
                break;
        }
    }

    //Validate user input before assigning them to scenario object's attributes.
    private void validate() {

        //Store text from input box in variable(s). Don't need to check if they are empty since these are optional fields
        String smokingHabit = inputSmokingHabit.getText().toString().trim();
        String consumptionHabit = inputConsumptionHabit.getText().toString().trim();

        //Assign smoking and consumption habit attributes to our scenario object.
        scenario.setSmokingHabit(smokingHabit);
        scenario.setConsumptionHabit(consumptionHabit);

        // When the patient object is being written to Firebase Real-time Database, make the progress bar visible to give UI responsiveness
        progressBar.setVisibility(View.VISIBLE);

        if (scenario.getSymptoms().size() == 0){
            System.out.println(scenario.getSymptoms());
            Toast.makeText(AddScenarioDetails.this, "Scenario cannot be added without a symptom", Toast.LENGTH_LONG).show();
            startActivity(new Intent(AddScenarioDetails.this, AddScenarioDetails.class));

        }

        //Find the Scenarios node in the database, create auto-generated UID and write a scenario object with all its attributes.
        //Implement an onCompleteListener to check if the data write is successful.
        FirebaseDatabase.getInstance().getReference().child("Scenarios").push().setValue(scenario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                // If successful, tell the user the scenario has been added
                if (task.isSuccessful()) {
                    Toast.makeText(AddScenarioDetails.this, "Scenario added successfully!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(AddScenarioDetails.this, MainActivity.class));

                    // Else, give them an error message
                } else {
                    Toast.makeText(AddScenarioDetails.this, "Scenario could not be added! Check internet and try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}