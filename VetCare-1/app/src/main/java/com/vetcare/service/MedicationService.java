/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vetcare.service;

import com.vetcare.model.Medication;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public interface MedicationService {
    Medication registerMedication(Medication medication);
    List<Medication> getAllMedications();
    Medication updateMedication(Medication medication);
    void changeMedicationStatus(int id, boolean status);
    Medication updateStock(int id, int newAvailableQuantity);
    List<Medication> getLowStockMedications();
}
