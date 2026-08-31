/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vetcare.service;

import com.vetcare.model.Specialty;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public interface SpecialtyService {
    Specialty registerSpecialty(Specialty specialty);
    List<Specialty> getAllSpecialties();
}
