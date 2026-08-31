/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.service.impl;

import com.vetcare.dao.MedicationDAO;
import com.vetcare.exception.MedicineNotFoundException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Medication;
import com.vetcare.service.MedicationService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public class MedicationServiceImpl implements MedicationService {

    private final MedicationDAO medicationDAO;

    public MedicationServiceImpl(MedicationDAO medicationDAO) {
        this.medicationDAO = medicationDAO;
    }

    @Override
    public Medication registerMedication(Medication medication) {
        // Regla: "El código debe ser único"
        boolean duplicated = medicationDAO.findAll().stream()
                .anyMatch(m -> m.getCode().equalsIgnoreCase(medication.getCode()));
        if (duplicated) {
            throw new ValidationException("Ya existe un medicamento con el código: " + medication.getCode());
        }

        // Regla: "El nombre y la presentación son obligatorios"
        if (medication.getName() == null || medication.getName().isBlank()) {
            throw new ValidationException("El nombre del medicamento es obligatorio");
        }
        if (medication.getPresentation() == null || medication.getPresentation().isBlank()) {
            throw new ValidationException("La presentación del medicamento es obligatoria");
        }

        // Regla: "La cantidad disponible no puede ser negativa"
        if (medication.getAvailableQuantity() < 0) {
            throw new ValidationException("La cantidad disponible no puede ser negativa");
        }

        // Regla: "El precio debe ser mayor que cero"
        if (medication.getPrice() == null || medication.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El precio debe ser mayor que cero");
        }

        medication.setStatus(true);
        medication.setRegistrationDate(LocalDateTime.now());

        return medicationDAO.save(medication);
    }

    @Override
    public List<Medication> getAllMedications() {
        return medicationDAO.findAll();
    }

    @Override
    public Medication updateMedication(Medication medication) {
        medicationDAO.findById(medication.getId())
                .orElseThrow(() -> new MedicineNotFoundException(medication.getId()));

        if (medication.getPrice() == null || medication.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El precio debe ser mayor que cero");
        }

        return medicationDAO.update(medication);
    }

    @Override
    public void changeMedicationStatus(int id, boolean status) {
        medicationDAO.changeStatus(id, status);
    }

    @Override
    public Medication updateStock(int id, int newAvailableQuantity) {
        // Regla: "La cantidad disponible no puede ser negativa"
        if (newAvailableQuantity < 0) {
            throw new ValidationException("La cantidad disponible no puede ser negativa");
        }

        medicationDAO.findById(id)
                .orElseThrow(() -> new MedicineNotFoundException(id));

        return medicationDAO.updateStock(id, newAvailableQuantity);
    }

    @Override
    public List<Medication> getLowStockMedications() {
        return medicationDAO.findLowStock();
    }
}
