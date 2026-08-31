/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.view;

import com.vetcare.model.Specialty;
import com.vetcare.model.User;
import com.vetcare.model.Veterinarian;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Gu_Zra
 */
public class VeterinarianView {

    public String showMenu() {
        String[] options = {"Registrar", "Listar", "Filtrar por especialidad", "Activar/Desactivar", "Volver"};
        return (String) JOptionPane.showInputDialog(
                null, "Gestión de veterinarios", "Veterinarios",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]
        );
    }

    public Veterinarian captureVeterinarianData(List<User> users, List<Specialty> specialties) {
        String[] userNames = users.stream()
                .filter(u -> u.getRole().getName().equals("VETERINARIO"))
                .map(u -> u.getId() + " - " + u.getFirstName() + " " + u.getLastName())
                .toArray(String[]::new);

        if (userNames.length == 0) {
            JOptionPane.showMessageDialog(null, "No hay usuarios con rol VETERINARIO disponibles. Crea uno primero.");
            return null;
        }

        String selectedUser = (String) JOptionPane.showInputDialog(null, "Selecciona el usuario:",
                "Registrar veterinario", JOptionPane.QUESTION_MESSAGE, null, userNames, userNames[0]);
        if (selectedUser == null) return null;
        int userId = Integer.parseInt(selectedUser.split(" - ")[0]);
        User user = users.stream().filter(u -> u.getId() == userId).findFirst().orElse(null);

        String[] specialtyNames = specialties.stream()
                .map(s -> s.getId() + " - " + s.getName())
                .toArray(String[]::new);

        if (specialtyNames.length == 0) {
            JOptionPane.showMessageDialog(null, "No hay especialidades registradas. Crea una primero.");
            return null;
        }

        String selectedSpecialty = (String) JOptionPane.showInputDialog(null, "Selecciona la especialidad:",
                "Registrar veterinario", JOptionPane.QUESTION_MESSAGE, null, specialtyNames, specialtyNames[0]);
        if (selectedSpecialty == null) return null;
        int specialtyId = Integer.parseInt(selectedSpecialty.split(" - ")[0]);
        Specialty specialty = specialties.stream().filter(s -> s.getId() == specialtyId).findFirst().orElse(null);

        String license = JOptionPane.showInputDialog("Tarjeta profesional:");
        if (license == null) return null;

        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setUser(user);
        veterinarian.setSpecialty(specialty);
        veterinarian.setProfessionalLicense(license);
        return veterinarian;
    }

    public Integer askSpecialtyId() {
        String idStr = JOptionPane.showInputDialog("ID de la especialidad:");
        if (idStr == null) return null;
        return Integer.parseInt(idStr);
    }

    public Integer askVeterinarianId() {
        String idStr = JOptionPane.showInputDialog("ID del veterinario:");
        if (idStr == null) return null;
        return Integer.parseInt(idStr);
    }

    public boolean askNewStatus() {
        int confirm = JOptionPane.showConfirmDialog(null, "¿Activar (Sí) o Desactivar (No)?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }

    public void showVeterinariansList(List<Veterinarian> veterinarians) {
        StringBuilder sb = new StringBuilder();
        for (Veterinarian v : veterinarians) {
            sb.append(v.getId()).append(" - ").append(v.getUser().getFirstName())
                    .append(" ").append(v.getUser().getLastName())
                    .append(" - ").append(v.getSpecialty().getName())
                    .append(" - Tarjeta: ").append(v.getProfessionalLicense())
                    .append(" - ").append(v.getUser().getStatus() ? "ACTIVO" : "INACTIVO").append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No hay veterinarios registrados");
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
