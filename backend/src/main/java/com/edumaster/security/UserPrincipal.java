package com.edumaster.security;

import com.edumaster.model.Role;
import com.edumaster.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * UserPrincipal Implementation
 * 
 * This class adapts our User entity to Spring Security's UserDetails interface.
 * It provides the bridge between our domain model and Spring Security's authentication.
 * 
 * Key Features:
 * - Implements Spring Security UserDetails interface
 * - Provides role-based authorities
 * - Handles account status checks
 * - Maintains user ID for easy access
 * 
 * @author EduMaster Team
 */
public class UserPrincipal implements UserDetails {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String username; // This will be email
    private String email;
    
    @JsonIgnore
    private String password;
    
    private Role role;
    private Boolean isVerified;
    private Boolean isActive;
    private String profilePictureUrl;
    
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String firstName, String lastName, String username, String email, 
                        String password, Role role, Boolean isVerified, Boolean isActive,
                        String profilePictureUrl, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isVerified = isVerified;
        this.isActive = isActive;
        this.profilePictureUrl = profilePictureUrl;
        this.authorities = authorities;
    }

    /**
     * Create UserPrincipal from User entity
     * 
     * @param user User entity
     * @return UserPrincipal instance
     */
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return new UserPrincipal(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(), // Email is used as username
            user.getEmail(),
            user.getPassword(),
            user.getRole(),
            user.getIsVerified(),
            user.getIsActive(),
            user.getProfilePictureUrl(),
            authorities
        );
    }

    // UserDetails interface methods
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // We don't implement account expiration
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive; // Account is locked if not active
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // We don't implement credential expiration
    }

    @Override
    public boolean isEnabled() {
        return isActive && isVerified; // User must be active and verified
    }

    // Getters for additional properties
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    /**
     * Check if user has specific role
     * 
     * @param role Role to check
     * @return true if user has the role
     */
    public boolean hasRole(Role role) {
        return this.role == role;
    }

    /**
     * Check if user is admin
     * 
     * @return true if user is admin
     */
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    /**
     * Check if user is instructor
     * 
     * @return true if user is instructor
     */
    public boolean isInstructor() {
        return role == Role.INSTRUCTOR || role == Role.ADMIN;
    }

    /**
     * Check if user is student
     * 
     * @return true if user is student
     */
    public boolean isStudent() {
        return role == Role.STUDENT || role == Role.INSTRUCTOR || role == Role.ADMIN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", isVerified=" + isVerified +
                ", isActive=" + isActive +
                '}';
    }
}