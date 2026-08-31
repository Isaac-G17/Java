/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.controller;

import com.vetcare.exception.BusinessException;
import com.vetcare.exception.PersistenceException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Medication;
import com.vetcare.service.MedicationService;
import com.vetcare.view.MedicationView;

/**
 *
 * @author Gu_Zra
 */
public class MedicationController {

    private final MedicationService medicationService;
    private final MedicationView medicationView;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
        this.medicationView = new MedicationView();
    }

    public void show() {
        boolean back = false;
        while (!back) {
            String choice = medicationView.showMenu();

            if (choice == null || choice.equals("Volver")) {
                back = true;
                continue;
            }

            try {
                switch (choice) {
                    case "Registrar" -> registerMedication();
                    case "Listar" -> listMedications();
                    case "Actualizar existencias" -> updateStock();
                    case "Activar/Desactivar" -> changeStatus();
                    case "Ver bajo inventario" -> viewLowStock();
                }
            } catch (ValidationException | BusinessException e) {
                medicationView.showError(e.getMessage());
            } catch (PersistenceException e) {
                medicationView.showTechnicalError();
            } catch (Exception e) {
                medicationView.showUnexpectedError();
            }
        }
    }

    private void registerMedication() {
        Medication medication = medicationView.captureMedicationData();
        if (medication == null) return;
        Medication saved = medicationService.registerMedication(medication);
        medicationView.showSuccess("Medicamento registrado con id: " + saved.getId());
    }

    private void listMedications() {
        medicationView.showMedicationsList(medicationService.getAllMedications());
    }

    private void updateStock() {
        Integer id = medicationView.askMedicationId();
        if (id == null) return;
        Integer newQuantity = medicationView.askNewQuantity();
        if (newQuantity == null) return;
        medicationService.updateStock(id, newQuantity);
        medicationView.showSuccess("Inventario actualizado");
    }

    private void changeStatus() {
        Integer id = medicationView.askMedicationId();
        if (id == null) return;
        boolean newStatus = medicationView.askNewStatus();
        medicationService.changeMedicationStatus(id, newStatus);
        medicationView.showSuccess("Estado actualizado");
    }

    private void viewLowStock() {
        medicationView.showMedicationsList(medicationService.getLowStockMedications());
    }
}
