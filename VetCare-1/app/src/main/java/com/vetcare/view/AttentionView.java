/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.view;

import com.vetcare.model.Appointment;
import com.vetcare.model.Attention;
import com.vetcare.model.AttentionDetails;
import com.vetcare.model.Medication;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Gu_Zra
 */
public class AttentionView {

    public String showMenu() {
        String[] options = {"Iniciar atención", "Finalizar atención", "Ver historial médico", "Volver"};
        return (String) JOptionPane.showInputDialog(
                null, "Gestión de atenciones médicas", "Atenciones",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]
        );
    }

    public Appointment selectConfirmedAppointment(List<Appointment> confirmedAppointments) {
        if (confirmedAppointments.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay citas confirmadas disponibles para iniciar atención");
            return null;
        }

        String[] options = confirmedAppointments.stream()
                .map(a -> a.getId() + " - " + a.getPet().getName() + " - " + a.getDate() + " " + a.getHour())
                .toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(null, "Selecciona la cita confirmada:",
                "Iniciar atención", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (selected == null) return null;

        int appointmentId = Integer.parseInt(selected.split(" - ")[0]);
        return confirmedAppointments.stream()
                .filter(a -> a.getId() == appointmentId)
                .findFirst()
                .orElse(null);
    }

    public String captureSymptoms() {
        return JOptionPane.showInputDialog("Síntomas observados:");
    }

    public Integer askAttentionId() {
        String idStr = JOptionPane.showInputDialog("ID de la atención a finalizar:");
        if (idStr == null) return null;
        return Integer.parseInt(idStr);
    }

    public String captureDiagnosis() {
        return JOptionPane.showInputDialog("Diagnóstico:");
    }

    public String captureTreatment() {
        return JOptionPane.showInputDialog("Tratamiento (deja vacío si vas a usar solo observaciones):");
    }

    public String captureObservation() {
        return JOptionPane.showInputDialog("Observaciones (deja vacío si ya registraste tratamiento):");
    }

    public List<AttentionDetails> captureMedicationsUsed(List<Medication> availableMedications) {
        List<AttentionDetails> details = new ArrayList<>();

        int addMore = JOptionPane.showConfirmDialog(null, "¿Deseas asociar medicamentos a esta atención?",
                "Medicamentos", JOptionPane.YES_NO_OPTION);

        while (addMore == JOptionPane.YES_OPTION) {
            String[] medNames = availableMedications.stream()
                    .filter(Medication::isStatus)
                    .map(m -> m.getId() + " - " + m.getName() + " (disponible: " + m.getAvailableQuantity() + ")")
                    .toArray(String[]::new);

            if (medNames.length == 0) {
                JOptionPane.showMessageDialog(null, "No hay medicamentos activos disponibles");
                break;
            }

            String selected = (String) JOptionPane.showInputDialog(null, "Selecciona el medicamento:",
                    "Agregar medicamento", JOptionPane.QUESTION_MESSAGE, null, medNames, medNames[0]);
            if (selected == null) break;

            int medicationId = Integer.parseInt(selected.split(" - ")[0]);
            Medication medication = availableMedications.stream()
                    .filter(m -> m.getId() == medicationId)
                    .findFirst()
                    .orElse(null);

            String quantityStr = JOptionPane.showInputDialog("Cantidad a utilizar:");
            if (quantityStr == null) {
                addMore = JOptionPane.showConfirmDialog(null, "¿Agregar otro medicamento?",
                        "Medicamentos", JOptionPane.YES_NO_OPTION);
                continue;
            }
            int quantity = Integer.parseInt(quantityStr);

            AttentionDetails detail = new AttentionDetails();
            detail.setMedication(medication);
            detail.setQuantity(quantity);
            details.add(detail);

            addMore = JOptionPane.showConfirmDialog(null, "¿Agregar otro medicamento?",
                    "Medicamentos", JOptionPane.YES_NO_OPTION);
        }

        return details;
    }

    public Integer askPetIdForHistory() {
        String idStr = JOptionPane.showInputDialog("ID de la mascota para ver el historial médico:");
        if (idStr == null) return null;
        return Integer.parseInt(idStr);
    }

    public void showMedicalHistory(List<Attention> attentions) {
        if (attentions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Esta mascota no tiene atenciones registradas");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Attention a : attentions) {
            sb.append("Fecha: ").append(a.getDateAttention())
                    .append("\nDiagnóstico: ").append(a.getDiagnosis())
                    .append("\nTratamiento: ").append(a.getTreatment())
                    .append("\nEstado: ").append(a.getStatus())
                    .append("\n------------------------\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
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
