package org.example.service;

import org.example.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    List<User> getAllUsers();
    User getUser(int id);
//    User updateUser(int id, String email);
    void deleteUser(int id);
}
