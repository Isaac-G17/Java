/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Gu_Zra
 */
public class Medication {
    private int id;
    private String code;
    private String name;
    private String presentation;
    private String laboratory;
    private int availableQuantity;
    private int minimumQuantity;
    private BigDecimal price;
    private boolean status;
    private LocalDateTime registrationDate;

    public Medication() {
    }

    public Medication(int id, String code, String name, String presentation, 
            String laboratory, int availableQuantity, int minimumQuantity,
            BigDecimal price, boolean status, LocalDateTime registrationDate){
        
        this.id = id;
        this.code = code;
        this.name = name;
        this.presentation = presentation;
        this.laboratory = laboratory;
        this.availableQuantity = availableQuantity;
        this.minimumQuantity = minimumQuantity;
        this.price = price;
        this.status = status;
        this.registrationDate = registrationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    } 
}
