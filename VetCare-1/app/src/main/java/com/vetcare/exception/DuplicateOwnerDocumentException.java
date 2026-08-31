/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.exception;

/**
 *
 * @author Gu_Zra
 */
public class DuplicateOwnerDocumentException extends BusinessException {
    public DuplicateOwnerDocumentException(String identificationNumber) {
        super("Ya existe un propietario con el documento: " + identificationNumber);
    }
}
