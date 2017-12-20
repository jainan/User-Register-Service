package com.test.ankitjain.registerservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.test.ankitjain.registerservice.exception.InvalidParameterException;
import com.test.ankitjain.registerservice.exception.ResourceNotFoundException;
import com.test.ankitjain.registerservice.model.User;

@Component
public class UserService {

    private List<User> users = new ArrayList<>();

    public User getUser(String username) throws ResourceNotFoundException {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        throw new ResourceNotFoundException("User with Username " + username + " not found");
    }

    public List<User> getAllUsers() throws ResourceNotFoundException {
        if (users.size() > 0) {
            return users;
        }
        throw new ResourceNotFoundException("Users not found");
    }

    public void registerUser(User user) {

        if (!users.contains(user)) {
            users.add(user);
        }
    }

    public boolean isUserRegistered(User user) throws InvalidParameterException {
        if (!users.contains(user))
            return false;
        throw new InvalidParameterException("User already  registerred");
    }

}
