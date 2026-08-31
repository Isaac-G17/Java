/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.exception;

/**
 *
 * @author Gu_Zra
 */
public class OwnerNotFoundException extends BusinessException {
    public OwnerNotFoundException(int id) {
        super("No se encontró el propietario con id: " + id);
    }
    
    public OwnerNotFoundException(String identificationNumber) {
        super("No se encontró el propietario con el documento: " + identificationNumber);
    }
}

