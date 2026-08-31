/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao.impl;

import com.vetcare.config.ConnectionDB;
import com.vetcare.dao.AppointmentDAO;
import com.vetcare.enums.AppointmentStatus;
import com.vetcare.enums.IdentificationType;
import com.vetcare.enums.Sex;
import com.vetcare.exception.PersistenceException;
import com.vetcare.model.Appointment;
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
public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public Appointment save(Appointment appointment) {
        String sql = "INSERT INTO appointment (date, hour, reason, status, registration_date, "
                + "id_pet, id_veterinarian) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setObject(1, appointment.getDate());
            statement.setObject(2, appointment.getHour());
            statement.setString(3, appointment.getReason());
            statement.setString(4, appointment.getStatus().name());
            statement.setObject(5, appointment.getRegistrationDate());
            statement.setInt(6, appointment.getPet().getId());
            statement.setInt(7, appointment.getVeterinarian().getId());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    appointment.setId(generatedKeys.getInt(1));
                }
            }

            return appointment;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar la cita", e);
        }
    }

    @Override
    public List<Appointment> findAll() {
        return executeListQuery(buildBaseSelect(), null);
    }

    @Override
    public Appointment update(Appointment appointment) {
        String sql = "UPDATE appointment SET date = ?, hour = ?, reason = ?, "
                + "id_pet = ?, id_veterinarian = ? WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, appointment.getDate());
            statement.setObject(2, appointment.getHour());
            statement.setString(3, appointment.getReason());
            statement.setInt(4, appointment.getPet().getId());
            statement.setInt(5, appointment.getVeterinarian().getId());
            statement.setInt(6, appointment.getId());

            statement.executeUpdate();
            return appointment;

        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar la cita", e);
        }
    }

    @Override
    public void changeStatus(int id, AppointmentStatus status) {
        String sql = "UPDATE appointment SET status = ? WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status.name());
            statement.setInt(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Error al cambiar el estado de la cita", e);
        }
    }

    @Override
    public List<Appointment> findByPet(int petId) {
        String sql = buildBaseSelect() + " WHERE a.id_pet = ?";
        return executeListQuery(sql, stmt -> stmt.setInt(1, petId));
    }

    @Override
    public List<Appointment> findByVeterinarian(int veterinarianId) {
        String sql = buildBaseSelect() + " WHERE a.id_veterinarian = ?";
        return executeListQuery(sql, stmt -> stmt.setInt(1, veterinarianId));
    }

    @Override
    public List<Appointment> findByAppointmentDate(LocalDate appointmentDate) {
        String sql = buildBaseSelect() + " WHERE a.date = ?";
        return executeListQuery(sql, stmt -> stmt.setObject(1, appointmentDate));
    }

    @Override
    public Optional<Appointment> findById(int id) {
        String sql = buildBaseSelect() + " WHERE a.id = ?";

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
            throw new PersistenceException("Error al buscar la cita por id", e);
        }
    }

    /**
    * ParamSetter es una interfaz funcional (tiene un solo método) que permite
    * "pasar como parámetro" la lógica de asignar valores a los "?" del SQL,
    * en vez de repetir el mismo bloque de conexión + ejecución + mapeo en
    * cada método de búsqueda (findAll, findByPet, findByVeterinarian, findByDate).
    *
    * Cada método público solo indica QUÉ parámetro asignar (con una lambda,
    * ej: stmt -> stmt.setInt(1, petId)), y executeListQuery() se encarga de
    * CÓMO ejecutar la consulta completa. Esto evita duplicar ~15 líneas de
    * código JDBC idéntico en 4 lugares distintos (principio DRY - Don't
    * Repeat Yourself).
    */
    @FunctionalInterface
    private interface ParamSetter {
        void set(PreparedStatement statement) throws SQLException;
    }
    /**
    * Ejecuta cualquier consulta SELECT que devuelva múltiples citas (Appointment).
    * Recibe el SQL ya armado y, opcionalmente, un ParamSetter que define qué
    * valores asignar a los "?" antes de ejecutar (null si el SQL no tiene "?",
    * como en el caso de findAll()).
    */
    private List<Appointment> executeListQuery(String sql, ParamSetter paramSetter) {
        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (paramSetter != null) {
                paramSetter.set(statement);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    appointments.add(mapRow(resultSet));
                }
                return appointments;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al consultar citas", e);
        }
    }

    private String buildBaseSelect() {
        return "SELECT "
                + "a.id AS a_id, a.date AS a_date, a.hour AS a_hour, a.reason AS a_reason, "
                + "a.status AS a_status, a.registration_date AS a_registration_date, "

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

                + "FROM appointment a "
                + "JOIN pet p ON a.id_pet = p.id "
                + "JOIN owner o ON p.id_owner = o.id "
                + "JOIN veterinarian v ON a.id_veterinarian = v.id "
                + "JOIN users u ON v.id_user = u.id "
                + "JOIN role r ON u.id_rol = r.id "
                + "JOIN specialty s ON v.id_specialty = s.id";
    }

    private Appointment mapRow(ResultSet rs) throws SQLException {
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

        Veterinarian veterinarian = new Veterinarian(
                rs.getInt("v_id"),
                rs.getString("v_professional_license"),
                user,
                specialty
        );

        return new Appointment(
                rs.getInt("a_id"),
                rs.getObject("a_date", LocalDate.class),
                rs.getObject("a_hour", LocalTime.class),
                rs.getString("a_reason"),
                AppointmentStatus.valueOf(rs.getString("a_status")),
                rs.getTimestamp("a_registration_date").toLocalDateTime(),
                pet,
                veterinarian
        );
    }
}