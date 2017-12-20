package com.test.ankitjain.registerservice.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.test.ankitjain.registerservice.exception.InvalidParameterException;

@Component
public class RequestValidator {
    
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,})";
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]*$";
    
    private Pattern passwordPattern;
    private Pattern usernamePattern;
    private Matcher matcher;
    
    public RequestValidator(){
        passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        usernamePattern = Pattern.compile(USERNAME_PATTERN);
    }
    
    public boolean validateUsername(final String username) throws InvalidParameterException {
        matcher = usernamePattern.matcher(username);
        if (matcher.matches())
            return true;
        throw new InvalidParameterException("Username (alphanumerical, no spaces) is invalid");
    }
    
    public boolean validatePassword(final String password) throws InvalidParameterException {
        matcher = passwordPattern.matcher(password);
        if (matcher.matches())
            return true;
        throw new InvalidParameterException("Password (at least four characters, at least one upper case character, at least one number) is invalid");
    }
}
