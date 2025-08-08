package com.edumaster.model;

/**
 * Course Status Enumeration
 * 
 * Represents the different states a course can be in:
 * - DRAFT: Course is being created/edited by instructor
 * - PENDING_APPROVAL: Course submitted for admin approval
 * - PUBLISHED: Course is live and available for enrollment
 * - SUSPENDED: Course temporarily unavailable (admin action)
 * 
 * @author EduMaster Team
 */
public enum CourseStatus {
    DRAFT("Draft"),
    PENDING_APPROVAL("Pending Approval"),
    PUBLISHED("Published"),
    SUSPENDED("Suspended");

    private final String displayName;

    CourseStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Check if course is available for enrollment
     */
    public boolean isAvailableForEnrollment() {
        return this == PUBLISHED;
    }

    /**
     * Check if course can be edited by instructor
     */
    public boolean canBeEdited() {
        return this == DRAFT;
    }

    /**
     * Check if course is visible to public
     */
    public boolean isPubliclyVisible() {
        return this == PUBLISHED;
    }

    /**
     * Get status from string (case-insensitive)
     */
    public static CourseStatus fromString(String statusString) {
        if (statusString == null) {
            return DRAFT; // Default status
        }
        
        try {
            return CourseStatus.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DRAFT; // Default status if invalid
        }
    }
}