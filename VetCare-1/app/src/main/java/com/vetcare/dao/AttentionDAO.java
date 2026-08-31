/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao;

import com.vetcare.model.Attention;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public interface AttentionDAO {
    Attention save(Attention attention);
    Attention update(Attention attention);
    List<Attention> findByPet(int petId); // esto ES tu "historial médico"
    Optional<Attention> findByAppointmentId(int appointmentId);
    Optional<Attention> findById(int id);
}
