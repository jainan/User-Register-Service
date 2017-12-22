package com.test.registerservice.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.test.registerservice.exception.InvalidParameterException;
import com.test.registerservice.exception.ResourceNotFoundException;
import com.test.registerservice.model.User;
import com.test.registerservice.service.ExclusionService;
import com.test.registerservice.service.UserServiceImpl;

@RestController
@RequestMapping("/users")
public class RegisterServiceController {

    private final Logger LOGGER = LoggerFactory.getLogger(RegisterServiceController.class);

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ExclusionService exclusionService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers() throws ResourceNotFoundException {
        LOGGER.info("getting all users");
        List<User> users = userService.getAllUsers();
        if (users == null || users.isEmpty()) {
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable("username") String username) throws ResourceNotFoundException {
        LOGGER.info("getting user with username: {}", username);
        User user = userService.getUser(username);
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> register(@RequestBody User user) throws InvalidParameterException {

        LOGGER.info("creating new user: {}", user);

        if (user == null)
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

        if(user.getUsername() == null || user.getPassword() == null)
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        
        if (!user.validateUsername(user.getUsername()) || !user.validatePassword(user.getPassword())) {
            LOGGER.info("Either Username " + user.getUsername() + " or password " + user.getPassword() + " is invalid. test");
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        if (userService.isUserRegistered(user)) {
            LOGGER.info("User with name " + user.getUsername() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        if (!exclusionService.validate(user.getDob(), user.getSsn())) {
            LOGGER.info("Either DOB " + user.getDob() + " or SSN " + user.getSsn() + " is in blacklist.");
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        
        userService.registerUser(user);

        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}").buildAndExpand(user.getUsername()).toUri();
        return ResponseEntity.created(location).build();
    }

    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    public void setExclusionService(ExclusionService exclusionService) {
        this.exclusionService = exclusionService;
    }
}
