package com.edumaster.model;

/**
 * Course Level Enumeration
 * 
 * Indicates the difficulty level of a course:
 * - BEGINNER: For users with little to no experience
 * - INTERMEDIATE: For users with some basic knowledge
 * - ADVANCED: For experienced users seeking specialized knowledge
 * 
 * @author EduMaster Team
 */
public enum CourseLevel {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    private final String displayName;

    CourseLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get level from string (case-insensitive)
     */
    public static CourseLevel fromString(String levelString) {
        if (levelString == null) {
            return BEGINNER; // Default level
        }
        
        try {
            return CourseLevel.valueOf(levelString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BEGINNER; // Default level if invalid
        }
    }

    /**
     * Get order for sorting (lower numbers = easier levels)
     */
    public int getOrder() {
        return switch (this) {
            case BEGINNER -> 1;
            case INTERMEDIATE -> 2;
            case ADVANCED -> 3;
        };
    }
}