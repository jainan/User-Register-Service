package com.test.registerservice.service;

import java.util.List;

import com.test.registerservice.exception.InvalidParameterException;
import com.test.registerservice.exception.ResourceNotFoundException;
import com.test.registerservice.model.User;

public interface UserInterface {

    User getUser(String username) throws ResourceNotFoundException;
    List<User> getAllUsers() throws ResourceNotFoundException;
    void registerUser(User user);
    boolean isUserRegistered(User user) throws InvalidParameterException;
}
