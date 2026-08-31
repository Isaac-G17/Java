/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.view;

import com.vetcare.model.Medication;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Gu_Zra
 */
public class MedicationView {

    public String showMenu() {
        String[] options = {"Registrar", "Listar", "Actualizar existencias", "Activar/Desactivar",
                "Ver bajo inventario", "Volver"};
        return (String) JOptionPane.showInputDialog(
                null, "Gestión de medicamentos", "Medicamentos",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]
        );
    }

    public Medication captureMedicationData() {
        String code = JOptionPane.showInputDialog("Código:");
        if (code == null) return null;
        String name = JOptionPane.showInputDialog("Nombre:");
        if (name == null) return null;
        String presentation = JOptionPane.showInputDialog("Presentación:");
        if (presentation == null) return null;
        String laboratory = JOptionPane.showInputDialog("Laboratorio:");
        if (laboratory == null) return null;
        String availableQtyStr = JOptionPane.showInputDialog("Cantidad disponible:");
        if (availableQtyStr == null) return null;
        String minQtyStr = JOptionPane.showInputDialog("Cantidad mínima:");
        if (minQtyStr == null) return null;
        String priceStr = JOptionPane.showInputDialog("Precio unitario:");
        if (priceStr == null) return null;

        Medication medication = new Medication();
        medication.setCode(code);
        medication.setName(name);
        medication.setPresentation(presentation);
        medication.setLaboratory(laboratory);
        medication.setAvailableQuantity(Integer.parseInt(availableQtyStr));
        medication.setMinimumQuantity(Integer.parseInt(minQtyStr));
        medication.setPrice(new BigDecimal(priceStr));
        return medication;
    }

    public Integer askMedicationId() {
        String idStr = JOptionPane.showInputDialog("ID del medicamento:");
        if (idStr == null) return null;
        return Integer.parseInt(idStr);
    }

    public Integer askNewQuantity() {
        String qtyStr = JOptionPane.showInputDialog("Nueva cantidad disponible:");
        if (qtyStr == null) return null;
        return Integer.parseInt(qtyStr);
    }

    public boolean askNewStatus() {
        int confirm = JOptionPane.showConfirmDialog(null, "¿Activar (Sí) o Desactivar (No)?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }

    public void showMedicationsList(List<Medication> medications) {
        StringBuilder sb = new StringBuilder();
        for (Medication m : medications) {
            sb.append(m.getId()).append(" - ").append(m.getName())
                    .append(" - Disponible: ").append(m.getAvailableQuantity())
                    .append(" - Mínimo: ").append(m.getMinimumQuantity())
                    .append(" - $").append(m.getPrice())
                    .append(" - ").append(m.isStatus() ? "ACTIVO" : "INACTIVO").append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No hay medicamentos registrados");
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