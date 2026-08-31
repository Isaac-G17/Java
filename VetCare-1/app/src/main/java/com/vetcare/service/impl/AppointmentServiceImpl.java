/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.service.impl;

import com.vetcare.dao.AppointmentDAO;
import com.vetcare.dao.PetDAO;
import com.vetcare.dao.VeterinarianDAO;
import com.vetcare.enums.AppointmentStatus;
import com.vetcare.exception.AppointmentConflictException;
import com.vetcare.exception.BusinessException;
import com.vetcare.exception.InactiveOwnerException;
import com.vetcare.exception.InvalidAppointmentStateException;
import com.vetcare.exception.PetNotFoundException;
import com.vetcare.exception.ValidationException;
import com.vetcare.exception.VeterinarianNotAvailableException;
import com.vetcare.model.Appointment;
import com.vetcare.model.Pet;
import com.vetcare.model.Veterinarian;
import com.vetcare.service.AppointmentService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDAO appointmentDAO;
    private final PetDAO petDAO;
    private final VeterinarianDAO veterinarianDAO;

    public AppointmentServiceImpl(AppointmentDAO appointmentDAO, PetDAO petDAO, VeterinarianDAO veterinarianDAO) {
        this.appointmentDAO = appointmentDAO;
        this.petDAO = petDAO;
        this.veterinarianDAO = veterinarianDAO;
    }

    @Override
    public Appointment scheduleAppointment(Appointment appointment) {
        // Regla: "La mascota debe encontrarse activa"
        Pet pet = petDAO.findById(appointment.getPet().getId())
                .orElseThrow(() -> new PetNotFoundException(appointment.getPet().getId()));
        if (!pet.getStatus()) {
            throw new ValidationException("La mascota está inactiva, no puede agendar citas");
        }

        // Regla: "El propietario debe encontrarse activo"
        if (!pet.getOwner().getStatus()) {
            throw new InactiveOwnerException(pet.getOwner().getId());
        }

        // Regla: "El veterinario debe encontrarse activo"
        Veterinarian veterinarian = veterinarianDAO.findAll().stream()
                .filter(v -> v.getId() == appointment.getVeterinarian().getId())
                .findFirst()
                .orElseThrow(() -> new BusinessException("No se encontró el veterinario"));
        if (!veterinarian.getUser().getStatus()) {
            throw new VeterinarianNotAvailableException(veterinarian.getId());
        }

        // Regla: "La fecha de la cita no puede estar en el pasado"
        if (appointment.getDate().isBefore(LocalDate.now())) {
            throw new ValidationException("La fecha de la cita no puede estar en el pasado");
        }

        // Regla: "Un veterinario no puede tener dos citas en la misma fecha y hora"
        boolean vetConflict = appointmentDAO.findByVeterinarian(veterinarian.getId()).stream()
                .anyMatch(a -> a.getDate().equals(appointment.getDate())
                        && a.getHour().equals(appointment.getHour())
                        && a.getStatus() != AppointmentStatus.CANCELADA);
        if (vetConflict) {
            throw new AppointmentConflictException("El veterinario ya tiene una cita programada en esa fecha y hora");
        }

        // Regla: "Una mascota no puede tener dos citas en la misma fecha y hora"
        boolean petConflict = appointmentDAO.findByPet(pet.getId()).stream()
                .anyMatch(a -> a.getDate().equals(appointment.getDate())
                        && a.getHour().equals(appointment.getHour())
                        && a.getStatus() != AppointmentStatus.CANCELADA);
        if (petConflict) {
            throw new AppointmentConflictException("La mascota ya tiene una cita programada en esa fecha y hora");
        }

        appointment.setStatus(AppointmentStatus.PROGRAMADA);
        appointment.setRegistrationDate(LocalDateTime.now());
        appointment.setPet(pet);
        appointment.setVeterinarian(veterinarian);

        return appointmentDAO.save(appointment);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentDAO.findAll();
    }

    @Override
    public Appointment updateAppointment(Appointment appointment) {
        Appointment existing = appointmentDAO.findByPet(appointment.getPet().getId()).stream()
                .filter(a -> a.getId() == appointment.getId())
                .findFirst()
                .orElseThrow(() -> new BusinessException("No se encontró la cita"));

        // Regla: "Una cita finalizada no puede modificarse"
        if (existing.getStatus() == AppointmentStatus.FINALIZADA) {
            throw new InvalidAppointmentStateException("Una cita finalizada no puede modificarse");
        }

        return appointmentDAO.update(appointment);
    }

    @Override
    public void cancelAppointment(int id) {
        changeAppointmentStatus(id, AppointmentStatus.CANCELADA);
    }

    @Override
    public void changeAppointmentStatus(int id, AppointmentStatus status) {
        Appointment appointment = appointmentDAO.findById(id)
                .orElseThrow(() -> new BusinessException("No se encontró la cita"));

        // Regla: "Una cita finalizada o cancelada no puede cambiar de estado"
        if (appointment.getStatus() == AppointmentStatus.FINALIZADA
                || appointment.getStatus() == AppointmentStatus.CANCELADA) {
            throw new InvalidAppointmentStateException(
                    "Una cita " + appointment.getStatus().name().toLowerCase() + " no puede cambiar de estado");
        }

        // Regla: "Solo una cita programada puede confirmarse"
        if (status == AppointmentStatus.CONFIRMADA && appointment.getStatus() != AppointmentStatus.PROGRAMADA) {
            throw new InvalidAppointmentStateException("Solo una cita programada puede confirmarse");
        }

        appointmentDAO.changeStatus(id, status);
    }

    @Override
    public List<Appointment> findByPet(int petId) {
        return appointmentDAO.findByPet(petId);
    }

    @Override
    public List<Appointment> findByVeterinarian(int veterinarianId) {
        return appointmentDAO.findByVeterinarian(veterinarianId);
    }

    @Override
    public List<Appointment> findByDate(LocalDate date) {
        return appointmentDAO.findByAppointmentDate(date);
    }
}
