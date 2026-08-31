/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.exception;

/**
 *
 * @author Gu_Zra
 */
public class UnauthorizedActionException extends BusinessException {
    public UnauthorizedActionException(String action) {
        super("No tienes permisos para realizar esta acción: " + action);
    }
}
