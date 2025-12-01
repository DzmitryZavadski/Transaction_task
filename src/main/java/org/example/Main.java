package org.example;


import org.example.model.User;
import org.example.service.UserService;
import org.example.service.userServiceImpl.UserServiceIml;

import java.util.List;

public class Main {
    static void main() {
        UserService userService=new UserServiceIml();

//        System.out.println("Select all users");
//        List<User> userList = userService.getAllUsers();
//        userList.forEach(System.out::println);

//        System.out.println("*********");

//        System.out.println("Find user by id");
//        System.out.println(userService.getUser(2));

        System.out.println("*********");

        System.out.println("Save user");

        User user=new User();
        user.setName("Ivan");
        user.setAge(38);
        user.setEmail("ivan@email.com");
        System.out.println("saved user = " + userService.createUser(user));

        System.out.println("*************");

//        System.out.println("delete user");
//        userService.deleteUser(2);
    }
}
