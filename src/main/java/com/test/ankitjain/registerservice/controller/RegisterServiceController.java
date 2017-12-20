package com.test.ankitjain.registerservice.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.test.ankitjain.registerservice.exception.InvalidParameterException;
import com.test.ankitjain.registerservice.exception.ResourceNotFoundException;
import com.test.ankitjain.registerservice.helper.RequestValidator;
import com.test.ankitjain.registerservice.model.User;
import com.test.ankitjain.registerservice.service.ExclusionServiceMock;
import com.test.ankitjain.registerservice.service.UserService;

@RestController
public class RegisterServiceController {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestValidator validator;
    
    @Autowired
    private ExclusionServiceMock ExclusionService;
    
    @GetMapping("/user/{username}")
    public User getUser(@PathVariable String username) throws ResourceNotFoundException {
        return userService.getUser(username);
    }
    
    @GetMapping("user")
    public List<User> getAllUsers() throws ResourceNotFoundException {
        return userService.getAllUsers();
    }

    @PostMapping("/user")
    public ResponseEntity<Void> register(@RequestBody User user) throws InvalidParameterException {

        if (user == null)
            return ResponseEntity.noContent().build();

        if (!validator.validateUsername(user.getUsername()) || !validator.validatePassword(user.getPassword()))
            return ResponseEntity.badRequest().build();

        if (userService.isUserRegistered(user))
            return ResponseEntity.badRequest().build();
        
        if(ExclusionService.validate(user.getDob(), user.getSsn()))
            return ResponseEntity.badRequest().build();
        
        userService.registerUser(user);

        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{username}").buildAndExpand(user.getUsername()).toUri();
        
        return ResponseEntity.created(location).build();
    }
}
