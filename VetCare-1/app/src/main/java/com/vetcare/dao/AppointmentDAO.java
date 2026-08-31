/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao;

import com.vetcare.enums.AppointmentStatus;
import com.vetcare.model.Appointment;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public interface AppointmentDAO {
    Appointment save(Appointment appointment);
    List<Appointment> findAll();
    Appointment update(Appointment appointment);
    void changeStatus(int id, AppointmentStatus status);
    List<Appointment> findByPet(int petId);
    List<Appointment> findByVeterinarian(int veterinarianId);
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);
    Optional<Appointment> findById(int id);
}
