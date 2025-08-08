package com.edumaster.exception;

/**
 * Resource Not Found Exception
 * 
 * Custom exception thrown when a requested resource cannot be found.
 * Typically used for database entities that don't exist.
 * 
 * @author EduMaster Team
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}