/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao;

import com.vetcare.model.Veterinarian;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public interface VeterinarianDAO {
    Veterinarian save(Veterinarian veterinarian); 
    List<Veterinarian> findAll(); 
    Veterinarian update(Veterinarian veterinarian);
    void changeStatus(int id, boolean status);
    List<Veterinarian> findBySpecialty(int specialtyId);
}
