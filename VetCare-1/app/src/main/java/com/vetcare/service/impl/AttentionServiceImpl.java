/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.service.impl;

import com.vetcare.config.ConnectionDB;
import com.vetcare.dao.AppointmentDAO;
import com.vetcare.dao.AttentionDAO;
import com.vetcare.dao.AttentionDetailsDAO;
import com.vetcare.dao.MedicationDAO;
import com.vetcare.enums.AppointmentStatus;
import com.vetcare.enums.AttentionStatus;
import com.vetcare.exception.BusinessException;
import com.vetcare.exception.InsufficientStockException;
import com.vetcare.exception.InvalidAppointmentStateException;
import com.vetcare.exception.MedicalRecordAlreadyExistsException;
import com.vetcare.exception.MedicineNotFoundException;
import com.vetcare.exception.PersistenceException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Appointment;
import com.vetcare.model.Attention;
import com.vetcare.model.AttentionDetails;
import com.vetcare.model.Medication;
import com.vetcare.service.AttentionService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public class AttentionServiceImpl implements AttentionService {

    private final AttentionDAO attentionDAO;
    private final AttentionDetailsDAO attentionDetailsDAO;
    private final MedicationDAO medicationDAO;
    private final AppointmentDAO appointmentDAO;

    public AttentionServiceImpl(AttentionDAO attentionDAO, AttentionDetailsDAO attentionDetailsDAO,
                                 MedicationDAO medicationDAO, AppointmentDAO appointmentDAO) {
        this.attentionDAO = attentionDAO;
        this.attentionDetailsDAO = attentionDetailsDAO;
        this.medicationDAO = medicationDAO;
        this.appointmentDAO = appointmentDAO;
    }

    @Override
    public Attention startAttention(Attention attention) {
        Appointment appointment = attention.getAppointment();

        // Regla: "Solo puede iniciarse una atención desde una cita confirmada"
        if (appointment.getStatus() != AppointmentStatus.CONFIRMADA) {
            throw new InvalidAppointmentStateException(
                "Solo se puede iniciar una atención desde una cita confirmada"
            );
        }

        // Regla: "Una cita solo puede generar una atención"
        Optional<Attention> existing = attentionDAO.findByAppointmentId(appointment.getId());
        if (existing.isPresent()) {
            throw new MedicalRecordAlreadyExistsException(appointment.getId());
        }

        attention.setStatus(AttentionStatus.INICIADA);
        attention.setDateAttention(LocalDateTime.now());

        // ---- TRANSACCIÓN: guardar la atención y pasar la cita a EN_ATENCION deben ser atómicos ----
        try (Connection connection = ConnectionDB.getConnectionDB()) {
            connection.setAutoCommit(false);

            try {
                saveAttentionWithConnection(connection, attention);
                updateAppointmentStatusWithConnection(connection, appointment.getId(), AppointmentStatus.EN_ATENCION);

                connection.commit();

            } catch (Exception e) {
                connection.rollback();
                throw new PersistenceException("Error al iniciar la atención, se revirtieron los cambios", e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error de conexión al iniciar la atención", e);
        }

        return attention;
    }

    @Override
    public Attention finalizeAttention(int attentionId, Attention updatedData, List<AttentionDetails> medicationsUsed) {
        Attention attention = attentionDAO.findById(attentionId)
                .orElseThrow(() -> new BusinessException("No se encontró la atención"));

        // Regla: "Una atención finalizada no puede modificarse"
        if (attention.getStatus() == AttentionStatus.FINALIZADA) {
            throw new InvalidAppointmentStateException("Esta atención ya fue finalizada");
        }

        // Sobrescribe los campos capturados en el formulario sobre la atención existente
        attention.setDiagnosis(updatedData.getDiagnosis());
        attention.setTreatment(updatedData.getTreatment());
        attention.setObservation(updatedData.getObservation());

        // Regla: "El diagnóstico es obligatorio para finalizar la atención"
        if (attention.getDiagnosis() == null || attention.getDiagnosis().isBlank()) {
            throw new ValidationException("El diagnóstico es obligatorio para finalizar la atención");
        }

        // Regla: "No se puede finalizar sin tratamiento u observación médica"
        boolean hasTreatment = attention.getTreatment() != null && !attention.getTreatment().isBlank();
        boolean hasObservation = attention.getObservation() != null && !attention.getObservation().isBlank();
        if (!hasTreatment && !hasObservation) {
            throw new ValidationException("Debe registrar al menos un tratamiento u observación médica");
        }

        // Regla: "Los medicamentos utilizados deben tener inventario disponible"
        for (AttentionDetails detail : medicationsUsed) {
            Medication medication = medicationDAO.findById(detail.getMedication().getId())
                    .orElseThrow(() -> new MedicineNotFoundException(detail.getMedication().getId()));

            if (!medication.isStatus()) {
                throw new ValidationException("No se puede utilizar el medicamento inactivo: " + medication.getName());
            }

            if (medication.getAvailableQuantity() < detail.getQuantity()) {
                throw new InsufficientStockException(
                        medication.getName(),
                        medication.getAvailableQuantity(),
                        detail.getQuantity()
                );
            }
        }

        // ---- TRANSACCIÓN ----
        try (Connection connection = ConnectionDB.getConnectionDB()) {
            connection.setAutoCommit(false);

            try {
                attention.setStatus(AttentionStatus.FINALIZADA);
                updateAttentionWithConnection(connection, attention);

                for (AttentionDetails detail : medicationsUsed) {
                    detail.setAttention(attention);
                    saveAttentionDetailWithConnection(connection, detail);
                    updateMedicationStockWithConnection(connection, detail.getMedication(), detail.getQuantity());
                }

                updateAppointmentStatusWithConnection(connection, attention.getAppointment().getId(), AppointmentStatus.FINALIZADA);

                connection.commit();

            } catch (BusinessException | ValidationException e) {
                connection.rollback();
                throw e;
            } catch (Exception e) {
                connection.rollback();
                throw new PersistenceException("Error al finalizar la atención, se revirtieron los cambios", e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error de conexión al finalizar la atención", e);
        }

        return attention;
    }

    @Override
    public List<Attention> getMedicalHistory(int petId) {
        return attentionDAO.findByPet(petId);
    }

    // --- Métodos auxiliares que reciben la Connection de la transacción ---

    private void saveAttentionWithConnection(Connection connection, Attention attention) throws SQLException {
        String sql = "INSERT INTO attention (symptoms, diagnosis, treatment, observation, "
                + "date_attention, status, id_veterinarian, id_appointment, id_pet) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, attention.getSymptoms());
            statement.setString(2, attention.getDiagnosis());
            statement.setString(3, attention.getTreatment());
            statement.setString(4, attention.getObservation());
            statement.setObject(5, attention.getDateAttention());
            statement.setString(6, attention.getStatus().name());
            statement.setInt(7, attention.getVeterinarian().getId());
            statement.setInt(8, attention.getAppointment().getId());
            statement.setInt(9, attention.getPet().getId());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    attention.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    private void updateAttentionWithConnection(Connection connection, Attention attention) throws SQLException {
        String sql = "UPDATE attention SET diagnosis = ?, treatment = ?, observation = ?, status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, attention.getDiagnosis());
            statement.setString(2, attention.getTreatment());
            statement.setString(3, attention.getObservation());
            statement.setString(4, attention.getStatus().name());
            statement.setInt(5, attention.getId());
            statement.executeUpdate();
        }
    }

    private void saveAttentionDetailWithConnection(Connection connection, AttentionDetails detail) throws SQLException {
        String sql = "INSERT INTO attention_details (quantity, id_attention, id_medication) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, detail.getQuantity());
            statement.setInt(2, detail.getAttention().getId());
            statement.setInt(3, detail.getMedication().getId());
            statement.executeUpdate();
        }
    }

    private void updateMedicationStockWithConnection(Connection connection, Medication medication, int quantityUsed) throws SQLException {
        // Descuenta y valida el inventario en la misma sentencia para evitar condiciones de carrera
        // entre la validación previa (fuera de la transacción) y el descuento real
        String sql = "UPDATE medication SET available_quantity = available_quantity - ? "
                + "WHERE id = ? AND available_quantity >= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quantityUsed);
            statement.setInt(2, medication.getId());
            statement.setInt(3, quantityUsed);
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new InsufficientStockException(medication.getName(), medication.getAvailableQuantity(), quantityUsed);
            }
        }
    }

    private void updateAppointmentStatusWithConnection(Connection connection, int appointmentId, AppointmentStatus status) throws SQLException {
        String sql = "UPDATE appointment SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, appointmentId);
            statement.executeUpdate();
        }
    }
}
