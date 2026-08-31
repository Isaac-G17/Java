/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vetcare.service;

import com.vetcare.model.Owner;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public interface OwnerService {
    Owner registerOwner(Owner owner);
    List<Owner> getAllOwners();
    Owner updateOwner(Owner owner);
    void changeOwnerStatus(int id, boolean status);
    Owner findByIdentificationNumber(String identificationNumber);
}
