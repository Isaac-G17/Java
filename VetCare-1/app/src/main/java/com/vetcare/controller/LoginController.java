/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.controller;

import com.vetcare.exception.BusinessException;
import com.vetcare.exception.PersistenceException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.User;
import com.vetcare.service.UserService;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Gu_Zra
 */
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    public User login() {
        while (true) {
            JTextField emailField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            Object[] fields = {
                    "Correo:", emailField,
                    "Contraseña:", passwordField
            };

            int option = JOptionPane.showConfirmDialog(null, fields, "Iniciar sesión",
                    JOptionPane.OK_CANCEL_OPTION);

            if (option != JOptionPane.OK_OPTION) {
                return null;
            }

            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            try {
                User user = userService.login(email, password);
                JOptionPane.showMessageDialog(null,
                        "Bienvenido, " + user.getFirstName() + " (" + user.getRole().getName() + ")");
                return user;

            } catch (BusinessException | ValidationException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error de acceso", JOptionPane.WARNING_MESSAGE);
            } catch (PersistenceException e) {
                JOptionPane.showMessageDialog(null,
                        "Error al conectar con la base de datos. Intenta más tarde.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
    }
}
