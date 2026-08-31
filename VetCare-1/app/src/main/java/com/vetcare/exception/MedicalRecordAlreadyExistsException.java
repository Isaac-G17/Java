/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.exception;

/**
 *
 * @author Gu_Zra
 */
public class MedicalRecordAlreadyExistsException extends BusinessException {
    public MedicalRecordAlreadyExistsException(int appointmentId) {
        super("La cita con id " + appointmentId + " ya tiene una atención médica registrada");
    }
}