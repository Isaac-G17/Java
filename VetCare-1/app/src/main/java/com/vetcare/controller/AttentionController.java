/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.controller;

import com.vetcare.enums.AppointmentStatus;
import com.vetcare.exception.BusinessException;
import com.vetcare.exception.PersistenceException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Appointment;
import com.vetcare.model.Attention;
import com.vetcare.model.AttentionDetails;
import com.vetcare.model.Medication;
import com.vetcare.service.AppointmentService;
import com.vetcare.service.AttentionService;
import com.vetcare.service.MedicationService;
import com.vetcare.view.AttentionView;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public class AttentionController {

    private final AttentionService attentionService;
    private final AppointmentService appointmentService;
    private final MedicationService medicationService;
    private final AttentionView attentionView;

    public AttentionController(AttentionService attentionService, AppointmentService appointmentService,
                                MedicationService medicationService) {
        this.attentionService = attentionService;
        this.appointmentService = appointmentService;
        this.medicationService = medicationService;
        this.attentionView = new AttentionView();
    }

    public void show() {
        boolean back = false;
        while (!back) {
            String choice = attentionView.showMenu();

            if (choice == null || choice.equals("Volver")) {
                back = true;
                continue;
            }

            try {
                switch (choice) {
                    case "Iniciar atención" -> startAttention();
                    case "Finalizar atención" -> finalizeAttention();
                    case "Ver historial médico" -> viewMedicalHistory();
                }
            } catch (ValidationException | BusinessException e) {
                attentionView.showError(e.getMessage());
            } catch (PersistenceException e) {
                attentionView.showTechnicalError();
            } catch (Exception e) {
                attentionView.showUnexpectedError();
            }
        }
    }

    private void startAttention() {
        // Regla: "Solo puede iniciarse una atención desde una cita confirmada"
        List<Appointment> confirmedAppointments = appointmentService.getAllAppointments().stream()
                .filter(a -> a.getStatus() == AppointmentStatus.CONFIRMADA)
                .toList();

        Appointment appointment = attentionView.selectConfirmedAppointment(confirmedAppointments);
        if (appointment == null) return;

        String symptoms = attentionView.captureSymptoms();
        if (symptoms == null) return;

        Attention attention = new Attention();
        attention.setAppointment(appointment);
        attention.setPet(appointment.getPet());
        attention.setVeterinarian(appointment.getVeterinarian());
        attention.setSymptoms(symptoms);

        Attention saved = attentionService.startAttention(attention);
        attentionView.showSuccess("Atención iniciada con id: " + saved.getId());
    }

    private void finalizeAttention() {
        Integer attentionId = attentionView.askAttentionId();
        if (attentionId == null) return;

        String diagnosis = attentionView.captureDiagnosis();
        String treatment = attentionView.captureTreatment();
        String observation = attentionView.captureObservation();

        Attention updatedData = new Attention();
        updatedData.setDiagnosis(diagnosis);
        updatedData.setTreatment(treatment);
        updatedData.setObservation(observation);

        List<Medication> availableMedications = medicationService.getAllMedications();
        List<AttentionDetails> medicationsUsed = attentionView.captureMedicationsUsed(availableMedications);

        Attention finalized = attentionService.finalizeAttention(attentionId, updatedData, medicationsUsed);
        attentionView.showSuccess("Atención finalizada correctamente. Estado: " + finalized.getStatus());
    }

    private void viewMedicalHistory() {
        Integer petId = attentionView.askPetIdForHistory();
        if (petId == null) return;
        List<Attention> history = attentionService.getMedicalHistory(petId);
        attentionView.showMedicalHistory(history);
    }
}
