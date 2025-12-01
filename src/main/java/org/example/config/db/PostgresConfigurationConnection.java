package org.example.config.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConfigurationConnection {
    String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String password = "";

    // Создание соединения
    public Connection getConnection() {
        try (
                Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Подключение к базе данных установлено успешно!");
        } catch (SQLException e) {
            System.err.println("Ошибка при подключении к базе данных: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
