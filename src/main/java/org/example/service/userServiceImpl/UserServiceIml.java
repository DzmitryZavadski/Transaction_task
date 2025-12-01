package org.example.service.userServiceImpl;

import org.example.model.User;
import org.example.service.UserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServiceIml implements UserService {
    private static final String FIND_ALL_STATEMENTS = "SELECT id, name, age, email FROM Users";
    private static final String FIND_BY_ID_STATEMENT = "SELECT id, name, age, email FROM Users WHERE id = ?";
    private static final String SAVE_STATEMENT = "INSERT INTO Users(name, age, email) VALUES (?, ?, ?)";
    private static final String DELETE_STATEMENT = "DELETE FROM Users WHERE id = ?";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "postgres"
            );
        } catch (SQLException e) {
            System.out.println("Can't connect to database." + e.getMessage());
        }
        return null;
    }

    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User createUser(User user) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_STATEMENT);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setString(3, user.getEmail());
            int id = preparedStatement.executeUpdate();
            user.setId(id);
        } catch (SQLException e) {
            System.out.println("Exception while saving user" + e.getMessage());
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_STATEMENTS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("user_name");
                Integer age = resultSet.getInt("age");
                String email = resultSet.getString("email");
                user.setId(id);
                user.setName(name);
                user.setAge(age);
                user.setEmail(email);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Exception while getting users" + e.getMessage());;
            close(connection);
        }
        close(connection);
        return users;
    }

    @Override
    public User getUser(int id) {
        User user = null;
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_STATEMENT);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new User();
                String name = resultSet.getString("user_name");
                Integer age = resultSet.getInt("age");
                String email = resultSet.getString("email");
                user.setId(id);
                user.setName(name);
                user.setAge(age);
                user.setEmail(email);
            }
        } catch (SQLException e) {
            System.out.println("Exception while getting user by id" + e.getMessage());;
            close(connection);
        }
        close(connection);
        return user;
    }

//    @Override
//    public void updateUser(int id, String email) {
//        Connection connection = getConnection();
//
//    }

    @Override
    public void deleteUser(int id) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STATEMENT);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Exception while saving user" + e.getMessage());
        }
    }
}
