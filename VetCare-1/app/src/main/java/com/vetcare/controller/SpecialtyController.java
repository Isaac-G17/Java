/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.controller;

import com.vetcare.exception.BusinessException;
import com.vetcare.exception.PersistenceException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Specialty;
import com.vetcare.service.SpecialtyService;
import com.vetcare.view.SpecialtyView;

/**
 *
 * @author Gu_Zra
 */
public class SpecialtyController {

    private final SpecialtyService specialtyService;
    private final SpecialtyView specialtyView;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
        this.specialtyView = new SpecialtyView();
    }

    public void show() {
        boolean back = false;
        while (!back) {
            String choice = specialtyView.showMenu();

            if (choice == null || choice.equals("Volver")) {
                back = true;
                continue;
            }

            try {
                switch (choice) {
                    case "Registrar" -> registerSpecialty();
                    case "Listar" -> listSpecialties();
                }
            } catch (ValidationException | BusinessException e) {
                specialtyView.showError(e.getMessage());
            } catch (PersistenceException e) {
                specialtyView.showTechnicalError();
            } catch (Exception e) {
                specialtyView.showUnexpectedError();
            }
        }
    }

    private void registerSpecialty() {
        Specialty specialty = specialtyView.captureSpecialtyData();
        if (specialty == null) return;

        Specialty saved = specialtyService.registerSpecialty(specialty);
        specialtyView.showSuccess("Especialidad registrada con id: " + saved.getId());
    }

    private void listSpecialties() {
        specialtyView.showSpecialtiesList(specialtyService.getAllSpecialties());
    }
}
