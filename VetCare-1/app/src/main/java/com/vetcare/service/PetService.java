/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vetcare.service;

import com.vetcare.model.Pet;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public interface PetService {
    Pet registerPet(Pet pet);
    List<Pet> getAllPets();
    Pet updatePet(Pet pet);
    void changePetStatus(int id, boolean status);
    Pet findByName(String name);
    List<Pet> findByOwnerId(int ownerId);
}
