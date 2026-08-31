/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao;

import com.vetcare.model.Specialty;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public interface SpecialtyDAO {
    Specialty save(Specialty specialty);
    Optional<Specialty> findById(int id);
    List<Specialty> findAll();
}