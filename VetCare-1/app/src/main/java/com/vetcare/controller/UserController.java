/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.controller;

import com.vetcare.exception.BusinessException;
import com.vetcare.exception.PersistenceException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Role;
import com.vetcare.model.User;
import com.vetcare.service.RoleService;
import com.vetcare.service.UserService;
import com.vetcare.view.UserView;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public class UserController {

    private final UserService userService;
    private final RoleService roleService; 
    private final UserView userView;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userView = new UserView();
    }

    public void show() {
        boolean back = false;
        while (!back) {
            String choice = userView.showMenu();

            if (choice == null || choice.equals("Volver")) {
                back = true;
                continue;
            }

            try {
                switch (choice) {
                    case "Registrar" -> registerUser();
                    case "Listar" -> listUsers();
                    case "Activar/Desactivar" -> changeStatus();
                }
            } catch (ValidationException | BusinessException e) {
                userView.showError(e.getMessage());
            } catch (PersistenceException e) {
                userView.showTechnicalError();
            } catch (Exception e) {
                userView.showUnexpectedError();
            }
        }
    }

    private void registerUser() {
        List<Role> roles = roleService.getAllRoles();
        User user = userView.captureUserData(roles);
        if (user == null) return;

        User saved = userService.registerUser(user);
        userView.showSuccess("Usuario registrado con id: " + saved.getId());
    }

    private void listUsers() {
        userView.showUsersList(userService.getAllUsers());
    }

    private void changeStatus() {
        Integer id = userView.askUserId();
        if (id == null) return;
        boolean newStatus = userView.askNewStatus();
        userService.changeUserStatus(id, newStatus);
        userView.showSuccess("Estado actualizado");
    }
}