/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.service;

import com.vetcare.model.Attention;
import com.vetcare.model.AttentionDetails;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public interface AttentionService {
    Attention startAttention(Attention attention);
    Attention finalizeAttention(int attentionId, Attention updatedData, List<AttentionDetails> medicationsUsed);
    List<Attention> getMedicalHistory(int petId);
}