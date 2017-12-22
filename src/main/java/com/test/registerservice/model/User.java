package com.test.registerservice.model;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.registerservice.exception.InvalidParameterException;

public class User implements Serializable {
    
    private final Logger LOGGER = LoggerFactory.getLogger(User.class);

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,})";
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]*$";

    private Pattern passwordPattern;
    private Pattern usernamePattern;
    private Matcher matcher;

    private String username;
    private String password;
    private String dob;
    private String ssn;

    public User() {
        passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        usernamePattern = Pattern.compile(USERNAME_PATTERN);
    }

    public User(String username, String password, String dob, String ssn) {
        this.username = username;
        this.password = password;
        this.dob = dob;
        this.ssn = ssn;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
    public String getSsn() {
        return ssn;
    }
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dob == null) ? 0 : dob.hashCode());
        result = prime * result + ((ssn == null) ? 0 : ssn.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (dob == null) {
            if (other.dob != null)
                return false;
        } else if (!dob.equals(other.dob))
            return false;
        if (ssn == null) {
            if (other.ssn != null)
                return false;
        } else if (!ssn.equals(other.ssn))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + ", dob=" + dob + ", ssn=" + ssn + "]";
    }

    public boolean validateUsername(final String username) throws InvalidParameterException {
        matcher = usernamePattern.matcher(username);
        if (matcher.matches()) {
            LOGGER.info("username matches the criteria");
            return true;
        }
        LOGGER.info("username doesn't matches the criteria");
        throw new InvalidParameterException("Username (alphanumerical, no spaces) is invalid");
    }

    public boolean validatePassword(final String password) throws InvalidParameterException {
        matcher = passwordPattern.matcher(password);
        if (matcher.matches()) {
            LOGGER.info("password matches the criteria");
            return true;
        }
        LOGGER.info("password doesn't matches the criteria");
        throw new InvalidParameterException("Password (at least four characters, at least one upper case character, at least one number) is invalid");
    }
}
