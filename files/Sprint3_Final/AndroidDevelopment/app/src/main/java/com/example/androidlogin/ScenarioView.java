package com.example.androidlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScenarioView extends AppCompatActivity {

    TextView Allergies, SmokingHistory, AlxhoholConsunptionHistory, PostDiagnosis,PostTreatmment,Symptoms;
    Button showbutton, nxtbutton, bckbutton;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_view);


        Allergies = (TextView) findViewById(R.id.allergiesView);
        SmokingHistory = (TextView) findViewById(R.id.smokingHistoryView);
        AlxhoholConsunptionHistory = (TextView) findViewById(R.id.alchoholConsumptionHistoryView);
        PostDiagnosis = (TextView) findViewById(R.id.pDiagnosisView);
        PostTreatmment = (TextView) findViewById(R.id.pTreatmentView);
        Symptoms = (TextView) findViewById(R.id.textsymptomexplanation);



        showbutton = (Button) findViewById(R.id.buttonstart);
        nxtbutton = (Button) findViewById(R.id.buttonnext2);
        bckbutton = (Button) findViewById(R.id.buttonback);



        nxtbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ScenarioView.this, "we are going to go further in sprint 4", Toast.LENGTH_SHORT).show();
            }
        });
        bckbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScenarioView.this, MainActivity.class));;
                Toast.makeText(ScenarioView.this, "Main Page", Toast.LENGTH_SHORT).show();
            }
        });

        //View a scenario. Shows one specific working example
        showbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reff = FirebaseDatabase.getInstance().getReference().child("Scenarios").child("-MyJKMXw446UsBFhnd6H");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String allergies = dataSnapshot.child("allergy").getValue().toString();
                        String smokingHistory = dataSnapshot.child("smokingHabit").getValue().toString();
                        String alcohol = dataSnapshot.child("consumptionHabit").getValue().toString();
                        String pdiagnosis = dataSnapshot.child("diagnoses").getValue().toString();
                        String ptreatments = dataSnapshot.child("treatments").getValue().toString();
                        String symptoms = dataSnapshot.child("symptoms").getValue().toString();

                        Allergies.setText(allergies);
                        SmokingHistory.setText(smokingHistory);
                        AlxhoholConsunptionHistory.setText(alcohol);
                        PostDiagnosis.setText(pdiagnosis);
                        PostTreatmment.setText(ptreatments);
                        Symptoms.setText(symptoms);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


    }



}