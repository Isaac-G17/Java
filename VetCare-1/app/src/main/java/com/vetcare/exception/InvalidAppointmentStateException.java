/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.exception;

/**
 *
 * @author Gu_Zra
 */
public class InvalidAppointmentStateException extends BusinessException {
    public InvalidAppointmentStateException(String message) {
        super(message);
        // Ej: new InvalidAppointmentStateException("Una cita cancelada no puede iniciar atención")
    }
}
