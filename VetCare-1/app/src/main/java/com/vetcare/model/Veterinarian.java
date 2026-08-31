/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.model;

/**
 *
 * @author Cohorte 5
 */
public class Veterinarian {
    private int id;
    private String professionalLicense;
    private User user;
    private Specialty specialty;

    public Veterinarian(){}

    public Veterinarian(int id, String professionalLicense, User user, Specialty specialty) {
        this.id = id;
        this.professionalLicense = professionalLicense;
        this.user = user;
        this.specialty = specialty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfessionalLicense() {
        return professionalLicense;
    }

    public void setProfessionalLicense(String professionalLicense) {
        this.professionalLicense = professionalLicense;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }
}
