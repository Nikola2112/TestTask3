package com.goit;


import java.sql.*;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5433/postgresDB";
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASSWORD = "flash8898";

    public static void saveEquation(Equation equation) {
        if (!equation.isValid()) {
            System.out.println("Рівняння недійсне. Неможливо зберегти в базу даних.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String sql = "INSERT INTO equation (equation) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, equation.getEquation());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveRoot(Equation equation, double root) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String sql = "INSERT INTO roots (equation_id, root) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, equation.getEquation());
                preparedStatement.setDouble(2, root);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
