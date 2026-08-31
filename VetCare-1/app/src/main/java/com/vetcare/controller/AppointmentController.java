/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.controller;

import com.vetcare.enums.AppointmentStatus;
import com.vetcare.exception.BusinessException;
import com.vetcare.exception.PersistenceException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Appointment;
import com.vetcare.model.Pet;
import com.vetcare.model.Veterinarian;
import com.vetcare.service.AppointmentService;
import com.vetcare.service.PetService;
import com.vetcare.service.VeterinarianService;
import com.vetcare.view.AppointmentView;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PetService petService;
    private final VeterinarianService veterinarianService;
    private final AppointmentView appointmentView;

    public AppointmentController(AppointmentService appointmentService, PetService petService,
                                  VeterinarianService veterinarianService) {
        this.appointmentService = appointmentService;
        this.petService = petService;
        this.veterinarianService = veterinarianService;
        this.appointmentView = new AppointmentView();
    }

    public void show() {
        boolean back = false;
        while (!back) {
            String choice = appointmentView.showMenu();

            if (choice == null || choice.equals("Volver")) {
                back = true;
                continue;
            }

            try {
                switch (choice) {
                    case "Registrar" -> registerAppointment();
                    case "Listar" -> listAppointments();
                    case "Buscar por mascota" -> findByPet();
                    case "Buscar por veterinario" -> findByVeterinarian();
                    case "Buscar por fecha" -> findByDate();
                    case "Cancelar cita" -> cancelAppointment();
                    case "Confirmar cita" -> confirmAppointment();
                }
            } catch (ValidationException | BusinessException e) {
                appointmentView.showError(e.getMessage());
            } catch (PersistenceException e) {
                appointmentView.showTechnicalError();
            } catch (Exception e) {
                appointmentView.showUnexpectedError();
            }
        }
    }

    private void registerAppointment() {
        List<Pet> pets = petService.getAllPets();
        List<Veterinarian> veterinarians = veterinarianService.getAllVeterinarians();

        Appointment appointment = appointmentView.captureAppointmentData(pets, veterinarians);
        if (appointment == null) return;

        Appointment saved = appointmentService.scheduleAppointment(appointment);
        appointmentView.showSuccess("Cita registrada con id: " + saved.getId());
    }

    private void listAppointments() {
        appointmentView.showAppointmentsList(appointmentService.getAllAppointments());
    }

    private void findByPet() {
        Integer petId = appointmentView.askPetId();
        if (petId == null) return;
        appointmentView.showAppointmentsList(appointmentService.findByPet(petId));
    }

    private void findByVeterinarian() {
        Integer vetId = appointmentView.askVeterinarianId();
        if (vetId == null) return;
        appointmentView.showAppointmentsList(appointmentService.findByVeterinarian(vetId));
    }

    private void findByDate() {
        LocalDate date = appointmentView.askDate();
        if (date == null) return;
        appointmentView.showAppointmentsList(appointmentService.findByDate(date));
    }

    private void cancelAppointment() {
        Integer id = appointmentView.askAppointmentId();
        if (id == null) return;
        appointmentService.cancelAppointment(id);
        appointmentView.showSuccess("Cita cancelada");
    }

    private void confirmAppointment() {
        Integer id = appointmentView.askAppointmentId();
        if (id == null) return;
        appointmentService.changeAppointmentStatus(id, AppointmentStatus.CONFIRMADA);
        appointmentView.showSuccess("Cita confirmada");
    }
}
