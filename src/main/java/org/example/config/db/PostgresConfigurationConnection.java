package org.example.config.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConfigurationConnection {
    public static String url = "jdbc:postgresql://localhost:5432/postgres";
    public static String user = "postgres";
    public static String password = "postgres";

    // Создание соединения
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection( url, user, password );
        } catch (SQLException e) {
            System.out.println("Can't connect to database." + e.getMessage());
        }
        return null;
    }
}
