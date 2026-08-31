/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao.impl;

import com.vetcare.config.ConnectionDB;
import com.vetcare.dao.VeterinarianDAO;
import com.vetcare.enums.IdentificationType;
import com.vetcare.exception.PersistenceException;
import com.vetcare.model.Veterinarian;
import com.vetcare.model.Role;
import com.vetcare.model.User;
import com.vetcare.model.Specialty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gu_Zra
 */
public class VeterinarianDAOImpl implements VeterinarianDAO {

    @Override
    public Veterinarian save(Veterinarian veterinarian) {
        String sql = "INSERT INTO veterinarian (professional_license, id_user, id_specialty) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, veterinarian.getProfessionalLicense());
            statement.setInt(2, veterinarian.getUser().getId());
            statement.setInt(3, veterinarian.getSpecialty().getId());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    veterinarian.setId(generatedKeys.getInt(1));
                }
            }

            return veterinarian;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar el veterinario", e);
        }
    }

    @Override
    public List<Veterinarian> findAll() {
        String sql = buildBaseSelect();
        List<Veterinarian> veterinarians = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    veterinarians.add(mapRow(resultSet));
                }
                return veterinarians;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar los veterinarios", e);
        }
    }

    @Override
    public Veterinarian update(Veterinarian veterinarian) {
        String sql = "UPDATE veterinarian SET professional_license = ?, id_specialty = ? WHERE id = ?";
        // Nota: no se actualiza id_user aquí — el veterinario asociado a un usuario no debería reasignarse

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, veterinarian.getProfessionalLicense());
            statement.setInt(2, veterinarian.getSpecialty().getId());
            statement.setInt(3, veterinarian.getId());

            statement.executeUpdate();
            return veterinarian;

        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar el veterinario", e);
        }
    }

    @Override
    public void changeStatus(int id, boolean status) {
        // El status vive en "users", no en "veterinarian" — se actualiza a través del id_user
        String sql = "UPDATE users SET status = ? WHERE id = (SELECT id_user FROM veterinarian WHERE id = ?)";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, status);
            statement.setInt(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Error al cambiar el estado del veterinario", e);
        }
    }

    @Override
    public List<Veterinarian> findBySpecialty(int specialtyId) {
        String sql = buildBaseSelect() + " WHERE v.id_specialty = ?";
        List<Veterinarian> veterinarians = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, specialtyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    veterinarians.add(mapRow(resultSet));
                }
                return veterinarians;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar veterinarios por especialidad", e);
        }
    }

    private String buildBaseSelect() {
        return "SELECT v.id AS v_id, v.professional_license, "
                + "u.id AS u_id, u.identification_type, u.identification_number, u.first_name, "
                + "u.last_name, u.phone_number, u.email, u.password, u.status AS u_status, "
                + "u.registration_date, "
                + "r.id AS r_id, r.name AS r_name, "
                + "s.id AS s_id, s.name AS s_name "
                + "FROM veterinarian v "
                + "JOIN users u ON v.id_user = u.id "
                + "JOIN role r ON u.id_rol = r.id "
                + "JOIN specialty s ON v.id_specialty = s.id";
    }

    private Veterinarian mapRow(ResultSet resultSet) throws SQLException {
        Role role = new Role(
                resultSet.getInt("r_id"),
                resultSet.getString("r_name")
        );

        User user = new User(
                resultSet.getInt("u_id"),
                IdentificationType.valueOf(resultSet.getString("identification_type")),
                resultSet.getString("identification_number"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("phone_number"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getBoolean("u_status"),
                resultSet.getTimestamp("registration_date").toLocalDateTime(),
                role
        );

        Specialty specialty = new Specialty(
                resultSet.getInt("s_id"),
                resultSet.getString("s_name")
        );

        return new Veterinarian(
                resultSet.getInt("v_id"),
                resultSet.getString("professional_license"),
                user,
                specialty
        );
    }
}
