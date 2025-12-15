package org.example.service.userServiceImpl;

import org.example.config.db.config.PostgresConfigurationConnection;
import org.example.model.User;
import org.example.service.UserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServiceIml implements UserService {
    private static final String FIND_ALL_STATEMENTS = "SELECT id, name, age, email FROM Users";
    private static final String FIND_BY_ID_STATEMENT = "SELECT * FROM Users WHERE id = ?";
    private static final String SAVE_STATEMENT = "INSERT INTO Users(name, age, email) VALUES (?, ?, ?)";
    private static final String DELETE_STATEMENT = "DELETE FROM Users WHERE id = ?";
    private static final String UPDATE_STATEMENT = "UPDATE Users SET EMAIL = ? WHERE id = ?";

    private PostgresConfigurationConnection getDbConnection = new PostgresConfigurationConnection();

    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User createUser(User user) {
        Connection connection = getDbConnection.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Exception while saving user" + e.getMessage());
            close(connection);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection connection = getDbConnection.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_STATEMENTS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Integer age = resultSet.getInt("age");
                String email = resultSet.getString("email");
                user.setId(id);
                user.setName(name);
                user.setAge(age);
                user.setEmail(email);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Exception while getting users" + e.getMessage());
            close(connection);
        }
        close(connection);
        return users;
    }

    @Override
    public User getUser(int id) {
        User user = null;
        Connection connection = getDbConnection.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_STATEMENT);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setAge(resultSet.getInt("age"));
                user.setEmail(resultSet.getString("email"));
            }
            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            System.out.println("Exception while getting user by id" + e.getMessage());
            close(connection);
        }
        close(connection);
        return user;
    }

    @Override
    public User updateUser(int id, String email) {
        Connection connection = getDbConnection.getConnection();
        User user = getUser(id);
        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement(UPDATE_STATEMENT);
            preparedStatement1.setString(1, email);
            preparedStatement1.setInt(2, id);
            preparedStatement1.executeUpdate();

            ResultSet resultSet = preparedStatement1.executeQuery();
            user.setId(id);
            user.setEmail(email);
            while (resultSet.next()) {
                resultSet.getInt("id");
                resultSet.getString("name");
                resultSet.getInt("age");
                resultSet.getString("email");
            }
            resultSet.close();
            preparedStatement1.close();
            close(connection);

        } catch (SQLException e) {
            System.out.println("Exception while update user" + e.getMessage());
            close(connection);
        }
        close(connection);
        return user;
    }

    @Override
    public void deleteUser(int id) {
        Connection connection = getDbConnection.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STATEMENT);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Exception while saving user" + e.getMessage());
            close(connection);
        }
    }
}
