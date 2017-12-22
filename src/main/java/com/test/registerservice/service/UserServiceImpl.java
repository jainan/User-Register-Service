package com.test.registerservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.test.registerservice.exception.InvalidParameterException;
import com.test.registerservice.exception.ResourceNotFoundException;
import com.test.registerservice.model.User;

@Service
public class UserServiceImpl implements UserInterface {

    private List<User> users = new ArrayList<>();

    @Override
    public User getUser(String username) throws ResourceNotFoundException {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        throw new ResourceNotFoundException("User with Username " + username + " not found");
    }

    @Override
    public List<User> getAllUsers() throws ResourceNotFoundException {
        if (users.size() > 0) {
            return users;
        }
        throw new ResourceNotFoundException("Users not found");
    }

    @Override
    public void registerUser(User user) {

        if (!users.contains(user)) {
            users.add(user);
        }
    }

    @Override
    public boolean isUserRegistered(User user) throws InvalidParameterException {
        if (!users.contains(user))
            return false;
        throw new InvalidParameterException("User already  registerred");
    }
}
