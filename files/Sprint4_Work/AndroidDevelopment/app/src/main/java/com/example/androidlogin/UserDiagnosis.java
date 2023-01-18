package com.example.androidlogin;

import java.util.ArrayList;

public class UserDiagnosis {
    private ArrayList<StepDiagnosis> scenarioDiagnoses = new ArrayList<>();

    private String userID;

    private String treatment;

    private String scenarioCode;

    public UserDiagnosis() {}

    public UserDiagnosis(ArrayList<StepDiagnosis> diagnoses, String uid, String treatment, String scenarioCode){
        this.scenarioDiagnoses = diagnoses;
        this.userID = uid;
        this.treatment = treatment;
        this.scenarioCode = scenarioCode;
    }

    @Override
    public String toString() {
        return "UserDiagnosis{" +
                "scenarioDiagnoses=" + scenarioDiagnoses +
                ", userID='" + userID + '\'' +
                ", treatment='" + treatment + '\'' +
                ", scenarioCode='" + scenarioCode + '\'' +
                '}';
    }

    public ArrayList<StepDiagnosis> getScenarioDiagnoses() {
        return scenarioDiagnoses;
    }

    public void setScenarioDiagnoses(ArrayList<StepDiagnosis> scenarioDiagnoses) {
        this.scenarioDiagnoses = scenarioDiagnoses;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getScenarioCode() {
        return scenarioCode;
    }

    public void setScenarioCode(String scenarioCode) {
        this.scenarioCode = scenarioCode;
    }
}
