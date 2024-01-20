package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        List<User> users = null;
        try {
            users = userService.getUsers();
        } catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users available");
        }
        return users;
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id){
        User user = null;
        try {
            user = userService.findById(id);
        } catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + id + " NOT FOUND");
        }
        return user;
    }
}
