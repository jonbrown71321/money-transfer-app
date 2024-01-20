package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao){
        this.userDao = userDao;
    }


    public List<User> getUsers(){
        return userDao.getUsers();
    }

    public User findById(int id){
        return userDao.getUserById(id);
    }

    public User findByUsername(String username){
        return userDao.getUserByUsername(username);
    }
}
