package com.edumaster.dto;

import com.edumaster.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Admin User Request DTO
 * 
 * Data Transfer Object for admin operations on user accounts.
 * Allows admins to modify user roles, status, and other admin-level properties.
 * 
 * @author EduMaster Team
 */
public class AdminUserRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotNull(message = "Role is required")
    private Role role;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @NotNull(message = "Verification status is required")
    private Boolean isVerified;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    private String profilePictureUrl;

    // Admin-specific fields
    private String adminNotes; // Internal notes for admin use

    // Constructors
    public AdminUserRequest() {}

    public AdminUserRequest(String firstName, String lastName, String email, Role role, 
                           Boolean isActive, Boolean isVerified) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.isVerified = isVerified;
    }

    // Business Methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isRoleChangeValid() {
        return role != null && (role == Role.STUDENT || role == Role.INSTRUCTOR || role == Role.ADMIN);
    }

    public boolean isStatusChangeValid() {
        return isActive != null && isVerified != null;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
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

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    @Override
    public String toString() {
        return "AdminUserRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                ", isVerified=" + isVerified +
                ", phone='" + phone + '\'' +
                '}';
    }
}