package com.edumaster.exception;

/**
 * User Already Exists Exception
 * 
 * Custom exception thrown when attempting to register a user with an email
 * that already exists in the system.
 * 
 * @author EduMaster Team
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}