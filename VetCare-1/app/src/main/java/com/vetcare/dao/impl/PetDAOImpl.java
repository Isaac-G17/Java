/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao.impl;

import com.vetcare.config.ConnectionDB;
import com.vetcare.dao.PetDAO;
import com.vetcare.enums.IdentificationType;
import com.vetcare.enums.Sex;
import com.vetcare.exception.PersistenceException;
import com.vetcare.model.Owner;
import com.vetcare.model.Pet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public class PetDAOImpl implements PetDAO {

    @Override
    public Pet save(Pet pet) {
        String sql = "INSERT INTO pet (name, species, breed, sex, date_of_birth, weight, "
                + "status, registration_date, id_owner) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             // 👆 Postgres devuelve el "id" que generó automáticamente
             
            statement.setString(1, pet.getName());
            statement.setString(2, pet.getSpecies());
            statement.setString(3, pet.getBreed());
            statement.setString(4, pet.getSex().name());
            statement.setObject(5, pet.getDateOfBirth());
            statement.setBigDecimal(6, pet.getWeight());
            statement.setBoolean(7, pet.getStatus());
            statement.setObject(8, pet.getRegistrationDate());
            statement.setInt(9, pet.getOwner().getId());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pet.setId(generatedKeys.getInt(1));
                }
            }

            return pet;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar la mascota", e);
        }
    }

    @Override
    public List<Pet> findAll() {
        String sql = "SELECT p.*, o.identification_type AS o_identification_type, "
                + "o.identification_number AS o_identification_number, o.first_name AS o_first_name, "
                + "o.last_name AS o_last_name, o.phone_number AS o_phone_number, o.email AS o_email, "
                + "o.address AS o_address, o.status AS o_status, o.registration_date AS o_registration_date "
                + "FROM pet p JOIN owner o ON p.id_owner = o.id";

        List<Pet> pets = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pets.add(mapRow(resultSet));
                }
                return pets;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar las mascotas", e);
        }
    }

    @Override
    public Pet update(Pet pet) {
        String sql = "UPDATE pet SET name = ?, species = ?, breed = ?, sex = ?, "
                + "date_of_birth = ?, weight = ?, id_owner = ? WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, pet.getName());
            statement.setString(2, pet.getSpecies());
            statement.setString(3, pet.getBreed());
            statement.setString(4, pet.getSex().name());
            statement.setObject(5, pet.getDateOfBirth());
            statement.setBigDecimal(6, pet.getWeight());
            statement.setInt(7, pet.getOwner().getId());
            statement.setInt(8, pet.getId());

            statement.executeUpdate();
            return pet;

        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar la mascota", e);
        }
    }

    @Override
    public void changeStatus(int id, boolean status) {
        String sql = "UPDATE pet SET status = ? WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, status);
            statement.setInt(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Error al cambiar el estado de la mascota", e);
        }
    }

    @Override
    public Optional<Pet> findByName(String name) {
        String sql = "SELECT p.*, o.identification_type AS o_identification_type, "
                + "o.identification_number AS o_identification_number, o.first_name AS o_first_name, "
                + "o.last_name AS o_last_name, o.phone_number AS o_phone_number, o.email AS o_email, "
                + "o.address AS o_address, o.status AS o_status, o.registration_date AS o_registration_date "
                + "FROM pet p JOIN owner o ON p.id_owner = o.id WHERE p.name = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar la mascota por nombre", e);
        }
    }

    @Override
    public List<Pet> findByOwnerId(int ownerId) {
        String sql = "SELECT p.*, o.identification_type AS o_identification_type, "
                + "o.identification_number AS o_identification_number, o.first_name AS o_first_name, "
                + "o.last_name AS o_last_name, o.phone_number AS o_phone_number, o.email AS o_email, "
                + "o.address AS o_address, o.status AS o_status, o.registration_date AS o_registration_date "
                + "FROM pet p JOIN owner o ON p.id_owner = o.id WHERE p.id_owner = ?";

        List<Pet> pets = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, ownerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pets.add(mapRow(resultSet));
                }
                return pets;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar mascotas del propietario", e);
        }
    }
    
    @Override
    public Optional<Pet> findById(int id) {
        String sql = "SELECT p.*, o.identification_type AS o_identification_type, "
                + "o.identification_number AS o_identification_number, o.first_name AS o_first_name, "
                + "o.last_name AS o_last_name, o.phone_number AS o_phone_number, o.email AS o_email, "
                + "o.address AS o_address, o.status AS o_status, o.registration_date AS o_registration_date "
                + "FROM pet p JOIN owner o ON p.id_owner = o.id WHERE p.id = ?";

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
            throw new PersistenceException("Error al buscar la mascota por id", e);
        }
    }

    // Mapea una fila combinada de pet + owner (por el JOIN) a un objeto Pet completo
    private Pet mapRow(ResultSet resultSet) throws SQLException {
        Owner owner = new Owner(
                resultSet.getInt("id_owner"),
                IdentificationType.valueOf(resultSet.getString("o_identification_type")),
                resultSet.getString("o_identification_number"),
                resultSet.getString("o_first_name"),
                resultSet.getString("o_last_name"),
                resultSet.getString("o_phone_number"),
                resultSet.getString("o_email"),
                resultSet.getString("o_address"),
                resultSet.getBoolean("o_status"),
                resultSet.getTimestamp("o_registration_date").toLocalDateTime()
        );

        return new Pet(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("species"),
                resultSet.getString("breed"),
                Sex.valueOf(resultSet.getString("sex")),
                resultSet.getObject("date_of_birth", LocalDate.class),
                resultSet.getBigDecimal("weight"),
                resultSet.getBoolean("status"),
                resultSet.getTimestamp("registration_date").toLocalDateTime(),
                owner
        );
    }
}
