package com.test.registerservice.exception;

public class ResourceNotFoundException extends Exception {

    private static final long serialVersionUID = -532981190371325529L;
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public ResourceNotFoundException(String errorMessage, Exception exception) {
        super(errorMessage, exception);
    }
}
