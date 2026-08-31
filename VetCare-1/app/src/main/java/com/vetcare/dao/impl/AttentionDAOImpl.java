/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao.impl;

import com.vetcare.config.ConnectionDB;
import com.vetcare.dao.AttentionDAO;
import com.vetcare.enums.AppointmentStatus;
import com.vetcare.enums.AttentionStatus;
import com.vetcare.enums.IdentificationType;
import com.vetcare.enums.Sex;
import com.vetcare.exception.PersistenceException;
import com.vetcare.model.Appointment;
import com.vetcare.model.Attention;
import com.vetcare.model.Owner;
import com.vetcare.model.Pet;
import com.vetcare.model.Role;
import com.vetcare.model.Specialty;
import com.vetcare.model.User;
import com.vetcare.model.Veterinarian;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public class AttentionDAOImpl implements AttentionDAO {

    @Override
    public Attention save(Attention attention) {
        String sql = "INSERT INTO attention (symptoms, diagnosis, treatment, observation, "
                + "date_attention, status, id_veterinarian, id_appointment, id_pet) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

            return attention;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar la atención", e);
        }
    }

    @Override
    public Attention update(Attention attention) {
        String sql = "UPDATE attention SET symptoms = ?, diagnosis = ?, treatment = ?, "
                + "observation = ?, status = ? WHERE id = ?";
        // No toca date_attention, id_veterinarian, id_appointment ni id_pet aquí a propósito:
        // esos datos no deberían cambiar una vez creada la atención

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, attention.getSymptoms());
            statement.setString(2, attention.getDiagnosis());
            statement.setString(3, attention.getTreatment());
            statement.setString(4, attention.getObservation());
            statement.setString(5, attention.getStatus().name());
            statement.setInt(6, attention.getId());

            statement.executeUpdate();
            return attention;

        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar la atención", e);
        }
    }

    @Override
    public List<Attention> findByPet(int petId) {
        String sql = buildBaseSelect() + " WHERE at.id_pet = ?";
        List<Attention> attentions = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, petId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    attentions.add(mapRow(resultSet));
                }
                return attentions;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar atenciones por mascota", e);
        }
    }

    @Override
    public Optional<Attention> findByAppointmentId(int appointmentId) {
        String sql = buildBaseSelect() + " WHERE at.id_appointment = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointmentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar la atención por cita", e);
        }
    }
    
    @Override
    public Optional<Attention> findById(int id) {
        String sql = buildBaseSelect() + " WHERE at.id = ?";
        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar la atención por id", e);
        }
    }
    
    private String buildBaseSelect() {
        return "SELECT "
                + "at.id AS at_id, at.symptoms AS at_symptoms, at.diagnosis AS at_diagnosis, "
                + "at.treatment AS at_treatment, at.observation AS at_observation, "
                + "at.date_attention AS at_date_attention, at.status AS at_status, "

                + "ap.id AS ap_id, ap.date AS ap_date, ap.hour AS ap_hour, ap.reason AS ap_reason, "
                + "ap.status AS ap_status, ap.registration_date AS ap_registration_date, "

                + "p.id AS p_id, p.name AS p_name, p.species AS p_species, p.breed AS p_breed, "
                + "p.sex AS p_sex, p.date_of_birth AS p_date_of_birth, p.weight AS p_weight, "
                + "p.status AS p_status, p.registration_date AS p_registration_date, "

                + "o.id AS o_id, o.identification_type AS o_identification_type, "
                + "o.identification_number AS o_identification_number, o.first_name AS o_first_name, "
                + "o.last_name AS o_last_name, o.phone_number AS o_phone_number, o.email AS o_email, "
                + "o.address AS o_address, o.status AS o_status, "
                + "o.registration_date AS o_registration_date, "

                + "v.id AS v_id, v.professional_license AS v_professional_license, "

                + "u.id AS u_id, u.identification_type AS u_identification_type, "
                + "u.identification_number AS u_identification_number, u.first_name AS u_first_name, "
                + "u.last_name AS u_last_name, u.phone_number AS u_phone_number, u.email AS u_email, "
                + "u.password AS u_password, u.status AS u_status, "
                + "u.registration_date AS u_registration_date, "

                + "r.id AS r_id, r.name AS r_name, "

                + "s.id AS s_id, s.name AS s_name "

                + "FROM attention at "
                + "JOIN appointment ap ON at.id_appointment = ap.id "
                + "JOIN pet p ON at.id_pet = p.id "
                + "JOIN owner o ON p.id_owner = o.id "
                + "JOIN veterinarian v ON at.id_veterinarian = v.id "
                + "JOIN users u ON v.id_user = u.id "
                + "JOIN role r ON u.id_rol = r.id "
                + "JOIN specialty s ON v.id_specialty = s.id";
    }

    private Attention mapRow(ResultSet rs) throws SQLException {
        Owner owner = new Owner(
                rs.getInt("o_id"),
                IdentificationType.valueOf(rs.getString("o_identification_type")),
                rs.getString("o_identification_number"),
                rs.getString("o_first_name"),
                rs.getString("o_last_name"),
                rs.getString("o_phone_number"),
                rs.getString("o_email"),
                rs.getString("o_address"),
                rs.getBoolean("o_status"),
                rs.getTimestamp("o_registration_date").toLocalDateTime()
        );

        // Esta Pet se reutiliza tanto para Attention como para Appointment,
        // porque en este dominio ambos apuntan a la misma mascota
        Pet pet = new Pet(
                rs.getInt("p_id"),
                rs.getString("p_name"),
                rs.getString("p_species"),
                rs.getString("p_breed"),
                Sex.valueOf(rs.getString("p_sex")),
                rs.getObject("p_date_of_birth", LocalDate.class),
                rs.getBigDecimal("p_weight"),
                rs.getBoolean("p_status"),
                rs.getTimestamp("p_registration_date").toLocalDateTime(),
                owner
        );

        Role role = new Role(
                rs.getInt("r_id"),
                rs.getString("r_name")
        );

        User user = new User(
                rs.getInt("u_id"),
                IdentificationType.valueOf(rs.getString("u_identification_type")),
                rs.getString("u_identification_number"),
                rs.getString("u_first_name"),
                rs.getString("u_last_name"),
                rs.getString("u_phone_number"),
                rs.getString("u_email"),
                rs.getString("u_password"),
                rs.getBoolean("u_status"),
                rs.getTimestamp("u_registration_date").toLocalDateTime(),
                role
        );

        Specialty specialty = new Specialty(
                rs.getInt("s_id"),
                rs.getString("s_name")
        );

        // Igual que Pet, este Veterinarian se reutiliza para Attention y Appointment
        Veterinarian veterinarian = new Veterinarian(
                rs.getInt("v_id"),
                rs.getString("v_professional_license"),
                user,
                specialty
        );

        Appointment appointment = new Appointment(
                rs.getInt("ap_id"),
                rs.getObject("ap_date", LocalDate.class),
                rs.getObject("ap_hour", LocalTime.class),
                rs.getString("ap_reason"),
                AppointmentStatus.valueOf(rs.getString("ap_status")),
                rs.getTimestamp("ap_registration_date").toLocalDateTime(),
                pet,
                veterinarian
        );

        return new Attention(
                rs.getInt("at_id"),
                rs.getString("at_symptoms"),
                rs.getString("at_diagnosis"),
                rs.getString("at_treatment"),
                rs.getString("at_observation"),
                rs.getTimestamp("at_date_attention").toLocalDateTime(),
                AttentionStatus.valueOf(rs.getString("at_status")),
                veterinarian,
                appointment,
                pet
        );
    }
}