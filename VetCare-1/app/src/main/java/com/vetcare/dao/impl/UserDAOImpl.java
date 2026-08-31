/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao.impl;

import com.vetcare.config.ConnectionDB;
import com.vetcare.dao.UserDAO;
import com.vetcare.enums.IdentificationType;
import com.vetcare.exception.PersistenceException;
import com.vetcare.model.Role;
import com.vetcare.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public class UserDAOImpl implements UserDAO {

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (identification_type, identification_number, first_name, "
                + "last_name, phone_number, email, password, status, registration_date, id_rol) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getIdentificationType().name());
            statement.setString(2, user.getIdentificationNumber());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getPhoneNumber());
            statement.setString(6, user.getEmail());
            statement.setString(7, user.getPassword());
            statement.setBoolean(8, user.getStatus());
            statement.setObject(9, user.getRegistrationDate());
            statement.setInt(10, user.getRole().getId());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }

            return user;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar el usuario", e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = buildBaseSelect();
        List<User> users = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(mapRow(resultSet));
                }
                return users;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar los usuarios", e);
        }
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET identification_type = ?, identification_number = ?, "
                + "first_name = ?, last_name = ?, phone_number = ?, email = ? WHERE id = ?";
        // No toca password, status ni id_rol aquí a propósito — cada uno debería
        // tener su propio método específico (changeStatus ya existe; password y rol
        // se podrían agregar como métodos separados si el negocio los llega a pedir)

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getIdentificationType().name());
            statement.setString(2, user.getIdentificationNumber());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getPhoneNumber());
            statement.setString(6, user.getEmail());
            statement.setInt(7, user.getId());

            statement.executeUpdate();
            return user;

        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar el usuario", e);
        }
    }

    @Override
    public void changeStatus(int id, boolean status) {
        String sql = "UPDATE users SET status = ? WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, status);
            statement.setInt(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Error al cambiar el estado del usuario", e);
        }
    }

    @Override
    public Optional<User> findByUserEmail(String email) {
        String sql = buildBaseSelect() + " WHERE u.email = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar el usuario", e);
        }
    }

    @Override
    public Optional<User> findByCredentials(String username, String password) {
        String sql = buildBaseSelect() + " WHERE u.email = ? AND u.password = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al validar las credenciales", e);
        }
    }

    private String buildBaseSelect() {
        return "SELECT u.id AS u_id, u.identification_type, u.identification_number, u.first_name, "
                + "u.last_name, u.phone_number, u.email, u.password, u.status AS u_status, "
                + "u.registration_date, r.id AS r_id, r.name AS r_name "
                + "FROM users u JOIN role r ON u.id_rol = r.id";
    }

    private User mapRow(ResultSet resultSet) throws SQLException {
        Role role = new Role(
                resultSet.getInt("r_id"),
                resultSet.getString("r_name")
        );

        return new User(
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
    }
}