package com.example.androidlogin;

import static com.example.androidlogin.LoginActivity.isTeacher;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SummaryActivityP extends AppCompatActivity implements View.OnClickListener{
    //Define all text inputs and buttons in the front-end
    private EditText patientId;
    private EditText ScenarioId;
    private Button viewScenario, finishScenario;
    private TextView summaryInfo;


    //firebase
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;

    private String summary;
    private ArrayList<String> list;
    private HashMap map;
    DatabaseReference database;
    String childEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_summary_page);

        //Connect UI elements to corresponding defined variables
        /*patientId = findViewById(R.id.editTextPatientId);
        patientId.setOnClickListener(this);

        ScenarioId = findViewById(R.id.editTextScenarioId);
        ScenarioId.setOnClickListener(this);*/

        viewScenario = findViewById(R.id.viewScenarioBttn);
        viewScenario.setOnClickListener(this);

        finishScenario = findViewById(R.id.buttonFinishScenario);
        finishScenario.setOnClickListener(this);

        summaryInfo = findViewById(R.id.sumsum);
        summaryInfo.setOnClickListener(this);







        //connect to the "ScenarioSummary" table in the firebase
        database = FirebaseDatabase.getInstance().getReference("ScenarioSummary");
        list = new ArrayList<>();
        map = new HashMap();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //reference each record in the selected table
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //the code gets the summary data stored in the firebase for each record
                for(DataSnapshot child : snapshot.getChildren()) {
                    summary = new String();
                    childEmail = child.child("userID").getValue().toString().trim();
                    String currentUser = user.getEmail();

                    summary = "Scenario Code: "+ child.child("scenarioCode").getValue().toString()+"\n\nScenario steps:-\n\n";

                    if (childEmail.equals(currentUser)) {
                        list.add(child.child("scenarioCode").getValue().toString());
                        for (int i = 0; i < ((int) child.child("scenarioDiagnoses").getChildrenCount()); i++) {
                            String strI = String.valueOf(i);
                            int j = i+1;

                            summary += "Symptom Description" + j + " : " + child.child("scenarioDiagnoses").child(strI).child("stepSymptom").child("desc").getValue().toString() + " \r\n" +
                                    "Symptom Type" + j + " : " + child.child("scenarioDiagnoses").child(strI).child("stepSymptom").child("symptomType").getValue().toString() + " \r\n" +
                                    "Diagnosis" + j + " : " + child.child("scenarioDiagnoses").child(strI).child("stepDiagnosis").getValue().toString() + "\r\n\n";
                            map.put(child.child("scenarioCode").getValue().toString(), summary);

                        }
                        summary +="\nTreatment: "+child.child("treatment").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*autoCompleteTxt = findViewById(R.id.auto_complete_text_send_scenario);
        adapterItems = new ArrayAdapter<String>(this, R.layout.send_scenario_list_item,list);
        //autoCompleteTxt.setAdapter(adapterItems);
        //autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String items = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), "Selected Scenario: "+items,Toast.LENGTH_SHORT).show();
                summary = items;
                summaryInfo.setText((String) map.get(items));
            }
        });*/

    }



    //When a button is pressed...
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.viewScenarioBttn:
                showinfo();
                break;
            case R.id.buttonFinishScenario:
                if (isTeacher == "true") {
                    startActivity(new Intent(SummaryActivityP.this, MainActivity.class));
                    break;
                }else{
                    startActivity(new Intent(SummaryActivityP.this, StudentMainActivity.class));
                    break;
                }
        }
    }

    // Shows the summary of the users page
    private void showinfo() {
        summaryInfo.setText(summary);
        summaryInfo.setTextColor(Color.parseColor("#FFFFFF"));
        System.out.println(summary);

    }

}