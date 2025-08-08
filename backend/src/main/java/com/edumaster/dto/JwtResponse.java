package com.edumaster.dto;

import com.edumaster.model.Role;

/**
 * JWT Response DTO
 * 
 * Data Transfer Object for authentication responses.
 * Contains JWT token and user information sent to client after successful authentication.
 * 
 * @author EduMaster Team
 */
public class JwtResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private Boolean isVerified;
    private Boolean isActive;
    private String profilePictureUrl;

    // Constructors
    public JwtResponse() {}

    public JwtResponse(String accessToken, String refreshToken, Long id, String email, 
                      String firstName, String lastName, Role role, Boolean isVerified, 
                      Boolean isActive, String profilePictureUrl) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.isVerified = isVerified;
        this.isActive = isActive;
        this.profilePictureUrl = profilePictureUrl;
    }

    // Convenience constructor without refresh token
    public JwtResponse(String accessToken, Long id, String email, String firstName, 
                      String lastName, Role role, Boolean isVerified, Boolean isActive) {
        this.accessToken = accessToken;
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.isVerified = isVerified;
        this.isActive = isActive;
    }

    // Business Methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isInstructor() {
        return role == Role.INSTRUCTOR || role == Role.ADMIN;
    }

    public boolean isStudent() {
        return role == Role.STUDENT || role == Role.INSTRUCTOR || role == Role.ADMIN;
    }

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "tokenType='" + tokenType + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                ", isVerified=" + isVerified +
                ", isActive=" + isActive +
                '}';
    }
}