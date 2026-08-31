/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.view;

import com.vetcare.enums.Sex;
import com.vetcare.model.Owner;
import com.vetcare.model.Pet;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Gu_Zra
 */
public class PetView {

    public String showMenu() {
        String[] options = {"Registrar", "Listar", "Buscar por nombre", "Buscar por propietario",
                "Activar/Desactivar", "Volver"};
        return (String) JOptionPane.showInputDialog(
                null, "Gestión de mascotas", "Mascotas",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]
        );
    }

    public Pet capturePetData(List<Owner> owners) {
        String[] ownerNames = owners.stream()
                .filter(Owner::getStatus) // solo propietarios activos, ya que un inactivo no puede registrar mascotas
                .map(o -> o.getId() + " - " + o.getFirstName() + " " + o.getLastName())
                .toArray(String[]::new);

        if (ownerNames.length == 0) {
            JOptionPane.showMessageDialog(null, "No hay propietarios activos registrados");
            return null;
        }

        String selectedOwner = (String) JOptionPane.showInputDialog(null, "Selecciona el propietario:",
                "Registrar mascota", JOptionPane.QUESTION_MESSAGE, null, ownerNames, ownerNames[0]);
        if (selectedOwner == null) return null;

        int ownerId = Integer.parseInt(selectedOwner.split(" - ")[0]);
        Owner owner = owners.stream().filter(o -> o.getId() == ownerId).findFirst().orElse(null);

        String name = JOptionPane.showInputDialog("Nombre de la mascota:");
        if (name == null) return null;
        String species = JOptionPane.showInputDialog("Especie:");
        if (species == null) return null;
        String breed = JOptionPane.showInputDialog("Raza:");
        if (breed == null) return null;

        Sex sex = (Sex) JOptionPane.showInputDialog(null, "Sexo:", "Registrar mascota",
                JOptionPane.QUESTION_MESSAGE, null, Sex.values(), Sex.MACHO);
        if (sex == null) return null;

        String birthDateStr = JOptionPane.showInputDialog("Fecha de nacimiento (YYYY-MM-DD):");
        if (birthDateStr == null) return null;
        String weightStr = JOptionPane.showInputDialog("Peso (kg):");
        if (weightStr == null) return null;

        Pet pet = new Pet();
        pet.setOwner(owner);
        pet.setName(name);
        pet.setSpecies(species);
        pet.setBreed(breed);
        pet.setSex(sex);
        pet.setDateOfBirth(LocalDate.parse(birthDateStr));
        pet.setWeight(new BigDecimal(weightStr));
        return pet;
    }

    public String askPetName() {
        return JOptionPane.showInputDialog("Nombre de la mascota a buscar:");
    }

    public Integer askOwnerId() {
        String idStr = JOptionPane.showInputDialog("ID del propietario:");
        if (idStr == null) return null;
        return Integer.parseInt(idStr);
    }

    public Integer askPetId() {
        String idStr = JOptionPane.showInputDialog("ID de la mascota:");
        if (idStr == null) return null;
        return Integer.parseInt(idStr);
    }

    public boolean askNewStatus() {
        int confirm = JOptionPane.showConfirmDialog(null, "¿Activar (Sí) o Desactivar (No)?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }

    public void showPetsList(List<Pet> pets) {
        StringBuilder sb = new StringBuilder();
        for (Pet p : pets) {
            sb.append(p.getId()).append(" - ").append(p.getName())
                    .append(" (").append(p.getSpecies()).append(") - Dueño: ")
                    .append(p.getOwner().getFirstName()).append(" ").append(p.getOwner().getLastName())
                    .append(" - ").append(p.getStatus() ? "ACTIVA" : "INACTIVA").append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No hay mascotas registradas");
    }

    public void showPetDetail(Pet pet) {
        JOptionPane.showMessageDialog(null,
                pet.getName() + " - " + pet.getSpecies() + " - " + pet.getBreed()
                        + "\nPeso: " + pet.getWeight() + " kg"
                        + "\nDueño: " + pet.getOwner().getFirstName() + " " + pet.getOwner().getLastName());
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
