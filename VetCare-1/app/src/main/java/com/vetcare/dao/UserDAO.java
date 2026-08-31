/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao;

import com.vetcare.model.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public interface UserDAO {
    User save(User user);
    List<User> findAll();
    User update(User user);
    void changeStatus(int id, boolean status);
    Optional<User> findByUserEmail(String email); 
    Optional<User> findByCredentials(String email, String password); // para el login
}
