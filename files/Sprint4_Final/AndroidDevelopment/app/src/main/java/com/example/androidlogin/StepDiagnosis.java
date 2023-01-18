package com.example.androidlogin;

//Author: Abdul Nafey Mohammed(anm30)

//StepDiagnosis object class allows us to neatly set up a diagnosis at each step of a scenario with
//its symptom and step number too.
public class StepDiagnosis {

    private int stepNo;

    private Symptom stepSymptom;

    private String stepDiagnosis;

    public StepDiagnosis(){}

    public StepDiagnosis (int step, Symptom symptom, String diagnosis) {
        this.stepNo = step;
        this.stepSymptom = symptom;
        this.stepDiagnosis = diagnosis;
    }


    public int getStepNo() {
        return stepNo;
    }

    public void setStepNo(int stepNo) {
        this.stepNo = stepNo;
    }

    @Override
    public String toString() {
        return "StepDiagnosis{" +
                "stepNo=" + stepNo +
                ", stepSymptom=" + stepSymptom +
                ", stepDiagnosis='" + stepDiagnosis + '\'' +
                '}';
    }

    public Symptom getStepSymptom() {
        return stepSymptom;
    }

    public void setStepSymptom(Symptom stepSymptom) {
        this.stepSymptom = stepSymptom;
    }

    public String getStepDiagnosis() {
        return stepDiagnosis;
    }

    public void setStepDiagnosis(String stepDiagnosis) {
        this.stepDiagnosis = stepDiagnosis;
    }
}
