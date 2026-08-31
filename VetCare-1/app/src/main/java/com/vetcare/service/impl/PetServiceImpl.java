/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.service.impl;

import com.vetcare.dao.OwnerDAO;
import com.vetcare.dao.PetDAO;
import com.vetcare.exception.InactiveOwnerException;
import com.vetcare.exception.OwnerNotFoundException;
import com.vetcare.exception.PetNotFoundException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Owner;
import com.vetcare.model.Pet;
import com.vetcare.service.PetService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public class PetServiceImpl implements PetService {

    private final PetDAO petDAO;
    private final OwnerDAO ownerDAO;

    public PetServiceImpl(PetDAO petDAO, OwnerDAO ownerDAO) {
        this.petDAO = petDAO;
        this.ownerDAO = ownerDAO;
    }

    @Override
    public Pet registerPet(Pet pet) {
        // Regla: "Toda mascota debe pertenecer a un propietario"
        // Regla: "El propietario debe encontrarse activo"
        Owner owner = ownerDAO.findById(pet.getOwner().getId())
                .orElseThrow(() -> new OwnerNotFoundException(pet.getOwner().getId()));

        if (!owner.getStatus()) {
            throw new InactiveOwnerException(owner.getId());
        }

        // Regla: "El peso debe ser mayor que cero"
        if (pet.getWeight() == null || pet.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El peso de la mascota debe ser mayor que cero");
        }

        // Regla: "La fecha de nacimiento no puede ser posterior a la fecha actual"
        if (pet.getDateOfBirth() != null && pet.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new ValidationException("La fecha de nacimiento no puede ser posterior a hoy");
        }

        // Regla: "No se debe registrar dos veces la misma mascota para el mismo
        // propietario utilizando el mismo nombre y fecha de nacimiento"
        boolean duplicated = petDAO.findByOwnerId(owner.getId()).stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(pet.getName())
                        && p.getDateOfBirth().equals(pet.getDateOfBirth()));

        if (duplicated) {
            throw new ValidationException(
                "Ya existe una mascota registrada con ese nombre y fecha de nacimiento para este propietario"
            );
        }

        pet.setStatus(true); // toda mascota nueva nace activa
        pet.setRegistrationDate(LocalDateTime.now());
        pet.setOwner(owner);

        return petDAO.save(pet);
    }

    @Override
    public List<Pet> getAllPets() {
        return petDAO.findAll();
    }

    @Override
    public Pet updatePet(Pet pet) {
        petDAO.findById(pet.getId())
                .orElseThrow(() -> new PetNotFoundException(pet.getId()));

        if (pet.getWeight() == null || pet.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El peso de la mascota debe ser mayor que cero");
        }

        return petDAO.update(pet);
    }

    @Override
    public void changePetStatus(int id, boolean status) {
        petDAO.changeStatus(id, status);
    }

    @Override
    public Pet findByName(String name) {
        return petDAO.findByName(name)
                .orElseThrow(() -> new PetNotFoundException(name));
    }

    @Override
    public List<Pet> findByOwnerId(int ownerId) {
        return petDAO.findByOwnerId(ownerId);
    }
}
