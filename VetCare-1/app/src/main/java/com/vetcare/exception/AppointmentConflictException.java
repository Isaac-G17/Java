/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.exception;

/**
 *
 * @author Gu_Zra
 */
public class AppointmentConflictException extends BusinessException {
    public AppointmentConflictException(String message) {
        super(message);
        // Ej: new AppointmentConflictException("El veterinario ya tiene una cita en esa fecha y hora")
    }
}
