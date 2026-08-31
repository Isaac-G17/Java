/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.view;

import com.vetcare.enums.IdentificationType;
import com.vetcare.model.Role;
import com.vetcare.model.User;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Gu_Zra
 */
public class UserView {

    public String showMenu() {
        String[] options = {"Registrar", "Listar", "Activar/Desactivar", "Volver"};
        return (String) JOptionPane.showInputDialog(
                null, "Gestión de usuarios", "Usuarios",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]
        );
    }

    public User captureUserData(List<Role> roles) {
        IdentificationType idType = (IdentificationType) JOptionPane.showInputDialog(null, "Tipo de identificación:",
                "Registrar usuario", JOptionPane.QUESTION_MESSAGE, null,
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
        String password = JOptionPane.showInputDialog("Contraseña:");
        if (password == null) return null;

        String[] roleNames = roles.stream()
                .map(r -> r.getId() + " - " + r.getName())
                .toArray(String[]::new);
        String selectedRole = (String) JOptionPane.showInputDialog(null, "Selecciona el rol:",
                "Registrar usuario", JOptionPane.QUESTION_MESSAGE, null, roleNames, roleNames[0]);
        if (selectedRole == null) return null;
        int roleId = Integer.parseInt(selectedRole.split(" - ")[0]);
        Role role = roles.stream().filter(r -> r.getId() == roleId).findFirst().orElse(null);

        User user = new User();
        user.setIdentificationType(idType);
        user.setIdentificationNumber(idNumber);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phone);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }

    public Integer askUserId() {
        String idStr = JOptionPane.showInputDialog("ID del usuario:");
        if (idStr == null) return null;
        return Integer.parseInt(idStr);
    }

    public boolean askNewStatus() {
        int confirm = JOptionPane.showConfirmDialog(null, "¿Activar (Sí) o Desactivar (No)?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }

    public void showUsersList(List<User> users) {
        StringBuilder sb = new StringBuilder();
        for (User u : users) {
            sb.append(u.getId()).append(" - ").append(u.getFirstName()).append(" ").append(u.getLastName())
                    .append(" - ").append(u.getEmail())
                    .append(" - ").append(u.getRole().getName())
                    .append(" - ").append(u.getStatus() ? "ACTIVO" : "INACTIVO").append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No hay usuarios registrados");
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
