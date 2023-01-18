package com.example.androidlogin;

import static com.example.androidlogin.AddScenarioDetails.scenario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddAllergyPopUp extends AppCompatActivity implements View.OnClickListener{

    //Define all text inputs and buttons in the front-end
    EditText inputAllergy;
    Button btnAddAllergy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addallergieswindow);

        //Pop-up window sizing
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        //Connect UI elements to corresponding defined variables
        inputAllergy = findViewById(R.id.editTextAllergy);
        btnAddAllergy = findViewById(R.id.btnAddAllergy);
        btnAddAllergy.setOnClickListener(this);
    }

    //When a button is pressed...
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnAddAllergy:
                validate();
                break;

        }
    }

    //Validate user input
    private void validate() {

        //Store text from input box in variable(s)
        String allergy = inputAllergy.getText().toString().trim();

        //Empty allergy must not be added
        if (allergy.isEmpty()) {
            inputAllergy.setError("Allergy name required! Click outside to cancel");
            inputAllergy.requestFocus();
            return;
        }

        //Assign allergy attribute to scenario object
        scenario.getAllergy().add(allergy);

        //Close the pop-up and display responsiveness of user's action
        finish();
        Toast.makeText(AddAllergyPopUp.this, "Allergy added!", Toast.LENGTH_LONG).show();
    }
}
