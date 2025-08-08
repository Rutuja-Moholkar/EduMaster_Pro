package com.edumaster.dto;

import jakarta.validation.constraints.Size;

/**
 * Update Profile Request DTO
 * 
 * Data Transfer Object for updating user profile information.
 * Contains only the fields that users can modify about themselves.
 * 
 * @author EduMaster Team
 */
public class UpdateProfileRequest {

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    private String profilePictureUrl;

    // Constructors
    public UpdateProfileRequest() {}

    public UpdateProfileRequest(String firstName, String lastName, String phone, String bio) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.bio = bio;
    }

    // Business Methods
    public String getFullName() {
        return firstName != null && lastName != null ? firstName + " " + lastName : null;
    }

    public boolean hasUpdates() {
        return firstName != null || lastName != null || phone != null || 
               bio != null || profilePictureUrl != null;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @Override
    public String toString() {
        return "UpdateProfileRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", bio='" + bio + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                '}';
    }
}