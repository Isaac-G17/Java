/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.exception;

/**
 *
 * @author Gu_Zra
 */
public class PetNotFoundException extends BusinessException {
    public PetNotFoundException(int id) {
        super("No se encontró la mascota con id: " + id);
    }
    
    public PetNotFoundException(String name) {
        super("No se encontró la mascota con nombre: " + name);
    }
}
