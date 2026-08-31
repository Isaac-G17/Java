/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao.impl;

import com.vetcare.config.ConnectionDB;
import com.vetcare.dao.OwnerDAO;
import com.vetcare.enums.IdentificationType;
import com.vetcare.exception.PersistenceException;
import com.vetcare.model.Owner;
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
public class OwnerDAOImpl implements OwnerDAO {

    @Override
    public Owner save(Owner owner) {
        String sql = "INSERT INTO owner (identification_type, identification_number, first_name, "
                + "last_name, phone_number, email, address, status, registration_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql, 
                     Statement.RETURN_GENERATED_KEYS)) {
            // Postgres que te devuelva el "id" que generó automáticamente
            
            statement.setString(1, owner.getIdentificationType().name());
            statement.setString(2, owner.getIdentificationNumber());
            statement.setString(3, owner.getFirstName());
            statement.setString(4, owner.getLastName());
            statement.setString(5, owner.getPhoneNumber());
            statement.setString(6, owner.getEmail());
            statement.setString(7, owner.getAddress());
            statement.setBoolean(8, owner.getStatus());
            statement.setObject(9, owner.getRegistrationDate());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    owner.setId(generatedKeys.getInt(1));
                }
            }

            return owner;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar el propietario", e);
        }
    }

    @Override
    public List<Owner> findAll() {
        String sql = "SELECT * FROM owner";
        List<Owner> owners = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Owner owner = mapRow(resultSet);
                    owners.add(owner);
                }
                return owners;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar los propietarios", e);
        }
    }

    @Override
    public Owner update(Owner owner) {
        String sql = "UPDATE owner SET identification_type = ?, identification_number = ?, "
                + "first_name = ?, last_name = ?, phone_number = ?, email = ?, address = ? "
                + "WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, owner.getIdentificationType().name());
            statement.setString(2, owner.getIdentificationNumber());
            statement.setString(3, owner.getFirstName());
            statement.setString(4, owner.getLastName());
            statement.setString(5, owner.getPhoneNumber());
            statement.setString(6, owner.getEmail());
            statement.setString(7, owner.getAddress());
            statement.setInt(8, owner.getId());

            statement.executeUpdate();
            return owner;

        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar el propietario", e);
        }
    }

    @Override
    public void changeStatus(int id, boolean status) {
        String sql = "UPDATE owner SET status = ? WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, status);
            statement.setInt(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Error al cambiar el estado del propietario", e);
        }
    }

    @Override
    public Optional<Owner> findByIdentificationNumber(String identificationNumber) {
        String sql = "SELECT * FROM owner WHERE identification_number = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, identificationNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar al propietario", e);
        }
    }
    
    @Override
    public Optional<Owner> findById(int id) {
        String sql = "SELECT * FROM owner WHERE id = ?";

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
            throw new PersistenceException("Error al buscar el propietario por id", e);
        }
    }

    // Método privado auxiliar: evita repetir el mismo mapeo en findAll y findByIdentificationNumber
    private Owner mapRow(ResultSet resultSet) throws SQLException {
        return new Owner(
                resultSet.getInt("id"),
                IdentificationType.valueOf(resultSet.getString("identification_type")),
                resultSet.getString("identification_number"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("phone_number"),
                resultSet.getString("email"),
                resultSet.getString("address"),
                resultSet.getBoolean("status"),
                resultSet.getTimestamp("registration_date").toLocalDateTime()
        );
    }
}