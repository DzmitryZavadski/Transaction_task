package org.example;


import org.example.model.User;
import org.example.service.UserService;
import org.example.service.userServiceImpl.UserServiceIml;

import java.util.List;

public class Main {
    static void main() {
        UserService userService=new UserServiceIml();

        //find all
//        System.out.println("Select all users");
//        List<User> userList = userService.getAllUsers();
//        userList.forEach(System.out::println);
//
//        System.out.println("*********");

        //find by id
//        System.out.println("Find user by id");
//        System.out.println(userService.getUser(2));

        //create
        System.out.println("*********");

        System.out.println("Save user");

        User user=new User();
        user.setId(10);
        user.setName("Artiom");
        user.setAge(33);
        user.setEmail("artiom@mail.com");
        System.out.println("saved user = " + userService.createUser(user));

        System.out.println("*************");

        //delete
//        System.out.println("delete user");
//        userService.deleteUser(2);

        //update
//        System.out.println("*********");
//        System.out.println("Update user");
//        User user = userService.updateUser(3, "test@email.com");
//        System.out.println(user);
//
//        System.out.println("*********");
    }
}
