/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.controller;

import com.vetcare.exception.BusinessException;
import com.vetcare.exception.PersistenceException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Owner;
import com.vetcare.model.Pet;
import com.vetcare.service.OwnerService;
import com.vetcare.service.PetService;
import com.vetcare.view.PetView;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public class PetController {

    private final PetService petService;
    private final OwnerService ownerService;
    private final PetView petView;

    public PetController(PetService petService, OwnerService ownerService) {
        this.petService = petService;
        this.ownerService = ownerService;
        this.petView = new PetView();
    }

    public void show() {
        boolean back = false;
        while (!back) {
            String choice = petView.showMenu();

            if (choice == null || choice.equals("Volver")) {
                back = true;
                continue;
            }

            try {
                switch (choice) {
                    case "Registrar" -> registerPet();
                    case "Listar" -> listPets();
                    case "Buscar por nombre" -> findByName();
                    case "Buscar por propietario" -> findByOwner();
                    case "Activar/Desactivar" -> changeStatus();
                }
            } catch (ValidationException | BusinessException e) {
                petView.showError(e.getMessage());
            } catch (PersistenceException e) {
                petView.showTechnicalError();
            } catch (Exception e) {
                petView.showUnexpectedError();
            }
        }
    }

    private void registerPet() {
        List<Owner> owners = ownerService.getAllOwners();
        Pet pet = petView.capturePetData(owners);
        if (pet == null) return;

        Pet saved = petService.registerPet(pet);
        petView.showSuccess("Mascota registrada con id: " + saved.getId());
    }

    private void listPets() {
        petView.showPetsList(petService.getAllPets());
    }

    private void findByName() {
        String name = petView.askPetName();
        if (name == null) return;
        Pet pet = petService.findByName(name);
        petView.showPetDetail(pet);
    }

    private void findByOwner() {
        Integer ownerId = petView.askOwnerId();
        if (ownerId == null) return;
        petView.showPetsList(petService.findByOwnerId(ownerId));
    }

    private void changeStatus() {
        Integer id = petView.askPetId();
        if (id == null) return;
        boolean newStatus = petView.askNewStatus();
        petService.changePetStatus(id, newStatus);
        petView.showSuccess("Estado actualizado");
    }
}
