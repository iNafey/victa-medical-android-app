package com.example.androidlogin;

import static com.example.androidlogin.AddScenarioDetails.scenario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SymptomPopUp extends AppCompatActivity implements View.OnClickListener{

    //Declare all front-end elements and symptom object
    EditText inputSymptomDesc;
    Spinner inputSymptomType;
    String symptomTypeValue; //Need to declare a symptom attribute name due to higher access level
    Button btnAddSymptomInPopUp;
    Symptom s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_pop_up);

        //Pop-up window sizing
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int) (height*.6));

        //Connect UI elements to corresponding defined variables
        inputSymptomType = findViewById(R.id.spinnerASSymptomType);
        inputSymptomDesc = findViewById(R.id.editTextASSymptomDesc);
        btnAddSymptomInPopUp = findViewById(R.id.btnAddSymptomInPopUp);
        btnAddSymptomInPopUp.setOnClickListener(this);

        //Set up the spinner by fetching all its values from res/values/strings.xml and connecting adapter to spinner element
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(SymptomPopUp.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.symptomtypes));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputSymptomType.setAdapter(myAdapter);

        //When an option is selected, a high level access variable copies the value to be used if the user clicks the add button
        inputSymptomType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString();
                symptomTypeValue = value;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Instantiate a symptom object
        s = new Symptom();
    }

    //When a button is pressed...
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnAddSymptomInPopUp:
                validate();
                break;
        }
    }

    //Validate user input before adding to a symptom instance
    private void validate() {

        //Store text from input box in variable(s)
        String symptomDesc = inputSymptomDesc.getText().toString().trim();

        //Empty symptom description must not be added
        if (symptomDesc.isEmpty()) {
            inputSymptomDesc.setError("Symptom description is required!");
            inputSymptomDesc.requestFocus();
            return;
        }

        //Assign symptom type and description attributes to symptom object
        s.setSymptomType(symptomTypeValue);
        s.setDesc(symptomDesc);

        //Assign symptom instance to a scenario object. Multiple symptoms can be added by clicking on the "ADD SYMPTOM" button
        scenario.getSymptoms().add(s);

        //Close the pop-up and display responsiveness of user's action
        finish();
        Toast.makeText(SymptomPopUp.this, "Symptom added!", Toast.LENGTH_LONG).show();
    }
}