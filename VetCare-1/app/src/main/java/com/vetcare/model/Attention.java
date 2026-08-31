/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.model;

import com.vetcare.enums.AttentionStatus;
import java.time.LocalDateTime;

/**
 *
 * @author Gu_Zra
 */
public class Attention {
    private int id;
    private String symptoms;
    private String diagnosis;
    private String treatment;
    private String observation;
    private LocalDateTime dateAttention;
    private AttentionStatus status;
    private Veterinarian veterinarian;
    private Appointment appointment;
    private Pet pet;

    public Attention() {
    }

    public Attention(int id, String symptoms, String diagnosis, String treatment,
            String observation, LocalDateTime dateAttention, 
            AttentionStatus status, Veterinarian veterinarian, 
            Appointment appointment, Pet pet) {
        
        this.id = id;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.observation = observation;
        this.dateAttention = dateAttention;
        this.status = status;
        this.veterinarian = veterinarian;
        this.appointment = appointment;
        this.pet = pet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDateTime getDateAttention() {
        return dateAttention;
    }

    public void setDateAttention(LocalDateTime dateAttention) {
        this.dateAttention = dateAttention;
    }

    public AttentionStatus getStatus() {
        return status;
    }

    public void setStatus(AttentionStatus status) {
        this.status = status;
    }

    public Veterinarian getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(Veterinarian veterinarian) {
        this.veterinarian = veterinarian;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
