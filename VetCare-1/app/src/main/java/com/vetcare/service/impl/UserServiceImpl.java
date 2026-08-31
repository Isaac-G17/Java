/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.service.impl;

import com.vetcare.dao.UserDAO;
import com.vetcare.exception.BusinessException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.User;
import com.vetcare.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User registerUser(User user) {
        // Regla: "El nombre de usuario debe ser único" (usando email como identificador)
        Optional<User> existing = userDAO.findByUserEmail(user.getEmail());
        if (existing.isPresent()) {
            throw new ValidationException("Ya existe un usuario registrado con ese correo");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ValidationException("La contraseña es obligatoria");
        }

        user.setStatus(true);
        user.setRegistrationDate(LocalDateTime.now());

        return userDAO.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public User updateUser(User user) {
        boolean exists = userDAO.findAll().stream()
                .anyMatch(u -> u.getId() == user.getId());
        if (!exists) {
            throw new BusinessException("No se encontró el usuario con id: " + user.getId());
        }

        return userDAO.update(user);
    }

    @Override
    public void changeUserStatus(int id, boolean status) {
        userDAO.changeStatus(id, status);
    }

    @Override
    public User login(String email, String password) {
        User user = userDAO.findByCredentials(email, password)
                .orElseThrow(() -> new BusinessException("Correo o contraseña incorrectos"));

        // Regla: "El usuario debe encontrarse activo"
        if (!user.getStatus()) {
            throw new BusinessException("Tu usuario está inactivo. Contacta al administrador");
        }

        return user;
    }
}