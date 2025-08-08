package com.edumaster.model;

/**
 * User Role Enumeration
 * 
 * Defines the different types of users in the system:
 * - STUDENT: Can enroll in courses and track progress
 * - INSTRUCTOR: Can create and manage courses
 * - ADMIN: Has full system access and management capabilities
 * 
 * @author EduMaster Team
 */
public enum Role {
    STUDENT("Student"),
    INSTRUCTOR("Instructor"), 
    ADMIN("Administrator");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get role by name (case-insensitive)
     */
    public static Role fromString(String roleString) {
        if (roleString == null) {
            return STUDENT; // Default role
        }
        
        try {
            return Role.valueOf(roleString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return STUDENT; // Default role if invalid
        }
    }

    /**
     * Check if the role has admin privileges
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * Check if the role has instructor privileges
     */
    public boolean isInstructor() {
        return this == INSTRUCTOR || this == ADMIN;
    }

    /**
     * Check if the role has student privileges
     */
    public boolean isStudent() {
        return this == STUDENT || this == INSTRUCTOR || this == ADMIN;
    }
}