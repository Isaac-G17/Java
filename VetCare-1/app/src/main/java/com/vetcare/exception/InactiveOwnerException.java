/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.exception;

/**
 *
 * @author Gu_Zra
 */
public class InactiveOwnerException extends BusinessException {
    public InactiveOwnerException(int ownerId) {
        super("El propietario con id " + ownerId + " está inactivo y no puede realizar esta acción");
    }
}