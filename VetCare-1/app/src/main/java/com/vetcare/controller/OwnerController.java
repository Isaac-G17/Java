/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.controller;

import com.vetcare.exception.BusinessException;
import com.vetcare.exception.PersistenceException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Owner;
import com.vetcare.service.OwnerService;
import com.vetcare.view.OwnerView;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public class OwnerController {

    private final OwnerService ownerService;
    private final OwnerView ownerView;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
        this.ownerView = new OwnerView();
    }

    public void show() {
        boolean back = false;
        while (!back) {
            String choice = ownerView.showMenu();

            if (choice == null || choice.equals("Volver")) {
                back = true;
                continue;
            }

            try {
                switch (choice) {
                    case "Registrar" -> registerOwner();
                    case "Listar" -> listOwners();
                    case "Buscar por documento" -> findOwner();
                    case "Activar/Desactivar" -> changeStatus();
                }
            } catch (ValidationException | BusinessException e) {
                ownerView.showError(e.getMessage());
            } catch (PersistenceException e) {
                ownerView.showTechnicalError();
            } catch (Exception e) {
                ownerView.showUnexpectedError();
            }
        }
    }

    private void registerOwner() {
        Owner owner = ownerView.captureOwnerData();
        if (owner == null) return;

        Owner saved = ownerService.registerOwner(owner);
        ownerView.showSuccess("Propietario registrado con id: " + saved.getId());
    }

    private void listOwners() {
        List<Owner> owners = ownerService.getAllOwners();
        ownerView.showOwnersList(owners);
    }

    private void findOwner() {
        String idNumber = ownerView.askIdentificationNumber();
        if (idNumber == null) return;
        Owner owner = ownerService.findByIdentificationNumber(idNumber);
        ownerView.showOwnerDetail(owner);
    }

    private void changeStatus() {
        Integer id = ownerView.askOwnerId();
        if (id == null) return;
        boolean newStatus = ownerView.askNewStatus();
        ownerService.changeOwnerStatus(id, newStatus);
        ownerView.showSuccess("Estado actualizado");
    }
}
