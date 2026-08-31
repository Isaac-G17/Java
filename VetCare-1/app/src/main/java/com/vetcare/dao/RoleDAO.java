/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao;

import com.vetcare.model.Role;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public interface RoleDAO {
    Optional<Role> findById(int id);
    List<Role> findAll();
}
