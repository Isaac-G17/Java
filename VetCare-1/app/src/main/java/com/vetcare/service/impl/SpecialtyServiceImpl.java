/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.service.impl;

import com.vetcare.dao.SpecialtyDAO;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Specialty;
import com.vetcare.service.SpecialtyService;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyDAO specialtyDAO;

    public SpecialtyServiceImpl(SpecialtyDAO specialtyDAO) {
        this.specialtyDAO = specialtyDAO;
    }

    @Override
    public Specialty registerSpecialty(Specialty specialty) {
        // Regla: "El nombre de la especialidad es obligatorio"
        if (specialty.getName() == null || specialty.getName().isBlank()) {
            throw new ValidationException("El nombre de la especialidad es obligatorio");
        }

        // Regla: "El nombre de la especialidad debe ser único"
        boolean duplicated = specialtyDAO.findAll().stream()
                .anyMatch(s -> s.getName().equalsIgnoreCase(specialty.getName()));
        if (duplicated) {
            throw new ValidationException("Ya existe una especialidad con ese nombre");
        }

        return specialtyDAO.save(specialty);
    }

    @Override
    public List<Specialty> getAllSpecialties() {
        return specialtyDAO.findAll();
    }
}
