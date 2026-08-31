/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao.impl;

import com.vetcare.config.ConnectionDB;
import com.vetcare.dao.MedicationDAO;
import com.vetcare.exception.MedicineNotFoundException;
import com.vetcare.exception.PersistenceException;
import com.vetcare.model.Medication;
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
public class MedicationDAOImpl implements MedicationDAO {

    @Override
    public Medication save(Medication medication) {
        String sql = "INSERT INTO medication (code, name, presentation, laboratory, "
                + "available_quantity, minimum_quantity, price, status, registration_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, medication.getCode());
            statement.setString(2, medication.getName());
            statement.setString(3, medication.getPresentation());
            statement.setString(4, medication.getLaboratory());
            statement.setInt(5, medication.getAvailableQuantity());
            statement.setInt(6, medication.getMinimumQuantity());
            statement.setBigDecimal(7, medication.getPrice());
            statement.setBoolean(8, medication.isStatus());
            statement.setObject(9, medication.getRegistrationDate());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    medication.setId(generatedKeys.getInt(1));
                }
            }

            return medication;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar el medicamento", e);
        }
    }

    @Override
    public List<Medication> findAll() {
        String sql = "SELECT * FROM medication";
        List<Medication> medications = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    medications.add(mapRow(resultSet));
                }
                return medications;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar los medicamentos", e);
        }
    }

    @Override
    public Medication update(Medication medication) {
        String sql = "UPDATE medication SET code = ?, name = ?, presentation = ?, laboratory = ?, "
                + "price = ? WHERE id = ?";
        // Nota: no toca available_quantity, minimum_quantity ni status a propósito
        // (esos van por updateStock y changeStatus, cada uno con su propia responsabilidad)

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, medication.getCode());
            statement.setString(2, medication.getName());
            statement.setString(3, medication.getPresentation());
            statement.setString(4, medication.getLaboratory());
            statement.setBigDecimal(5, medication.getPrice());
            statement.setInt(6, medication.getId());

            statement.executeUpdate();
            return medication;

        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar el medicamento", e);
        }
    }

    @Override
    public Medication updateStock(int id, int newAvailableQuantity) {
        String sql = "UPDATE medication SET available_quantity = ? WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, newAvailableQuantity);
            statement.setInt(2, id);

            statement.executeUpdate();

            // Devuelve el medicamento actualizado, releyéndolo de la base de datos
            return findById(id).orElseThrow(() -> new MedicineNotFoundException(id));

        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar el inventario", e);
        }
    }

    @Override
    public void changeStatus(int id, boolean status) {
        String sql = "UPDATE medication SET status = ? WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, status);
            statement.setInt(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Error al cambiar el estado del medicamento", e);
        }
    }

    @Override
    public List<Medication> findLowStock() {
        String sql = "SELECT * FROM medication WHERE available_quantity < minimum_quantity";
        List<Medication> medications = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    medications.add(mapRow(resultSet));
                }
                return medications;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar medicamentos con bajo inventario", e);
        }
    }

    // Método auxiliar interno, necesario para updateStock() — no está en la interfaz
    // porque el contrato MedicationDAO no lo pide explícitamente, pero lo necesitas
    // internamente para poder devolver el objeto actualizado
   @Override
    public Optional<Medication> findById(int id) {
        String sql = "SELECT * FROM medication WHERE id = ?";
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
            throw new PersistenceException("Error al buscar el medicamento por id", e);
        }
    }
    
    private Medication mapRow(ResultSet resultSet) throws SQLException {
        return new Medication(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("name"),
                resultSet.getString("presentation"),
                resultSet.getString("laboratory"),
                resultSet.getInt("available_quantity"),
                resultSet.getInt("minimum_quantity"),
                resultSet.getBigDecimal("price"),
                resultSet.getBoolean("status"),
                resultSet.getTimestamp("registration_date").toLocalDateTime()
        );
    }
}
