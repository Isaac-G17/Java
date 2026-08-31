/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.controller;

import com.vetcare.exception.BusinessException;
import com.vetcare.exception.PersistenceException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Specialty;
import com.vetcare.model.User;
import com.vetcare.model.Veterinarian;
import com.vetcare.service.SpecialtyService;
import com.vetcare.service.UserService;
import com.vetcare.service.VeterinarianService;
import com.vetcare.view.VeterinarianView;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public class VeterinarianController {

    private final VeterinarianService veterinarianService;
    private final UserService userService;
    private final SpecialtyService specialtyService; 
    private final VeterinarianView veterinarianView;

    public VeterinarianController(VeterinarianService veterinarianService, UserService userService, SpecialtyService specialtyService) {
        this.veterinarianService = veterinarianService;
        this.userService = userService;
        this.specialtyService = specialtyService;
        this.veterinarianView = new VeterinarianView();
    }

    public void show() {
        boolean back = false;
        while (!back) {
            String choice = veterinarianView.showMenu();

            if (choice == null || choice.equals("Volver")) {
                back = true;
                continue;
            }

            try {
                switch (choice) {
                    case "Registrar" -> registerVeterinarian();
                    case "Listar" -> listVeterinarians();
                    case "Filtrar por especialidad" -> findBySpecialty();
                    case "Activar/Desactivar" -> changeStatus();
                }
            } catch (ValidationException | BusinessException e) {
                veterinarianView.showError(e.getMessage());
            } catch (PersistenceException e) {
                veterinarianView.showTechnicalError();
            } catch (Exception e) {
                veterinarianView.showUnexpectedError();
            }
        }
    }

    private void registerVeterinarian() {
        List<User> users = userService.getAllUsers();
        List<Specialty> specialties = specialtyService.getAllSpecialties();

        Veterinarian veterinarian = veterinarianView.captureVeterinarianData(users, specialties);
        if (veterinarian == null) return;

        Veterinarian saved = veterinarianService.registerVeterinarian(veterinarian);
        veterinarianView.showSuccess("Veterinario registrado con id: " + saved.getId());
    }

    private void listVeterinarians() {
        veterinarianView.showVeterinariansList(veterinarianService.getAllVeterinarians());
    }

    private void findBySpecialty() {
        Integer specialtyId = veterinarianView.askSpecialtyId();
        if (specialtyId == null) return;
        veterinarianView.showVeterinariansList(veterinarianService.findBySpecialty(specialtyId));
    }

    private void changeStatus() {
        Integer id = veterinarianView.askVeterinarianId();
        if (id == null) return;
        boolean newStatus = veterinarianView.askNewStatus();
        veterinarianService.changeVeterinarianStatus(id, newStatus);
        veterinarianView.showSuccess("Estado actualizado");
    }
}
