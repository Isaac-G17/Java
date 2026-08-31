/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.view;

import com.vetcare.model.Appointment;
import com.vetcare.model.Pet;
import com.vetcare.model.Veterinarian;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Gu_Zra
 */
public class AppointmentView {

    public String showMenu() {
        String[] options = {"Registrar", "Listar", "Buscar por mascota", "Buscar por veterinario",
                "Buscar por fecha", "Cancelar cita", "Confirmar cita", "Volver"};
        return (String) JOptionPane.showInputDialog(
                null, "Gestión de citas", "Citas",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]
        );
    }

    public Appointment captureAppointmentData(List<Pet> pets, List<Veterinarian> veterinarians) {
        // Selección de mascota
        String[] petNames = pets.stream()
                .map(p -> p.getId() + " - " + p.getName())
                .toArray(String[]::new);
        String selectedPet = (String) JOptionPane.showInputDialog(null, "Selecciona la mascota:",
                "Registrar cita", JOptionPane.QUESTION_MESSAGE, null, petNames, petNames.length > 0 ? petNames[0] : null);
        if (selectedPet == null) return null;
        int petId = Integer.parseInt(selectedPet.split(" - ")[0]);
        Pet pet = pets.stream().filter(p -> p.getId() == petId).findFirst().orElse(null);

        // Selección de veterinario
        String[] vetNames = veterinarians.stream()
                .map(v -> v.getId() + " - " + v.getUser().getFirstName() + " " + v.getUser().getLastName())
                .toArray(String[]::new);
        String selectedVet = (String) JOptionPane.showInputDialog(null, "Selecciona el veterinario:",
                "Registrar cita", JOptionPane.QUESTION_MESSAGE, null, vetNames, vetNames.length > 0 ? vetNames[0] : null);
        if (selectedVet == null) return null;
        int vetId = Integer.parseInt(selectedVet.split(" - ")[0]);
        Veterinarian veterinarian = veterinarians.stream().filter(v -> v.getId() == vetId).findFirst().orElse(null);

        String dateStr = JOptionPane.showInputDialog("Fecha (YYYY-MM-DD):");
        if (dateStr == null) return null;
        String hourStr = JOptionPane.showInputDialog("Hora (HH:MM):");
        if (hourStr == null) return null;
        String reason = JOptionPane.showInputDialog("Motivo de la cita:");
        if (reason == null) return null;

        Appointment appointment = new Appointment();
        appointment.setPet(pet);
        appointment.setVeterinarian(veterinarian);
        appointment.setDate(LocalDate.parse(dateStr));
        appointment.setHour(LocalTime.parse(hourStr));
        appointment.setReason(reason);
        return appointment;
    }

    public Integer askPetId() {
        String idStr = JOptionPane.showInputDialog("ID de la mascota:");
        if (idStr == null) return null;
        return Integer.parseInt(idStr);
    }

    public Integer askVeterinarianId() {
        String idStr = JOptionPane.showInputDialog("ID del veterinario:");
        if (idStr == null) return null;
        return Integer.parseInt(idStr);
    }

    public LocalDate askDate() {
        String dateStr = JOptionPane.showInputDialog("Fecha a consultar (YYYY-MM-DD):");
        if (dateStr == null) return null;
        return LocalDate.parse(dateStr);
    }

    public Integer askAppointmentId() {
        String idStr = JOptionPane.showInputDialog("ID de la cita:");
        if (idStr == null) return null;
        return Integer.parseInt(idStr);
    }

    public void showAppointmentsList(List<Appointment> appointments) {
        StringBuilder sb = new StringBuilder();
        for (Appointment a : appointments) {
            sb.append(a.getId()).append(" - ").append(a.getPet().getName())
                    .append(" con ").append(a.getVeterinarian().getUser().getFirstName())
                    .append(" - ").append(a.getDate()).append(" ").append(a.getHour())
                    .append(" - ").append(a.getStatus()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No hay citas registradas");
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