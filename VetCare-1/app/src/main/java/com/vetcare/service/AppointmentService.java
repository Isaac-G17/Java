/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vetcare.service;

import com.vetcare.enums.AppointmentStatus;
import com.vetcare.model.Appointment;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public interface AppointmentService {
    Appointment scheduleAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
    Appointment updateAppointment(Appointment appointment);
    void cancelAppointment(int id);
    void changeAppointmentStatus(int id, AppointmentStatus status);
    List<Appointment> findByPet(int petId);
    List<Appointment> findByVeterinarian(int veterinarianId);
    List<Appointment> findByDate(LocalDate date);
}
