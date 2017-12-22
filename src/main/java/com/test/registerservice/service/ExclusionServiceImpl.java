package com.test.registerservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.test.registerservice.exception.InvalidParameterException;

@Service
public class ExclusionServiceImpl implements ExclusionService {

    private static final List<String> BLACKLISTED_SSN = new ArrayList<>();
    private static final List<String> BLACKLISTED_DOB = new ArrayList<>();
    
    public ExclusionServiceImpl() {

        BLACKLISTED_SSN.add("SSN10");
        BLACKLISTED_SSN.add("SSN20");
        BLACKLISTED_SSN.add("SSN30");
        BLACKLISTED_SSN.add("SSN40");
        
        BLACKLISTED_DOB.add("01-01-2000");
        BLACKLISTED_DOB.add("02-02-2000");
        BLACKLISTED_DOB.add("03-03-2000");
        BLACKLISTED_DOB.add("04-04-2000");
    }
    
    @Override
    public boolean validate(String dob, String ssn) throws InvalidParameterException {
        if((BLACKLISTED_DOB.contains(dob) || BLACKLISTED_SSN.contains(ssn)))
            throw new InvalidParameterException("User is black listed.");
        return true;
    }
}
