/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.exception;

/**
 *
 * @author Gu_Zra
 */
public class VeterinarianNotAvailableException extends BusinessException {
    public VeterinarianNotAvailableException(int veterinarianId) {
        super("El veterinario con id " + veterinarianId + " no está disponible (inactivo)");
    }
}
