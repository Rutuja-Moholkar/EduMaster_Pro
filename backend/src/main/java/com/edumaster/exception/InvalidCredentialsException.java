package com.edumaster.exception;

/**
 * Invalid Credentials Exception
 * 
 * Custom exception thrown when user authentication fails due to
 * incorrect email or password.
 * 
 * @author EduMaster Team
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}