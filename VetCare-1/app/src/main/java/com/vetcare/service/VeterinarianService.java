/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vetcare.service;

import com.vetcare.model.Veterinarian;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public interface VeterinarianService {
    Veterinarian registerVeterinarian(Veterinarian veterinarian);
    List<Veterinarian> getAllVeterinarians();
    Veterinarian updateVeterinarian(Veterinarian veterinarian);
    void changeVeterinarianStatus(int id, boolean status);
    List<Veterinarian> findBySpecialty(int specialtyId);
}
