package com.example.androidlogin;

import java.util.ArrayList;

//Scenario object class has all information needed to Add Scenario and View Scenario.
//Instances of this object are stored in the database under Scenarios.

public class Scenario {

    private String patientUID;
    private ArrayList<String> Allergy = new ArrayList<>();
    private String smokingHabit;
    private String consumptionHabit;
    private ArrayList<Diagnosis> Diagnoses = new ArrayList<>();
    private ArrayList<Treatment> Treatments = new ArrayList<>();
    private ArrayList<Symptom> Symptoms = new ArrayList<>();

    public Scenario () {};

    public String getPatient() {
        return patientUID;
    }

    public void setPatient(String patient) {
        this.patientUID = patient;
    }

    public ArrayList<String> getAllergy() {
        return Allergy;
    }

    public void setAllergy(ArrayList<String> allergy) {
        Allergy = allergy;
    }

    public String getSmokingHabit() {
        return smokingHabit;
    }

    public void setSmokingHabit(String smokingHabit) {
        this.smokingHabit = smokingHabit;
    }

    public String getConsumptionHabit() {
        return consumptionHabit;
    }

    public void setConsumptionHabit(String consumptionHabit) {
        this.consumptionHabit = consumptionHabit;
    }

    public ArrayList<Diagnosis> getDiagnoses() {
        return Diagnoses;
    }

    public void setDiagnoses(ArrayList<Diagnosis> diagnoses) {
        Diagnoses = diagnoses;
    }

    public ArrayList<Treatment> getTreatments() {
        return Treatments;
    }

    public void setTreatments(ArrayList<Treatment> treatments) {
        Treatments = treatments;
    }

    public ArrayList<Symptom> getSymptoms() {
        return Symptoms;
    }

    public void setSymptoms(ArrayList<Symptom> symptoms) {
        Symptoms = symptoms;
    }
}
