/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.view;

import com.vetcare.enums.IdentificationType;
import com.vetcare.model.Owner;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Gu_Zra
 */
public class OwnerView {

    public String showMenu() {
        String[] options = {"Registrar", "Listar", "Buscar por documento", "Activar/Desactivar", "Volver"};
        return (String) JOptionPane.showInputDialog(
                null, "Gestión de propietarios", "Propietarios",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]
        );
    }

    public Owner captureOwnerData() {
        IdentificationType idType = (IdentificationType) JOptionPane.showInputDialog(null, "Tipo de identificación:",
                "Registrar propietario", JOptionPane.QUESTION_MESSAGE, null,
                IdentificationType.values(), IdentificationType.CC);
        if (idType == null) return null;

        String idNumber = JOptionPane.showInputDialog("Número de identificación:");
        if (idNumber == null) return null;
        String firstName = JOptionPane.showInputDialog("Nombres:");
        if (firstName == null) return null;
        String lastName = JOptionPane.showInputDialog("Apellidos:");
        if (lastName == null) return null;
        String phone = JOptionPane.showInputDialog("Teléfono:");
        if (phone == null) return null;
        String email = JOptionPane.showInputDialog("Correo:");
        if (email == null) return null;
        String address = JOptionPane.showInputDialog("Dirección:");
        if (address == null) return null;

        Owner owner = new Owner();
        owner.setIdentificationType(idType);
        owner.setIdentificationNumber(idNumber);
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setPhoneNumber(phone);
        owner.setEmail(email);
        owner.setAddress(address);
        return owner;
    }

    public String askIdentificationNumber() {
        return JOptionPane.showInputDialog("Número de identificación a buscar:");
    }

    public Integer askOwnerId() {
        String idStr = JOptionPane.showInputDialog("ID del propietario:");
        if (idStr == null) return null;
        return Integer.parseInt(idStr);
    }

    public boolean askNewStatus() {
        int confirm = JOptionPane.showConfirmDialog(null, "¿Activar (Sí) o Desactivar (No)?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }

    public void showOwnersList(List<Owner> owners) {
        StringBuilder sb = new StringBuilder();
        for (Owner o : owners) {
            sb.append(o.getId()).append(" - ").append(o.getFirstName()).append(" ").append(o.getLastName())
                    .append(" - ").append(o.getIdentificationNumber())
                    .append(" - ").append(o.getStatus() ? "ACTIVO" : "INACTIVO").append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No hay propietarios registrados");
    }

    public void showOwnerDetail(Owner owner) {
        JOptionPane.showMessageDialog(null,
                owner.getFirstName() + " " + owner.getLastName() + " - " + owner.getEmail());
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.WARNING_MESSAGE);
    }

    public void showTechnicalError() {
        JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showUnexpectedError() {
        JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
