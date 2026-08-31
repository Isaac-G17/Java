/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao;

import com.vetcare.model.Medication;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public interface MedicationDAO {
    Medication save(Medication medication); 
    List<Medication> findAll(); 
    Medication update(Medication medication);
    Medication updateStock(int id, int newAvailableQuantit);
    void changeStatus(int id, boolean status);
    List<Medication> findLowStock();
    Optional<Medication> findById(int id);
}
