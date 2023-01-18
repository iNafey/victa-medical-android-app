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

//Author: Abdul Nafey Mohammed(anm30)

//This activity lets the user create a scenario to firebase database
public class AddScenarioDetails extends AppCompatActivity implements View.OnClickListener{

    //Declare a scenario object accessible from all class in the directory
    public static Scenario scenario;

    //Declare all front end elements and treatment object
    private FloatingActionButton addAllergy, addPastDiagnosis, addPastTreatments;
    private Button btnAddSymptom, btnCreateScenario;
    private EditText inputSmokingHabit, inputConsumptionHabit, inputScenarioCode;
    private ProgressBar progressBar;

    //Declare a database reference, will be used later to connect/point to our database 'ref' is
    // for the Patients table and 'databaseReference' is for Scenarios table within the db
    DatabaseReference ref;
    private DatabaseReference databaseReference;

    //Declare a boolean variable for the scenario code's existence - used to avoid duplicate codes
    private boolean codeExists = false;

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
        inputScenarioCode = findViewById(R.id.editTextScenarioCode);

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
        String scenarioCode = inputScenarioCode.getText().toString().trim();

        //Assign smoking and consumption habit attributes to our scenario object.
        scenario.setSmokingHabit(smokingHabit);
        scenario.setConsumptionHabit(consumptionHabit);

        //Following code is for validation of the scenario code
        //Scenario code mustn't be empty, any length apart from 6 digits, alphanumeric or a dupe in db
        if (scenarioCode.isEmpty()){
            inputScenarioCode.setError("A 6 digit code is required!");
            inputScenarioCode.requestFocus();
            return;
        }

        if (scenarioCode.length() != 6){
            inputScenarioCode.setError("Code must be 6 digits long!");
            inputScenarioCode.requestFocus();
            return;
        }

        try {
            Integer.parseInt(scenarioCode);
        } catch(NumberFormatException e){
            inputScenarioCode.setError("Code must be like 000102!");
            inputScenarioCode.requestFocus();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Scenarios");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Result will be held Here
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String compareScenarioCode = dsp.child("scenarioCode").getValue().toString().trim();

                    if (scenarioCode.matches(compareScenarioCode)) {
                        codeExists = true;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (codeExists == false){
            inputScenarioCode.setError("That scenario code already exists!");
            inputScenarioCode.requestFocus();
            return;
        }

        //If the inputted scenario code passes all validation its added to scenario object
        scenario.setScenarioCode(scenarioCode);

        //Scenario must have a symptom at all costs - else leads to a non existent/faulty scenario
        if (scenario.getSymptoms().size() == 0){
            //System.out.println(scenario.getSymptoms());
            Toast.makeText(AddScenarioDetails.this, "Scenario cannot be added without a symptom", Toast.LENGTH_LONG).show();
            startActivity(new Intent(AddScenarioDetails.this, AddScenarioSearch.class));

        } else {
            // When the patient object is being written to Firebase Real-time Database, make the progress bar visible to give UI responsiveness
            progressBar.setVisibility(View.VISIBLE);
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
}