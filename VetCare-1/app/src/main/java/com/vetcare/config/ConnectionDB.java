/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetcare.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Gu_Zra
 */
public class ConnectionDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/vetcare";
    private static final String USER = "postgres";
    private static final String PASS = "123456";

    public static Connection getConnectionDB() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
