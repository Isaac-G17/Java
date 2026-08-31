/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.service.impl;

import com.vetcare.dao.OwnerDAO;
import com.vetcare.exception.DuplicateOwnerDocumentException;
import com.vetcare.exception.OwnerNotFoundException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Owner;
import com.vetcare.service.OwnerService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public class OwnerServiceImpl implements OwnerService {

    private final OwnerDAO ownerDAO;

    public OwnerServiceImpl(OwnerDAO ownerDAO) {
        this.ownerDAO = ownerDAO;
    }

    @Override
    public Owner registerOwner(Owner owner) {
        // Regla: "La identificación debe ser única"
        Optional<Owner> existing = ownerDAO.findByIdentificationNumber(owner.getIdentificationNumber());
        if (existing.isPresent()) {
            throw new DuplicateOwnerDocumentException(owner.getIdentificationNumber());
        }

        // Regla: "El nombre y el teléfono son obligatorios"
        if (owner.getFirstName() == null || owner.getFirstName().isBlank()) {
            throw new ValidationException("El nombre del propietario es obligatorio");
        }
        if (owner.getPhoneNumber() == null || owner.getPhoneNumber().isBlank()) {
            throw new ValidationException("El teléfono del propietario es obligatorio");
        }

        owner.setStatus(true); // todo propietario nuevo nace activo
        owner.setRegistrationDate(LocalDateTime.now());

        return ownerDAO.save(owner);
    }

    @Override
    public List<Owner> getAllOwners() {
        return ownerDAO.findAll();
    }

    @Override
    public Owner updateOwner(Owner owner) {
        // Verifica que el propietario exista por su ID (no por el documento,
        // que podría estar siendo modificado en esta misma operación)
        ownerDAO.findById(owner.getId())
                .orElseThrow(() -> new OwnerNotFoundException(owner.getId()));

        return ownerDAO.update(owner);
    }

    @Override
    public void changeOwnerStatus(int id, boolean status) {
        ownerDAO.changeStatus(id, status);
    }

    @Override
    public Owner findByIdentificationNumber(String identificationNumber) {
    return ownerDAO.findByIdentificationNumber(identificationNumber)
            .orElseThrow(() -> new OwnerNotFoundException(identificationNumber));
    }
}
