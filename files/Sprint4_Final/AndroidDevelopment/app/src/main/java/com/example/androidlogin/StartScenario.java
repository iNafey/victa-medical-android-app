package com.example.androidlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Author: Abdul Nafey Mohammed (anm30)

//This activity displays a chosen scenario to the user who can interact and input their diagnoses
//and treatment. The scenario has a looping mechanism for symptoms and diagnoses as part of each step.
public class StartScenario extends AppCompatActivity implements View.OnClickListener{

    //Declare backend variables for front-end(xml view) elements
    private TextView Allergies, SmokingHistory, AlxhoholConsunptionHistory, PostDiagnosis,PostTreatmment, treatment, symptomYetToReveal;
    private EditText diagnosisInput;
    private Button btnRevealSymptom, btnTreatPatient, btnGoToSummary;

    private ScrollView myScrollView;
    private RecyclerView symptomList;

    //Set up a reference to pull info from db
    private DatabaseReference scenarioRef;

    //Create objects that we will use in this class - helps in pushing clean scenario summary objects
    private UserDiagnosis userDiagnosis;
    private StepDiagnosis stepDiagnosis;

    //Create var for totalSymptoms of a scenario, counter and array of the symptoms and diagnoses
    private Integer totalSymptoms = 1;
    private Integer clickCount = 0;
    ArrayList<Symptom> scenarioSymptoms = new ArrayList<>();
    ArrayList<String> userDiagnosisInputs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_scenario);

        //Connect all relevant back-end variables we created to their corresponding XML elements
        symptomYetToReveal = findViewById(R.id.symptomCount);

        Allergies = findViewById(R.id.allergiesView);
        SmokingHistory = findViewById(R.id.smokingHistoryView);
        AlxhoholConsunptionHistory = findViewById(R.id.alchoholConsumptionHistoryView);
        PostDiagnosis = findViewById(R.id.pDiagnosisView);
        PostTreatmment = findViewById(R.id.pTreatmentView);
        diagnosisInput = findViewById(R.id.editTextEnterDiagnosis);

        myScrollView = findViewById(R.id.ssScrollView);

        //Connect the RecyclerView element to the back-end
        symptomList = findViewById(R.id.recyclerViewSS);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        symptomList.setLayoutManager(layoutManager);

        //Get the intent passed from StartScenarioSearch.java class
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        btnRevealSymptom = (Button) findViewById(R.id.buttonRevealSymptom);
        btnTreatPatient = (Button) findViewById(R.id.buttonTreatPatient);
        btnTreatPatient.setOnClickListener(this);

        //Instantiate UserDiagnosis object - we will push this to firebase later
        userDiagnosis = new UserDiagnosis();

        //Set up the reference variable to get the info from the node - key within the Scenarios table
        scenarioRef = FirebaseDatabase.getInstance().getReference().child("Scenarios").child(key);

        //Find out the current user's email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            userDiagnosis.setUserID(userEmail);
        } else {
            // No user is signed in
        }


        //View a scenario and let the user interact with the scenario
        //This reveals a symptom as part of the new step for user. Also records their diagnosis.
        btnRevealSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scenarioRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Declare all the default outputs to be shown to user on the XML page
                        String smokingHistory="None", alcohol="None", pdiagnosisDate="None",
                        pdiagnosisName = "None", ptreatmentDate="None", ptreatmentName="None", symptom="None";

                        //Reveal symptom button is at the bottom of the screen so this scrolls up
                        myScrollView.fullScroll(ScrollView.FOCUS_UP);

                        //Find out how many totalSymptoms there are to define the no. of steps in this scenario
                        totalSymptoms = (int) dataSnapshot.child("symptoms").getChildrenCount();

                        //Set the scenario code to the one found under a scenario in Scenarios table in db
                        userDiagnosis.setScenarioCode(dataSnapshot.child("scenarioCode").getValue().toString());

                        //Looping mechanism for the steps in the scenario
                        if (clickCount <= totalSymptoms){
                            //Create a symptom object and a step diagnosis object for the current step
                            Symptom aSymptom;
                            aSymptom = new Symptom();
                            stepDiagnosis = new StepDiagnosis();

                            //Set the symptom type and desc for a symptom object
                            aSymptom.setSymptomType(dataSnapshot.child("symptoms").child(Integer.toString(clickCount)).
                                    child("symptomType").getValue().toString());
                            aSymptom.setDesc(dataSnapshot.child("symptoms").child(Integer.toString(clickCount)).
                                    child("desc").getValue().toString());

                            //Once a symptom object is pulled from firebase add it to list of symptoms in scenario
                            scenarioSymptoms.add(aSymptom);


                            /*
                            When the scenario starts, the user needs to click on reveal symptom button to display
                            the 1st symptom. This increases the click count for the button. But by now the 1st symptom
                            is already pulled hence we need to get the 1st item of scenarioSymptoms when the click
                            count is one. Otherwise, we pull scenario symptom at clickCount-1 as index start from 0
                             */
                            if (clickCount == 1) {
                                stepDiagnosis.setStepSymptom(scenarioSymptoms.get(0));
                            }else if (clickCount > 1){
                                stepDiagnosis.setStepSymptom(scenarioSymptoms.get(clickCount-1));
                            }

                            //This sets the step no of the diagnosis
                            stepDiagnosis.setStepNo(clickCount);
                        }

                        //If a patient has allergies, pull that data
                        String allergies = "";
                        if (!(dataSnapshot.child("allergy").getValue() == null)){
                            int totalAllergies = (int) dataSnapshot.child("allergy").getChildrenCount();

                            //Get all allergies and make up a string of allergies to present in XML
                            for (int i=0; i<totalAllergies; i++) {
                                String allergy = dataSnapshot.child("allergy").child(Integer.toString(i)).getValue().toString();
                                allergies = allergies + allergy + "\n";
                            }
                        }

                        //If a patient has a smoking habit pull that info to display in front end
                        if (!(dataSnapshot.child("smokingHabit").getValue() == null)){
                            smokingHistory = dataSnapshot.child("smokingHabit").getValue().toString();
                        }

                        //If patient has a consumpion habit pull that info to display
                        if (!(dataSnapshot.child("consumptionHabit").getValue() == null)){
                            alcohol = dataSnapshot.child("consumptionHabit").getValue().toString();
                        }

                        //If a patient has past diagnoses, pull that data
                        String diagnoses = "";
                        if (!(dataSnapshot.child("diagnoses").getValue() == null)){
                            int totalDiagnoses = (int) dataSnapshot.child("diagnoses").getChildrenCount();

                            //Get all past diagnoses and make up a string of diagnoses to display in front end
                            for (int i=0; i<totalDiagnoses; i++) {
                                pdiagnosisDate = dataSnapshot.child("diagnoses").child(Integer.toString(i)).child("date").getValue().toString();
                                pdiagnosisName = dataSnapshot.child("diagnoses").child(Integer.toString(i)).child("name").getValue().toString();
                                diagnoses = diagnoses + "Diagnosis:- \nDate: " + pdiagnosisDate + "\nDiagnosis: " +pdiagnosisName+"\n\n";
                            }
                        }

                        //If a patient has past treatments, pull that data
                        String treatments = "";
                        if (!(dataSnapshot.child("treatments").getValue() == null)){
                            int totalTreatments = (int) dataSnapshot.child("treatments").getChildrenCount();

                            //Get all past treatments and make up a string of treatments to display in front end
                            for (int i=0; i<totalTreatments; i++) {
                                ptreatmentDate = dataSnapshot.child("treatments").child(Integer.toString(i)).child("date").getValue().toString();
                                ptreatmentName = dataSnapshot.child("treatments").child(Integer.toString(i)).child("name").getValue().toString();
                                treatments = treatments + "Treatment:- \nDate: " + ptreatmentDate + "\nDescription: " +ptreatmentName+"\n\n";
                            }
                        }

                        //Display all the info that has been pulled and cleaned by the program in the front end
                        Allergies.setText(allergies);
                        SmokingHistory.setText(smokingHistory);
                        AlxhoholConsunptionHistory.setText(alcohol);
                        PostDiagnosis.setText(diagnoses);
                        PostTreatmment.setText(treatments);

                        //If the user enters a diagnosis once a symptom is revealed, then record it
                        if (clickCount != 0) {
                            String diagnosisAtAStep = diagnosisInput.getText().toString().trim();

                            stepDiagnosis.setStepDiagnosis(diagnosisAtAStep);

                            //Add diagnosis at each step to make a list of diagnoses throughout the scenario
                            userDiagnosis.getScenarioDiagnoses().add(stepDiagnosis);
                            diagnosisInput.getText().clear();

                            //Following code for testing if inputs are corrected aligned to each step
                            //userDiagnosisInputs.add(diagnosisAtAStep);
                            //System.out.println("User Diagnosis inputs: "+userDiagnosisInputs);
                        }
                        clickCount += 1;

                        //Each time, user clicks on Reveal Symptom, tell them how many symptoms are left to reveal
                        //If there are no symptoms in the scenario then inform the user on the next step
                        if (totalSymptoms == 0 || (totalSymptoms-clickCount <= 0)) {
                            symptomYetToReveal.setText("* No symptoms left to reveal, click on 'Treat Patient'");
                            btnRevealSymptom.setClickable(false);
                            btnRevealSymptom.setBackgroundColor(Color.parseColor("#939997"));
                        }
                        else {
                            symptomYetToReveal.setText("* " + Integer.toString(totalSymptoms - clickCount) + " symptom(s) yet to be revealed");
                        }

                        //Set up adapter to display symptoms at each step using a recycler view
                        CustomAdapter adapter = new CustomAdapter(scenarioSymptoms, StartScenario.this);
                        symptomList.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });




    }


    //What to do when a button on the screen is pressed
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonTreatPatient:

                //Only let the user treat the patient, once they see all their symptoms!
                if (clickCount < totalSymptoms){
                    Toast.makeText(StartScenario.this,"Reveal all symptoms first!", Toast.LENGTH_LONG).show();
                }else{
                    btnTreatPatient.setBackgroundColor(Color.parseColor("#4b8ad1"));
                    treatPatient();
                }
                break;
            case R.id.btnGoToSummary:

                //Summarise the previous interactions via a ScenarioSummary object and push to db
                summarizeScenario();
                break;
        }
    }

    private void treatPatient() {

        //Only assign data to step diagnosis and user diagnosis if the user pressed the Reveal Symptom
        //at least once before they click on Treat Patient. How can the user treat a patient without
        //reading any symptoms
        if (clickCount != 0) {
            Symptom aSymptom;
            aSymptom = new Symptom();
            stepDiagnosis = new StepDiagnosis();

            String diagnosisAtAStep = diagnosisInput.getText().toString().trim();

            //Setup all the diagnosis details for the last step
            stepDiagnosis.setStepNo(clickCount);
            stepDiagnosis.setStepDiagnosis(diagnosisAtAStep);
            stepDiagnosis.setStepSymptom(scenarioSymptoms.get(clickCount-1));

            //Append this diagnosis details above to a list of all diagnoses in the scenario
            userDiagnosis.getScenarioDiagnoses().add(stepDiagnosis);
            diagnosisInput.getText().clear();

            userDiagnosisInputs.add(diagnosisAtAStep);
        }

        //Set up a view for Treatment of the patient
        setContentView(R.layout.activity_ss_treatment);

        //Setup the button to go to the scenario summary
        btnGoToSummary = findViewById(R.id.btnGoToSummary);
        btnGoToSummary.setOnClickListener(this);

    }

    private void summarizeScenario() {
        //Get the user's treatment input and set it to the diagnosis object to be uploaded to db
        treatment = findViewById(R.id.editTextEnterTreatment);
        String treatmentInput = treatment.getText().toString().trim();
        userDiagnosis.setTreatment(treatmentInput);

        //Push the userDiagnosis object which is the summary of all the interactions that occured in the last 2 screens
        FirebaseDatabase.getInstance().getReference().child("ScenarioSummary").push().setValue(userDiagnosis).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // If successful, tell the user the scenario has been added
                if (task.isSuccessful()) {
                    Toast.makeText(StartScenario.this, "Scenario completed successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(StartScenario.this, SummaryActivityP.class));

                // Else, give them an error message
                } else {
                    Toast.makeText(StartScenario.this, "Scenario not completed! Check internet and try again!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Create a symptom info class
    class SymptomInfo
    {

        public String getType(){
            return type;
        }

        public String getDescription(){
            return description;
        }

        public String getKey(){
            return key;
        }

        public String type;
        public String description;
        public String key;

        public SymptomInfo(String type, String description, String key) {
            this.type = type;
            this.description = description;
            this.key = key;
        }
    }

    //Create adapter to display the search results of the clicked option in a row layout
    public static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private ArrayList<Symptom> localDataSet;
        private Context context;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView symptomNo, symptomType;
            TextView symptomDescription;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                symptomNo = view.findViewById(R.id.textViewStepNo);
                symptomType = view.findViewById(R.id.textViewSSTypeData);
                symptomDescription = view.findViewById(R.id.textViewSSDescData);

            }

        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView.
         */
        public CustomAdapter(ArrayList<Symptom> dataSet, Context context) {
            this.localDataSet = dataSet;
            this.context = context;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
            View view = layoutInflater.inflate(R.layout.row_style_ss_symptom_info,viewGroup,false);
            return new CustomAdapter.ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder viewHolder, final int position) {


            // Get element from the dataset at this position and replace the
            // contents of the view with that element
            Symptom thisSymptom = localDataSet.get(position);

            //Had an error where I couldnt assign stepDiagnosis.getStepNo() so I had to just
            //output a symptom heading without the number besides it
            viewHolder.symptomNo.setText("Symptom");
            viewHolder.symptomType.setText(thisSymptom.getSymptomType());
            viewHolder.symptomDescription.setText(thisSymptom.getDesc());

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }



}