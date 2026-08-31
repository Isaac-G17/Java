/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.model;

/**
 *
 * @author Gu_Zra
 */
public class AttentionDetails {
    private int id;
    private int quantity;
    private Attention attention;
    private Medication medication;

    public AttentionDetails() {
    }

    public AttentionDetails(int id, int quantity, Attention attention, Medication medication) {
        this.id = id;
        this.quantity = quantity;
        this.attention = attention;
        this.medication = medication;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Attention getAttention() {
        return attention;
    }

    public void setAttention(Attention attention) {
        this.attention = attention;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }   
}
