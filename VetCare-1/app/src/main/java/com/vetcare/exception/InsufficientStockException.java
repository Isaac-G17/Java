/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.exception;

/**
 *
 * @author Gu_Zra
 */
public class InsufficientStockException extends BusinessException {
    public InsufficientStockException(String medicationName, int available, int requested) {
        super("Inventario insuficiente para " + medicationName + ". Disponible: " 
                + available + ", solicitado: " + requested);
    }
}
