/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vetcare.service;

import com.vetcare.model.User;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public interface UserService {
    User registerUser(User user);
    List<User> getAllUsers();
    User updateUser(User user);
    void changeUserStatus(int id, boolean status);
    User login(String email, String password);
}