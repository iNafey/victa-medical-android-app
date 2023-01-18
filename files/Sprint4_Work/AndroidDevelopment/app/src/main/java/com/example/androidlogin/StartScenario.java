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

public class StartScenario extends AppCompatActivity implements View.OnClickListener{

    private TextView Allergies, SmokingHistory, AlxhoholConsunptionHistory, PostDiagnosis,PostTreatmment, treatment, symptomYetToReveal;
    private EditText diagnosisInput;
    private Button btnRevealSymptom, btnTreatPatient, btnGoToSummary;
    private DatabaseReference scenarioRef;
    private Integer totalSymptoms = 1;
    private Integer clickCount = 0;

    private ScrollView myScrollView;
    private RecyclerView symptomList;
    private UserDiagnosis userDiagnosis;
    private StepDiagnosis stepDiagnosis;

    ArrayList<Symptom> scenarioSymptoms = new ArrayList<>();
    ArrayList<String> userDiagnosisInputs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_scenario);

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


        userDiagnosis = new UserDiagnosis();

        scenarioRef = FirebaseDatabase.getInstance().getReference().child("Scenarios").child(key);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            userDiagnosis.setUserID(userEmail);
        } else {
            // No user is signed in
        }


        //View a scenario. Shows one specific working example
        btnRevealSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scenarioRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String allergy, smokingHistory="None", alcohol="None", pdiagnosisDate="None", pdiagnosisName = "None", ptreatmentDate="None", ptreatmentName="None", symptom="None";


                        myScrollView.fullScroll(ScrollView.FOCUS_UP);

                        totalSymptoms = (int) dataSnapshot.child("symptoms").getChildrenCount();
                        //System.out.println("There are "+totalSymptoms+" symptoms");
                        userDiagnosis.setScenarioCode(dataSnapshot.child("scenarioCode").getValue().toString());
                        if (clickCount <= totalSymptoms){
                            Symptom aSymptom;
                            aSymptom = new Symptom();
                            stepDiagnosis = new StepDiagnosis();

                            aSymptom.setSymptomType(dataSnapshot.child("symptoms").child(Integer.toString(clickCount)).child("symptomType").getValue().toString());
                            aSymptom.setDesc(dataSnapshot.child("symptoms").child(Integer.toString(clickCount)).child("desc").getValue().toString());
                            scenarioSymptoms.add(aSymptom);

                            if (clickCount == 1) {
                                stepDiagnosis.setStepSymptom(scenarioSymptoms.get(0));
                                System.out.println("Click count: "+clickCount);
                            }else if (clickCount > 1){
                                stepDiagnosis.setStepSymptom(scenarioSymptoms.get(clickCount-1));
                            }
                            stepDiagnosis.setStepNo(clickCount);

                            System.out.println("Scenario Symptoms: "+scenarioSymptoms);
                        }


                        String allergies = "";
                        if (!(dataSnapshot.child("allergy").getValue() == null)){
                            int totalAllergies = (int) dataSnapshot.child("allergy").getChildrenCount();


                            for (int i=0; i<totalAllergies; i++) {
                                allergy = dataSnapshot.child("allergy").child(Integer.toString(i)).getValue().toString();
                                System.out.println("ALLERGY: "+allergy);
                                allergies = allergies + allergy + "\n";
                            }
                        }

                        if (!(dataSnapshot.child("smokingHabit").getValue() == null)){
                            smokingHistory = dataSnapshot.child("smokingHabit").getValue().toString();
                        }

                        if (!(dataSnapshot.child("consumptionHabit").getValue() == null)){
                            alcohol = dataSnapshot.child("consumptionHabit").getValue().toString();
                        }

                        String diagnoses = "";
                        if (!(dataSnapshot.child("diagnoses").getValue() == null)){
                            int totalDiagnoses = (int) dataSnapshot.child("diagnoses").getChildrenCount();


                            for (int i=0; i<totalDiagnoses; i++) {
                                pdiagnosisDate = dataSnapshot.child("diagnoses").child(Integer.toString(i)).child("date").getValue().toString();
                                pdiagnosisName = dataSnapshot.child("diagnoses").child(Integer.toString(i)).child("name").getValue().toString();
                                diagnoses = diagnoses + "Diagnosis:- \nDate: " + pdiagnosisDate + "\nDiagnosis: " +pdiagnosisName+"\n\n";
                            }
                        }

                        String treatments = "";
                        if (!(dataSnapshot.child("treatments").getValue() == null)){

                            int totalTreatments = (int) dataSnapshot.child("treatments").getChildrenCount();


                            for (int i=0; i<totalTreatments; i++) {
                                ptreatmentDate = dataSnapshot.child("treatments").child(Integer.toString(i)).child("date").getValue().toString();
                                ptreatmentName = dataSnapshot.child("treatments").child(Integer.toString(i)).child("name").getValue().toString();
                                treatments = treatments + "Treatment:- \nDate: " + ptreatmentDate + "\nDescription: " +ptreatmentName+"\n\n";
                            }
                        }

                        //allergies = "None";
                        Allergies.setText(allergies);
                        SmokingHistory.setText(smokingHistory);
                        AlxhoholConsunptionHistory.setText(alcohol);
                        PostDiagnosis.setText(diagnoses);
                        PostTreatmment.setText(treatments);


                        if (clickCount != 0) {
                            String diagnosisAtAStep = diagnosisInput.getText().toString().trim();
                            stepDiagnosis.setStepDiagnosis(diagnosisAtAStep);
                            userDiagnosis.getScenarioDiagnoses().add(stepDiagnosis);
                            System.out.println("Step diagnosis: " + stepDiagnosis + "  User Diagnosis: " + userDiagnosis);
                            diagnosisInput.getText().clear();

                            userDiagnosisInputs.add(diagnosisAtAStep);
                            System.out.println("User Diagnosis inputs: "+userDiagnosisInputs);
                        }
                        clickCount += 1;




                        if (totalSymptoms == 0 || (totalSymptoms-clickCount <= 0)) {
                            symptomYetToReveal.setText("* No symptoms left to reveal, click on 'Treat Patient'");
                            btnRevealSymptom.setClickable(false);
                            btnRevealSymptom.setBackgroundColor(Color.parseColor("#939997"));
                        }
                        else {
                            symptomYetToReveal.setText("* " + Integer.toString(totalSymptoms - clickCount) + " symptom(s) yet to be revealed");
                        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonTreatPatient:

                if (clickCount < totalSymptoms){
                    Toast.makeText(StartScenario.this,"Reveal all symptoms first!", Toast.LENGTH_LONG).show();
                }else{
                    btnTreatPatient.setBackgroundColor(Color.parseColor("#4b8ad1"));
                    treatPatient();
                }
                break;
            case R.id.btnGoToSummary:
                summarizeScenario();
                break;
        }
    }

    private void treatPatient() {
        if (clickCount != 0) {

            Symptom aSymptom;
            aSymptom = new Symptom();
            stepDiagnosis = new StepDiagnosis();
            String diagnosisAtAStep = diagnosisInput.getText().toString().trim();
            stepDiagnosis.setStepNo(clickCount);
            stepDiagnosis.setStepDiagnosis(diagnosisAtAStep);
            stepDiagnosis.setStepSymptom(scenarioSymptoms.get(clickCount-1));
            userDiagnosis.getScenarioDiagnoses().add(stepDiagnosis);
            System.out.println(userDiagnosis.toString());
            diagnosisInput.getText().clear();

            userDiagnosisInputs.add(diagnosisAtAStep);
            System.out.println("User Diagnosis inputs: "+userDiagnosisInputs);


        }

        setContentView(R.layout.activity_ss_treatment);

        btnGoToSummary = findViewById(R.id.btnGoToSummary);
        btnGoToSummary.setOnClickListener(this);

    }

    private void summarizeScenario() {

        treatment = findViewById(R.id.editTextEnterTreatment);
        String treatmentInput = treatment.getText().toString().trim();
        userDiagnosis.setTreatment(treatmentInput);

        FirebaseDatabase.getInstance().getReference().child("ScenarioSummary").push().setValue(userDiagnosis).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // If successful, tell the user the scenario has been added
                if (task.isSuccessful()) {
                    Toast.makeText(StartScenario.this, "Scenario completed successfully!", Toast.LENGTH_LONG).show();

                    // Else, give them an error message
                } else {
                    Toast.makeText(StartScenario.this, "Scenario not completed! Check internet and try again!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Create a patient info class to get attribute information from patient objects in the database
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


            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            Symptom thisSymptom = localDataSet.get(position);
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