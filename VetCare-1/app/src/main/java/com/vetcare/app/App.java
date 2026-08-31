/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.vetcare.app;

import com.vetcare.controller.LoginController;
import com.vetcare.controller.MainMenuController;
import com.vetcare.dao.AppointmentDAO;
import com.vetcare.dao.AttentionDAO;
import com.vetcare.dao.AttentionDetailsDAO;
import com.vetcare.dao.MedicationDAO;
import com.vetcare.dao.OwnerDAO;
import com.vetcare.dao.PetDAO;
import com.vetcare.dao.RoleDAO;
import com.vetcare.dao.SpecialtyDAO;
import com.vetcare.dao.UserDAO;
import com.vetcare.dao.VeterinarianDAO;
import com.vetcare.dao.impl.AppointmentDAOImpl;
import com.vetcare.dao.impl.AttentionDAOImpl;
import com.vetcare.dao.impl.AttentionDetailsDAOImpl;
import com.vetcare.dao.impl.MedicationDAOImpl;
import com.vetcare.dao.impl.OwnerDAOImpl;
import com.vetcare.dao.impl.PetDAOImpl;
import com.vetcare.dao.impl.RoleDAOImpl;
import com.vetcare.dao.impl.SpecialtyDAOImpl;
import com.vetcare.dao.impl.UserDAOImpl;
import com.vetcare.dao.impl.VeterinarianDAOImpl;
import com.vetcare.model.User;
import com.vetcare.service.AppointmentService;
import com.vetcare.service.AttentionService;
import com.vetcare.service.MedicationService;
import com.vetcare.service.OwnerService;
import com.vetcare.service.PetService;
import com.vetcare.service.RoleService;
import com.vetcare.service.SpecialtyService;
import com.vetcare.service.UserService;
import com.vetcare.service.VeterinarianService;
import com.vetcare.service.impl.AppointmentServiceImpl;
import com.vetcare.service.impl.AttentionServiceImpl;
import com.vetcare.service.impl.MedicationServiceImpl;
import com.vetcare.service.impl.OwnerServiceImpl;
import com.vetcare.service.impl.PetServiceImpl;
import com.vetcare.service.impl.RoleServiceImpl;
import com.vetcare.service.impl.SpecialtyServiceImpl;
import com.vetcare.service.impl.UserServiceImpl;
import com.vetcare.service.impl.VeterinarianServiceImpl;

/**
 *
 * @author Cohorte 5
 */
public class App {

    public static void main(String[] args) {
        // --- DAO ---
        OwnerDAO ownerDAO = new OwnerDAOImpl();
        PetDAO petDAO = new PetDAOImpl();
        VeterinarianDAO veterinarianDAO = new VeterinarianDAOImpl();
        AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
        AttentionDAO attentionDAO = new AttentionDAOImpl();
        AttentionDetailsDAO attentionDetailsDAO = new AttentionDetailsDAOImpl();
        MedicationDAO medicationDAO = new MedicationDAOImpl();
        UserDAO userDAO = new UserDAOImpl();
        SpecialtyDAO specialtyDAO = new SpecialtyDAOImpl();
        RoleDAO roleDAO = new RoleDAOImpl();

        // --- Service ---
        OwnerService ownerService = new OwnerServiceImpl(ownerDAO);
        PetService petService = new PetServiceImpl(petDAO, ownerDAO);
        VeterinarianService veterinarianService = new VeterinarianServiceImpl(veterinarianDAO, userDAO);
        AppointmentService appointmentService = new AppointmentServiceImpl(appointmentDAO, petDAO, veterinarianDAO);
        AttentionService attentionService = new AttentionServiceImpl(attentionDAO, attentionDetailsDAO, medicationDAO, appointmentDAO);
        MedicationService medicationService = new MedicationServiceImpl(medicationDAO);
        UserService userService = new UserServiceImpl(userDAO);
        SpecialtyService specialtyService = new SpecialtyServiceImpl(specialtyDAO);
        RoleService roleService = new RoleServiceImpl(roleDAO);

        // --- Login ---
        LoginController loginController = new LoginController(userService);
        User loggedUser = loginController.login();

        if (loggedUser != null) {
            MainMenuController mainMenu = new MainMenuController(
                    loggedUser, ownerService, petService, veterinarianService,
                    appointmentService, attentionService, medicationService,
                    userService, specialtyService, roleService
            );
            mainMenu.show();
        }

        System.out.println("Aplicación finalizada.");
    }
}
