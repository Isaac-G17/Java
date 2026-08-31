/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao.impl;

import com.vetcare.config.ConnectionDB;
import com.vetcare.dao.SpecialtyDAO;
import com.vetcare.exception.PersistenceException;
import com.vetcare.model.Specialty;
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
public class SpecialtyDAOImpl implements SpecialtyDAO{

    @Override
    public Specialty save(Specialty specialty) {
        String sql = "INSERT INTO specialty (name) VALUES (?)";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, specialty.getName());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    specialty.setId(generatedKeys.getInt(1));
                }
            }

            return specialty;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar la especialidad", e);
        }
    }

    @Override
    public Optional<Specialty> findById(int id) {
        String sql = "SELECT * FROM specialty WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnectionDB(); 
              PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id); 

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {  
                    Specialty specialty = new Specialty(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                    return Optional.of(specialty);
                }
                return Optional.empty(); 
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar la especialidad", e);
// por ahora, temporal — luego lo reemplazamos por tu excepción personalizada
        }
    }

    @Override
    public List<Specialty> findAll() {
        String sql = "SELECT * FROM specialty";
        List<Specialty> specialties = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
               PreparedStatement statement = connection.prepareStatement(sql)) {
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) { 
                    Specialty specialty = new Specialty(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                    specialties.add(specialty);
                }
                return specialties;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar la especialidad", e);

        }
    }
}
