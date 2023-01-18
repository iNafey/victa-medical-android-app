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

public class AddPastTreatmentPopUp extends AppCompatActivity implements View.OnClickListener{

    //Declare all front end elements and treatment object
    EditText inputTreatmentDate, inputTreatment;
    Button btnAddTreatment;
    Treatment t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pasttreatmentswindow);

        //Pop-up window sizing
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        //Connect UI elements to corresponding defined variables
        inputTreatmentDate = findViewById(R.id.editTextPTDate);
        inputTreatment = findViewById(R.id.editTextPTTreatment);
        btnAddTreatment = findViewById(R.id.btnAddTreatment);
        btnAddTreatment.setOnClickListener(this);

        //Always remember to instantiate the declared object variable
        t = new Treatment();
    }

    //When a button is pressed...
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnAddTreatment:
                validate();
                break;
        }
    }

    //Validate user input
    private void validate() {

        //Store text from input box in variable(s)
        String TreatmentDate = inputTreatmentDate.getText().toString().trim();
        String Treatment = inputTreatment.getText().toString().trim();

        //Validation for treatment date to be of the format: DD/MM/YYYY
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            date = sdf.parse(TreatmentDate);
            System.out.println("Im in the try statement");
            if (!TreatmentDate.equals(sdf.format(date))) {
                date = null;
                inputTreatmentDate.setError("Date should be in the format DD/MM/YYYY");
                inputTreatmentDate.requestFocus();
                return;
            }
        } catch (ParseException ex) {
            inputTreatmentDate.setError("Date should be in the format DD/MM/YYYY");
            inputTreatmentDate.requestFocus();
            ex.printStackTrace();
            return;
        }

        //Empty diagnosis must not be added
        if (Treatment.isEmpty()) {
            inputTreatment.setError("Past treatment description required!");
            inputTreatment.requestFocus();
            return;
        }

        //Assign date and name attributes to diagnosis object
        t.setDate(TreatmentDate);
        t.setName(Treatment);

        //Assign treatment instance to a scenario object. Multiple treatments can be added by clicking on the + button
        scenario.getTreatments().add(t);

        //Close the pop-up and display responsiveness of user's action
        finish();
        Toast.makeText(AddPastTreatmentPopUp.this, "Treatment added!", Toast.LENGTH_LONG).show();
    }
}