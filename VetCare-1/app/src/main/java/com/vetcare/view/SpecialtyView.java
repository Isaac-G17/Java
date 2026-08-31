/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.view;

import com.vetcare.model.Specialty;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Gu_Zra
 */
public class SpecialtyView {

    public String showMenu() {
        String[] options = {"Registrar", "Listar", "Volver"};
        return (String) JOptionPane.showInputDialog(
                null, "Gestión de especialidades", "Especialidades",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]
        );
    }

    public Specialty captureSpecialtyData() {
        String name = JOptionPane.showInputDialog("Nombre de la especialidad:");
        if (name == null) return null;

        Specialty specialty = new Specialty();
        specialty.setName(name);
        return specialty;
    }

    public void showSpecialtiesList(List<Specialty> specialties) {
        StringBuilder sb = new StringBuilder();
        for (Specialty s : specialties) {
            sb.append(s.getId()).append(" - ").append(s.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No hay especialidades registradas");
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
