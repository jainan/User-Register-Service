package com.test.registerservice.exception;

public class InvalidParameterException extends Exception {

    private static final long serialVersionUID = -532981190371325529L;
    public InvalidParameterException() {
        super();
    }

    public InvalidParameterException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidParameterException(String errorMessage, Exception exception) {
        super(errorMessage, exception);
    }
}
