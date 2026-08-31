/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.controller;

import com.vetcare.model.User;
import com.vetcare.service.AppointmentService;
import com.vetcare.service.AttentionService;
import com.vetcare.service.MedicationService;
import com.vetcare.service.OwnerService;
import com.vetcare.service.PetService;
import com.vetcare.service.RoleService;
import com.vetcare.service.SpecialtyService;
import com.vetcare.service.UserService;
import com.vetcare.service.VeterinarianService;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Gu_Zra
 */
public class MainMenuController {

    private final User loggedUser;
    private final OwnerService ownerService;
    private final PetService petService;
    private final VeterinarianService veterinarianService;
    private final AppointmentService appointmentService;
    private final AttentionService attentionService;
    private final MedicationService medicationService;
    private final UserService userService;
    private final SpecialtyService specialtyService;
    private final RoleService roleService;

    public MainMenuController(User loggedUser, OwnerService ownerService, PetService petService,
                               VeterinarianService veterinarianService, AppointmentService appointmentService,
                               AttentionService attentionService, MedicationService medicationService,
                               UserService userService, SpecialtyService specialtyService, RoleService roleService) {
        this.loggedUser = loggedUser;
        this.ownerService = ownerService;
        this.petService = petService;
        this.veterinarianService = veterinarianService;
        this.appointmentService = appointmentService;
        this.attentionService = attentionService;
        this.medicationService = medicationService;
        this.userService = userService;
        this.specialtyService = specialtyService;
        this.roleService = roleService;
    }

    public void show() {
        boolean exit = false;
        while (!exit) {
            List<String> options = buildMenuOptions();
            String[] optionsArray = options.toArray(new String[0]);

            String choice = (String) JOptionPane.showInputDialog(
                    null, "Selecciona una opción:", "Menú principal - " + loggedUser.getRole().getName(),
                    JOptionPane.QUESTION_MESSAGE, null, optionsArray, optionsArray[0]
            );

            if (choice == null || choice.equals("Salir")) {
                exit = true;
                continue;
            }

            switch (choice) {
                case "Propietarios" -> new OwnerController(ownerService).show();
                case "Mascotas" -> new PetController(petService, ownerService).show();
                case "Veterinarios" -> new VeterinarianController(veterinarianService, userService, specialtyService).show();
                case "Citas" -> new AppointmentController(appointmentService, petService, veterinarianService).show();
                case "Atenciones" -> new AttentionController(attentionService, appointmentService, medicationService).show();
                case "Medicamentos" -> new MedicationController(medicationService).show();
                case "Usuarios" -> new UserController(userService, roleService).show();
                case "Especialidades" -> new SpecialtyController(specialtyService).show();
                case "Cerrar sesión" -> exit = true;
            }
        }
    }

    private List<String> buildMenuOptions() {
        List<String> options = new ArrayList<>();
        String role = loggedUser.getRole().getName();

        switch (role) {
            case "ADMIN" -> {
                options.add("Veterinarios");
                options.add("Especialidades");
                options.add("Medicamentos");
                options.add("Usuarios");
                options.add("Propietarios");
                options.add("Mascotas");
                options.add("Citas");
                options.add("Atenciones");
            }
            case "RECEPCIONISTA" -> {
                options.add("Propietarios");
                options.add("Mascotas");
                options.add("Citas");
                options.add("Veterinarios");
            }
            case "VETERINARIO" -> {
                options.add("Citas");
                options.add("Atenciones");
            }
        }

        options.add("Cerrar sesión");
        options.add("Salir");
        return options;
    }
}
