/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.exception;

/**
 *
 * @author Gu_Zra
 */
public class DuplicateVeterinarianLicenseException extends BusinessException {
    public DuplicateVeterinarianLicenseException(String license) {
        super("Ya existe un veterinario con la tarjeta profesional: " + license);
    }
}
