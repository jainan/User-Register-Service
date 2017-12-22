package com.test.registerservice.service;

import com.test.registerservice.exception.InvalidParameterException;

/**
 * Service to offer validation of a user against a 'blacklist'. Blacklisted users fail the validation.
 * @author JainAn
 *
 */
public interface ExclusionService {

    /**
     * 
     * @param dob the user's date of birth in ISO 8601 format
     * @param ssn the user's social security number (United States)
     * @return true if the user could be validated and is not blacklisted, false if the user is blacklisted and therefore
     *         failed validation
     */
    boolean validate(final String dob, final String ssn) throws InvalidParameterException;
}
