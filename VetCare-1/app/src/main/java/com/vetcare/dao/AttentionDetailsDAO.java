/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao;

import com.vetcare.model.AttentionDetails;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public interface AttentionDetailsDAO {
    AttentionDetails save(AttentionDetails attentionDetails);
    List<AttentionDetails> findByAttentionId(int attentionId);
}
