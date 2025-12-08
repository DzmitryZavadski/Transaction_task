package org.example.service.connectionPoolServiceImpl;

import org.example.config.connection.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ConnectionPoolService {
    static void main() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("database");
        String dbUrl = resourceBundle.getString("dbUrl");
        String dbUserName = resourceBundle.getString("dbUser");
        String dbPassword = resourceBundle.getString("dbPass");

        ConnectionPool pool = new ConnectionPool(dbUrl, dbUserName, dbPassword, 2);
        Connection conn = null;
        try {
            conn = pool.getConnection();
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM users");
                System.out.println("There are below tables");
                while (rs.next()) {
                    System.out.print(rs.getString("id"));
                    System.out.print("| ");
                    System.out.print(rs.getString("id"));
                    System.out.print("| ");
                    System.out.print(rs.getString("id"));
                    System.out.print("| ");
                    System.out.print(rs.getString("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    pool.returnConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
