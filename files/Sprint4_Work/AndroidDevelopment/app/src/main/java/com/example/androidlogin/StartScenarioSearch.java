package com.example.androidlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class StartScenarioSearch extends AppCompatActivity {

    //Declare all front end elements
    private EditText ScenarioSearch;
    private ImageButton SearchButton;

    //Declare a database reference, will be used later to connect/point to our database
    DatabaseReference ScenarioDatabase;

    //Declare an autocomplete text feature
    private AutoCompleteTextView txtSearch;

    //Declare the back-end for the recycler view in the XML
    private RecyclerView scenariosList;
    DatabaseReference PatientDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_scenario_search);

        //Set up a reference to our realtime firebase database and search for a node with Patients
        ScenarioDatabase = FirebaseDatabase.getInstance().getReference("Scenarios");

        PatientDatabase = FirebaseDatabase.getInstance().getReference("Patients");

        //Connect the patient name element to the back-end
        txtSearch = findViewById(R.id.editTextSSScenarioCode);

        //Connect the RecyclerView element to the back-end
        scenariosList = findViewById(R.id.scenarioListSS);

        //Connect the input box for patient to the back-end
        ScenarioSearch = findViewById(R.id.editTextSSScenarioCode);

        //Create a linear layout manager which can create multiple views/screens linearly.
        //The content of recycler view is set to the manager to allow multiple mini screens
        //in the form of search results to appear
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        scenariosList.setLayoutManager(layoutManager);

        //Run function to display search results
        populateSearch();

    }

    private void populateSearch() {

        //Create an event listener that listens for any input in the search bar
        ValueEventListener eventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    ArrayList<String> names = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        //System.out.println(ds.child("scenarioCode").getValue());
                        String n = ds.child("scenarioCode").getValue(String.class);
                        names.add(n);
                    }

                    //Create an adapter to show the results in a drop down list format
                    ArrayAdapter adapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, names);
                    txtSearch.setAdapter(adapter1);
                    txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String selection=adapterView.getItemAtPosition(i).toString();
                            getScenarios(selection);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ScenarioDatabase.addListenerForSingleValueEvent(eventListener);
    }

    //Get all scenarios who's part of their scenario code matches the selection of input in the search bar
    private void getScenarios(String selection) {
        Query query = ScenarioDatabase.orderByChild("scenarioCode").equalTo(selection);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    ArrayList<ScenarioInfo> scenarioInfos = new ArrayList<>();

                    for(DataSnapshot ds:snapshot.getChildren())
                    {

                        //We stored patient's UID when creating a scenario so we will use that UID to search for their name.
                        Object patientID = ds.child("patient").getValue();
                        DatabaseReference patientNameRef = PatientDatabase.child(patientID.toString()).child("name");

                        //Check the db for the patient's name and create a scenario info object to display the tile information,
                        //Once the user clicks an option from the search
                        patientNameRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String patientName = (String) snapshot.getValue();

                                //Get only the scenario code and UID (key)
                                ScenarioInfo scenarioInfo = new ScenarioInfo(ds.child("scenarioCode").getValue(String.class)
                                        ,patientName,ds.getKey());
                                scenarioInfos.add(scenarioInfo);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                System.out.println("The read failed.");
                            }
                        });

                    }
                    CustomAdapter adapter = new CustomAdapter(scenarioInfos, StartScenarioSearch.this);
                    scenariosList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(eventListener);
    }

    //Create a patient info class to get attribute information from patient objects in the database
    class ScenarioInfo
    {

        public String getName(){
            return name;
        }

        public String getCode(){
            return code;
        }

        public String getKey(){
            return key;
        }

        public String code;
        public String name;
        public String key;

        public ScenarioInfo(String code, String name, String key) {
            this.code = code;
            this.name = name;
            this.key = key;
        }
    }

    //Create adapter to display the search results of the clicked option in a row layout
    public static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private ArrayList<ScenarioInfo> localDataSet;
        private Context context;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView scenarioCode;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                name = view.findViewById(R.id.ssSearchPatientName);
                scenarioCode = view.findViewById(R.id.scenarioCode);
            }

        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView.
         */
        public CustomAdapter(ArrayList<ScenarioInfo> dataSet, Context context) {
            this.localDataSet = dataSet;
            this.context = context;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
            View view = layoutInflater.inflate(R.layout.row_style_ss_search_patient,viewGroup,false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            ScenarioInfo thisScenario = localDataSet.get(position);
            viewHolder.scenarioCode.setText(thisScenario.getCode());
            viewHolder.name.setText(thisScenario.getName());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*
                    Pass the scenario's UID as key into StartScenario.java to connect the scenario option to
                    starting a scenario. Intent allows us to send information between classes.
                     */
                    Intent intent = new Intent(context, StartScenario.class);
                    intent.putExtra("key", thisScenario.getKey());
                    context.startActivity(intent);
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }

}