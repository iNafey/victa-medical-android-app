package com.example.androidlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SendScenarioActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText subject;
    private TextView body;
    private Button buttonSend;
    private TextView buttonBack;

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
        setContentView(R.layout.activity_send_scenario);

        email = findViewById(R.id.editTextSendScenarioEnterEmail);
        email.setOnClickListener(this);

        subject = findViewById(R.id.editTextSendScenarioEnterSubject);
        subject.setOnClickListener(this);

        body = findViewById(R.id.textViewSendScenarioEnterBody);
        body.setOnClickListener(this);

        buttonSend = findViewById(R.id.buttonSendScenarioSendButton);
        buttonSend.setOnClickListener(this);

        buttonBack = findViewById(R.id.textViewBackFromSendScenario);
        buttonBack.setOnClickListener(this);
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

                   if (childEmail.equals(currentUser)) {
                       list.add(child.child("scenarioCode").getValue().toString());
                       for (int i = 0; i < ((int) child.child("scenarioDiagnoses").getChildrenCount()); i++) {
                           String strI = String.valueOf(i);
                           int j = i+1;

                           summary += "Symptom Description" + j + " : " + child.child("scenarioDiagnoses").child(strI).child("stepSymptom").child("desc").getValue().toString() + " \r\n" +
                                   "Symptom Type" + j + " : " + child.child("scenarioDiagnoses").child(strI).child("stepSymptom").child("symptomType").getValue().toString() + " \r\n" +
                                   "Diagnosis" + j + " : " + child.child("scenarioDiagnoses").child(strI).child("stepDiagnosis").getValue().toString() + "\r\n";
                           map.put(child.child("scenarioCode").getValue().toString(), summary);
                       }
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        autoCompleteTxt = findViewById(R.id.auto_complete_text_send_scenario);
        adapterItems = new ArrayAdapter<String>(this, R.layout.send_scenario_list_item,list);
        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String items = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), "Selected Scenario: "+items,Toast.LENGTH_SHORT).show();
                summary = items;

                body.setText((String) map.get(items));
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewBackFromSendScenario:
                Toast.makeText(SendScenarioActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SendScenarioActivity.this, MainActivity.class));
                break;
            //open the email app when the send email button is clicked
            case R.id.buttonSendScenarioSendButton:
                if (!email.getText().toString().isEmpty() || !subject.getText().toString().isEmpty() || !body.getText().toString().isEmpty()) {

                    if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                        Toast.makeText(SendScenarioActivity.this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});
                        intent.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
                        intent.putExtra(Intent.EXTRA_TEXT, body.getText().toString());

                        intent.setData(Uri.parse("mailto:"));
                        if (intent.resolveActivity(getPackageManager()) != null ) {
                            startActivity(intent);
                        }else {
                            Toast.makeText(SendScenarioActivity.this, "There is not application that support this action.", Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    Toast.makeText(SendScenarioActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();

                }
        }
    }
}