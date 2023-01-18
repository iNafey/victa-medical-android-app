package com.example.androidlogin;

//Symptom object(s) are used in scenarios. Can be multiple.
public class Symptom {
    private String symptomType, desc;

    public Symptom() {};

    public String toString(){
        return "Symptom Type: "+symptomType+", Symptom Description: "+desc;
    }

    public String getSymptomType() {
        return symptomType;
    }

    public void setSymptomType(String symptomType) {
        this.symptomType = symptomType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
