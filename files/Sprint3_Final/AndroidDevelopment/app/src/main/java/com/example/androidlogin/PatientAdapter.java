package com.example.androidlogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
//The purpose of the class is to present each patient on the patient view activity
public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    Context context;
    ArrayList<Patient> list;

    public PatientAdapter(Context context, ArrayList<Patient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PatientAdapter.PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.patient_view_item,parent,false);
        return new PatientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapter.PatientViewHolder holder, int position) {
        Patient patient = list.get(position);
        holder.name.setText(patient.getName());
        holder.dob.setText(patient.getDob());
        holder.gender.setText(patient.getGender());
        holder.address.setText(patient.getAddress());
        holder.race.setText(patient.getRace());
        holder.occupationalHistory.setText(patient.getOccupationalHistory());
        holder.hospital.setText(patient.getHospital());
        holder.idAndNo.setText(patient.getIdAndNo());
        holder.nextOfKin.setText(patient.getNextOfKin());
        holder.nextOfKinAddress.setText(patient.getNextOfKinAddress());
        holder.professionalTraining.setText(patient.getProfessionalTraining());
        holder.recentSourceOfFood.setText(patient.getRecentSourceOfFood());
        holder.recentSourceOfWater.setText(patient.getRecentSourceOfWater());
        holder.recentSanitationSystem.setText(patient.getRecentSanitationSystem());
        holder.regDate.setText(patient.getRegDate());
        holder.trainingHistory.setText(patient.getTrainingHistory());
        holder.clan.setText(patient.getClan());
        holder.totem.setText(patient.getTotem());
        holder.tribe.setText(patient.getTribe());
        holder.maritalStatus.setText(patient.getMaritalStatus());
        holder.weatherTransition.setText(patient.getWeatherTransition());
        holder.longTermEnvironmentExposure.setText(patient.getLongTermEnvironmentExposure());
        holder.endemicDiseaseRegion.setText(patient.getEndemicDiseaseRegion());
        holder.diseaseVectors.setText(patient.getDiseaseVectors());
        holder.communicableDiseaseContact.setText(patient.getCommunicableDiseaseContact());
        holder.ambientTemperature.setText(patient.getAmbientTemperature());
        holder.relativeHumidity.setText(patient.getRelativeHumidity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder{

        TextView name, dob, gender, address, race, occupationalHistory, hospital, idAndNo, nextOfKin, nextOfKinAddress, professionalTraining,
                recentSourceOfFood, recentSourceOfWater, recentSanitationSystem, regDate, trainingHistory, clan, totem, tribe, maritalStatus, weatherTransition,
                longTermEnvironmentExposure, endemicDiseaseRegion, diseaseVectors, communicableDiseaseContact, ambientTemperature, relativeHumidity;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            dob = itemView.findViewById(R.id.textViewDOB);
            gender = itemView.findViewById(R.id.textViewGender);
            address = itemView.findViewById(R.id.textViewAddress);
            race = itemView.findViewById(R.id.textViewRace);
            occupationalHistory = itemView.findViewById(R.id.textViewOccupationalHistory);
            hospital = itemView.findViewById(R.id.textViewHospital);
            idAndNo = itemView.findViewById(R.id.textViewIdAndNo);
            nextOfKin = itemView.findViewById(R.id.textViewNextOfKin);
            nextOfKinAddress = itemView.findViewById(R.id.textViewNextOfKinAddress);
            professionalTraining = itemView.findViewById(R.id.textViewProfessionalTraining);
            recentSourceOfFood = itemView.findViewById(R.id.textViewRecentSourceOfFood);
            recentSourceOfWater = itemView.findViewById(R.id.textViewRecentSourceOfWater);
            recentSanitationSystem = itemView.findViewById(R.id.textViewRecentSanitationSystem);
            regDate = itemView.findViewById(R.id.textViewRegDate);
            trainingHistory = itemView.findViewById(R.id.textViewTrainingHistory);
            clan = itemView.findViewById(R.id.textViewClan);
            totem = itemView.findViewById(R.id.textViewTotem);
            tribe = itemView.findViewById(R.id.textViewTribe);
            maritalStatus = itemView.findViewById(R.id.textViewMaritalStatus);
            weatherTransition = itemView.findViewById(R.id.textViewWeatherTransition);
            longTermEnvironmentExposure = itemView.findViewById(R.id.textViewLongTermEnvironmentExposure);
            endemicDiseaseRegion = itemView.findViewById(R.id.textViewEndemicDiseaseRegion);
            diseaseVectors = itemView.findViewById(R.id.textViewDiseaseVectors);
            communicableDiseaseContact = itemView.findViewById(R.id.textViewCommunicableDiseaseContact);
            ambientTemperature = itemView.findViewById(R.id.textViewAmbientTemperature);
            relativeHumidity = itemView.findViewById(R.id.textViewRelativeHumidity);
        }
    }
    public void filterPatientList(ArrayList<Patient> filteredPatientList) {
        list = filteredPatientList;
        notifyDataSetChanged();
    }

}
