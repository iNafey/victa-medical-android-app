package com.example.androidlogin;

import static com.example.androidlogin.AddScenarioDetails.scenario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPastDiagnosisPopUp extends AppCompatActivity implements View.OnClickListener{

    //Declare all front-end elements and diagnosis object
    EditText inputDiagnosisDate, inputDiagnosis;
    Button btnAddDiagnosis;
    Diagnosis diagnosisObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pastdiagnosiswindow);

        //Pop-up window sizing
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        //Connect UI elements to corresponding defined variables
        inputDiagnosisDate = findViewById(R.id.editTextPDDate);
        inputDiagnosis = findViewById(R.id.editTextPDDiagnosis);
        btnAddDiagnosis = findViewById(R.id.btnAddDiagnosis);
        btnAddDiagnosis.setOnClickListener(this);

        //Always remember to instantiate the declared object variable
        diagnosisObj = new Diagnosis();
    }

    //When a button is pressed...
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddDiagnosis:
                validate();
                break;
        }
    }

    //Validate user input
    private void validate() {

        //Store text from input box in variable(s)
        String diagnosisDate = inputDiagnosisDate.getText().toString().trim();
        String diagnosis = inputDiagnosis.getText().toString().trim();

        //Validation for diagnosis date to be the format: DD/MM/YYYY
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            date = sdf.parse(diagnosisDate);
            System.out.println("Im in the try statement");
            if (!diagnosisDate.equals(sdf.format(date))) {
                date = null;
                inputDiagnosisDate.setError("Date should be in the format DD/MM/YYYY");
                inputDiagnosisDate.requestFocus();
                return;
            }
        } catch (ParseException ex) {
            inputDiagnosisDate.setError("Date should be in the format DD/MM/YYYY");
            inputDiagnosisDate.requestFocus();
            ex.printStackTrace();
            return;
        }

        //Empty diagnosis must not be added
        if (diagnosis.isEmpty()) {
            inputDiagnosis.setError("Past diagnosis required!");
            inputDiagnosis.requestFocus();
            return;
        }

        //Assign date and name attributes to diagnosis object
        diagnosisObj.setDate(diagnosisDate);
        diagnosisObj.setName(diagnosis);

        //Assign diagnosis instance to a scenario object. Multiple diagnoses can be added by clicking on the + button
        scenario.getDiagnoses().add(diagnosisObj);

        //Close the pop-up and display responsiveness of user's action
        finish();
        Toast.makeText(AddPastDiagnosisPopUp.this, "Diagnosis added!", Toast.LENGTH_LONG).show();


    }
}
