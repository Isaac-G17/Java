/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.service.impl;

import com.vetcare.dao.UserDAO;
import com.vetcare.dao.VeterinarianDAO;
import com.vetcare.exception.BusinessException;
import com.vetcare.exception.DuplicateVeterinarianLicenseException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Veterinarian;
import com.vetcare.service.VeterinarianService;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public class VeterinarianServiceImpl implements VeterinarianService {

    private final VeterinarianDAO veterinarianDAO;
    private final UserDAO userDAO;

    public VeterinarianServiceImpl(VeterinarianDAO veterinarianDAO, UserDAO userDAO) {
        this.veterinarianDAO = veterinarianDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Veterinarian registerVeterinarian(Veterinarian veterinarian) {
        if (veterinarian.getProfessionalLicense() == null || veterinarian.getProfessionalLicense().isBlank()) {
            throw new ValidationException("La tarjeta profesional es obligatoria");
        }

        // Regla: "La tarjeta profesional debe ser única"
        boolean duplicated = veterinarianDAO.findAll().stream()
                .anyMatch(v -> v.getProfessionalLicense().equalsIgnoreCase(veterinarian.getProfessionalLicense()));
        if (duplicated) {
            throw new DuplicateVeterinarianLicenseException(veterinarian.getProfessionalLicense());
        }

        return veterinarianDAO.save(veterinarian);
    }

    @Override
    public List<Veterinarian> getAllVeterinarians() {
        return veterinarianDAO.findAll();
    }

    @Override
    public Veterinarian updateVeterinarian(Veterinarian veterinarian) {
        boolean exists = veterinarianDAO.findAll().stream()
                .anyMatch(v -> v.getId() == veterinarian.getId());
        if (!exists) {
            throw new BusinessException("No se encontró el veterinario con id: " + veterinarian.getId());
        }

        return veterinarianDAO.update(veterinarian);
    }

    @Override
    public void changeVeterinarianStatus(int id, boolean status) {
        // El status vive en "users", así que se delega al DAO que ya resuelve
        // internamente el UPDATE sobre la tabla correcta a través del id_user
        veterinarianDAO.changeStatus(id, status);
    }

    @Override
    public List<Veterinarian> findBySpecialty(int specialtyId) {
        return veterinarianDAO.findBySpecialty(specialtyId);
    }
}