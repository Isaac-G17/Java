/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.model;

import com.vetcare.enums.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author Gu_Zra
 */
public class Appointment {
    private int id;
    private LocalDate date;
    private LocalTime hour;
    private String reason;
    private AppointmentStatus status;
    private LocalDateTime registrationDate;
    private Pet pet;
    private Veterinarian veterinarian;

    public Appointment() {
    }

    public Appointment(int id, LocalDate date, LocalTime hour, String reason, 
            AppointmentStatus status, LocalDateTime registrationDate,
            Pet pet, Veterinarian veterinarian) {
        
        this.id = id;
        this.date = date;
        this.hour = hour;
        this.reason = reason;
        this.status = status;
        this.registrationDate = registrationDate;
        this.pet = pet;
        this.veterinarian = veterinarian;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHour() {
        return hour;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Veterinarian getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(Veterinarian veterinarian) {
        this.veterinarian = veterinarian;
    }
}
