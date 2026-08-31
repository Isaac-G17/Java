/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.dao.impl;

import com.vetcare.config.ConnectionDB;
import com.vetcare.dao.AttentionDetailsDAO;
import com.vetcare.exception.PersistenceException;
import com.vetcare.model.AttentionDetails;
import com.vetcare.model.Medication;
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
public class AttentionDetailsDAOImpl implements AttentionDetailsDAO {

    @Override
    public AttentionDetails save(AttentionDetails attentionDetails) {
        String sql = "INSERT INTO attention_details (quantity, id_attention, id_medication) "
                + "VALUES (?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, attentionDetails.getQuantity());
            statement.setInt(2, attentionDetails.getAttention().getId());
            statement.setInt(3, attentionDetails.getMedication().getId());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    attentionDetails.setId(generatedKeys.getInt(1));
                }
            }

            return attentionDetails;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar el detalle de atención", e);
        }
    }

    @Override
    public List<AttentionDetails> findByAttentionId(int attentionId) {
        // Trae el detalle + el medicamento completo (necesario para mostrar
        // nombre, presentación, etc. en el historial médico), pero NO reconstruye
        // la Attention completa aquí, porque quien llame a este método ya la tiene
        String sql = "SELECT ad.id AS ad_id, ad.quantity AS ad_quantity, "
                + "m.id AS m_id, m.code AS m_code, m.name AS m_name, "
                + "m.presentation AS m_presentation, m.laboratory AS m_laboratory, "
                + "m.available_quantity AS m_available_quantity, "
                + "m.minimum_quantity AS m_minimum_quantity, m.price AS m_price, "
                + "m.status AS m_status, m.registration_date AS m_registration_date "
                + "FROM attention_details ad "
                + "JOIN medication m ON ad.id_medication = m.id "
                + "WHERE ad.id_attention = ?";

        List<AttentionDetails> details = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, attentionId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Medication medication = new Medication(
                            resultSet.getInt("m_id"),
                            resultSet.getString("m_code"),
                            resultSet.getString("m_name"),
                            resultSet.getString("m_presentation"),
                            resultSet.getString("m_laboratory"),
                            resultSet.getInt("m_available_quantity"),
                            resultSet.getInt("m_minimum_quantity"),
                            resultSet.getBigDecimal("m_price"),
                            resultSet.getBoolean("m_status"),
                            resultSet.getTimestamp("m_registration_date").toLocalDateTime()
                    );

                    AttentionDetails detail = new AttentionDetails(
                            resultSet.getInt("ad_id"),
                            resultSet.getInt("ad_quantity"),
                            null,  // no reconstruimos la Attention aquí — ver nota abajo
                            medication
                    );

                    details.add(detail);
                }
                return details;
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar los detalles de la atención", e);
        }
    }
}
