/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.exception;

/**
 *
 * @author Gu_Zra
 */
public class MedicineNotFoundException extends BusinessException {
    public MedicineNotFoundException(int id) {
        super("No se encontró el medicamento con id: " + id);
    }
}