/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao.impl;

import com.vetcare.config.ConnectionDB;
import com.vetcare.dao.RoleDAO;
import com.vetcare.exception.PersistenceException;
import com.vetcare.model.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gu_Zra
 */
public class RoleDAOImpl implements RoleDAO {

    @Override
    public Optional<Role> findById(int id) {
        String sql = "SELECT * FROM role WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnectionDB(); 
              PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);  // reemplaza el primer "?" por el valor de id

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {  // ¿hay una fila?
                    Role role = new Role(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                    return Optional.of(role);
                }
                return Optional.empty();  // no se encontró nada
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar el rol", e);
        }
    }

    @Override
    public List<Role> findAll() {
        String sql = "SELECT * FROM role";
        List<Role> roles = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
               PreparedStatement statement = connection.prepareStatement(sql)) {
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) { 
                    Role role = new Role(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                    roles.add(role);
                }
                return roles;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar el rol", e);

        }
    }

}
