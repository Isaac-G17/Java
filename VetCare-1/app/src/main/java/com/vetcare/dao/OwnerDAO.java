/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao;

import com.vetcare.model.Owner;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public interface OwnerDAO {
    Owner save(Owner owner); //Register Owner 
    List<Owner> findAll(); // Consult Owner
    Owner update(Owner owner);// Update Owner
    void changeStatus(int id, boolean status); // changes Status owner 
    Optional<Owner> findByIdentificationNumber(String identificationNumber); // Search Owner
    Optional<Owner> findById(int id);     
}
