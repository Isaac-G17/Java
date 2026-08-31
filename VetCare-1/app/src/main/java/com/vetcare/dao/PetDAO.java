/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao;

import com.vetcare.model.Pet;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public interface PetDAO {
    Pet save(Pet pet); 
    List<Pet> findAll(); 
    Pet update(Pet pet);
    void changeStatus(int id, boolean status);
    Optional<Pet> findByName(String name);
    List <Pet> findByOwnerId( int ownerId);
    Optional<Pet> findById(int id);
}
