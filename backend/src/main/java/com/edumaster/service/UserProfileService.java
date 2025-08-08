package com.edumaster.service;

import com.edumaster.dto.ChangePasswordRequest;
import com.edumaster.dto.UpdateProfileRequest;
import com.edumaster.dto.UserProfileResponse;
import com.edumaster.exception.ResourceNotFoundException;
import com.edumaster.model.User;
import com.edumaster.repository.UserRepository;
import com.edumaster.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * User Profile Service
 * 
 * Handles user profile management operations including profile updates,
 * password changes, and profile information retrieval.
 * 
 * Key Features:
 * - User profile CRUD operations
 * - Secure password change functionality
 * - Profile picture management
 * - User statistics calculation
 * - Role-based access control
 * 
 * @author EduMaster Team
 */
@Service
@Transactional
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get current user's profile information
     * 
     * @return UserProfileResponse with complete profile data
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getCurrentUserProfile() {
        UserPrincipal currentUser = getCurrentUser();
        logger.info("Fetching profile for user: {}", currentUser.getEmail());

        User user = userRepository.findById(currentUser.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + currentUser.getId()));

        UserProfileResponse response = new UserProfileResponse(user);
        
        // Add statistics based on user role
        populateUserStatistics(response, user);
        
        return response;
    }

    /**
     * Get user profile by ID (for admins or self-access)
     * 
     * @param userId User ID to fetch
     * @return UserProfileResponse
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public UserProfileResponse getUserProfile(Long userId) {
        logger.info("Fetching profile for user ID: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        UserProfileResponse response = new UserProfileResponse(user);
        populateUserStatistics(response, user);
        
        return response;
    }

    /**
     * Update current user's profile information
     * 
     * @param updateRequest Profile update data
     * @return Updated UserProfileResponse
     */
    public UserProfileResponse updateCurrentUserProfile(UpdateProfileRequest updateRequest) {
        UserPrincipal currentUser = getCurrentUser();
        logger.info("Updating profile for user: {}", currentUser.getEmail());

        if (!updateRequest.hasUpdates()) {
            throw new IllegalArgumentException("No updates provided");
        }

        User user = userRepository.findById(currentUser.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + currentUser.getId()));

        // Update only provided fields
        if (StringUtils.hasText(updateRequest.getFirstName())) {
            user.setFirstName(updateRequest.getFirstName());
        }
        
        if (StringUtils.hasText(updateRequest.getLastName())) {
            user.setLastName(updateRequest.getLastName());
        }
        
        if (StringUtils.hasText(updateRequest.getPhone())) {
            user.setPhone(updateRequest.getPhone());
        }
        
        if (updateRequest.getBio() != null) { // Allow setting bio to empty string
            user.setBio(updateRequest.getBio());
        }
        
        if (StringUtils.hasText(updateRequest.getProfilePictureUrl())) {
            user.setProfilePictureUrl(updateRequest.getProfilePictureUrl());
        }

        User updatedUser = userRepository.save(user);
        logger.info("Profile updated successfully for user: {}", updatedUser.getEmail());

        UserProfileResponse response = new UserProfileResponse(updatedUser);
        populateUserStatistics(response, updatedUser);
        
        return response;
    }

    /**
     * Change current user's password
     * 
     * @param changePasswordRequest Password change data
     * @return Success message
     */
    public String changePassword(ChangePasswordRequest changePasswordRequest) {
        UserPrincipal currentUser = getCurrentUser();
        logger.info("Password change request for user: {}", currentUser.getEmail());

        if (!changePasswordRequest.isPasswordMatching()) {
            throw new IllegalArgumentException("New passwords do not match");
        }

        if (!changePasswordRequest.isDifferentFromCurrent()) {
            throw new IllegalArgumentException("New password must be different from current password");
        }

        User user = userRepository.findById(currentUser.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + currentUser.getId()));

        // Verify current password
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        logger.info("Password changed successfully for user: {}", user.getEmail());
        return "Password changed successfully";
    }

    /**
     * Delete current user's account (soft delete)
     * 
     * @return Success message
     */
    public String deleteCurrentUserAccount() {
        UserPrincipal currentUser = getCurrentUser();
        logger.info("Account deletion request for user: {}", currentUser.getEmail());

        User user = userRepository.findById(currentUser.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + currentUser.getId()));

        // Soft delete by setting inactive
        user.setIsActive(false);
        userRepository.save(user);

        logger.info("Account deactivated successfully for user: {}", user.getEmail());
        return "Account deactivated successfully";
    }

    /**
     * Reactivate user account (for previously soft-deleted accounts)
     * 
     * @param email User email
     * @return Success message
     */
    @PreAuthorize("hasRole('ADMIN')")
    public String reactivateUserAccount(String email) {
        logger.info("Account reactivation request for email: {}", email);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        user.setIsActive(true);
        userRepository.save(user);

        logger.info("Account reactivated successfully for user: {}", user.getEmail());
        return "Account reactivated successfully";
    }

    /**
     * Get user statistics for dashboard
     * 
     * @param userId User ID
     * @return Statistics map
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getUserDashboardStats(Long userId) {
        logger.info("Fetching dashboard statistics for user ID: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        UserProfileResponse response = new UserProfileResponse(user);
        populateUserStatistics(response, user);
        
        return response;
    }

    /**
     * Search users by name or email (for admins)
     * 
     * @param query Search query
     * @param pageable Pagination parameters
     * @return Page of UserProfileResponse
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserProfileResponse> searchUsers(String query, Pageable pageable) {
        logger.info("Searching users with query: {}", query);

        Page<User> users;
        
        if (StringUtils.hasText(query)) {
            // Search by first name, last name, or email
            users = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                query, query, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.map(user -> {
            UserProfileResponse response = new UserProfileResponse(user);
            populateUserStatistics(response, user);
            return response;
        });
    }

    /**
     * Get users by role with pagination
     * 
     * @param role User role
     * @param pageable Pagination parameters
     * @return Page of UserProfileResponse
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserProfileResponse> getUsersByRole(com.edumaster.model.Role role, Pageable pageable) {
        logger.info("Fetching users with role: {}", role);

        Page<User> users = userRepository.findByRole(role, pageable);
        
        return users.map(user -> {
            UserProfileResponse response = new UserProfileResponse(user);
            populateUserStatistics(response, user);
            return response;
        });
    }

    /**
     * Populate user statistics based on their role
     * 
     * @param response UserProfileResponse to populate
     * @param user User entity
     */
    private void populateUserStatistics(UserProfileResponse response, User user) {
        switch (user.getRole()) {
            case INSTRUCTOR:
                // For instructors: total courses, total students, total earnings
                if (user.getInstructedCourses() != null) {
                    response.setTotalCourses(user.getInstructedCourses().size());
                    
                    // Calculate total students from all courses
                    int totalStudents = user.getInstructedCourses().stream()
                        .mapToInt(course -> course.getEnrollments() != null ? course.getEnrollments().size() : 0)
                        .sum();
                    response.setTotalStudents(totalStudents);
                    
                    // TODO: Calculate total earnings from payments
                    response.setTotalEarnings(0.0);
                }
                break;
                
            case STUDENT:
                // For students: total enrollments
                if (user.getEnrollments() != null) {
                    response.setTotalEnrollments(user.getEnrollments().size());
                }
                break;
                
            case ADMIN:
                // For admins: system-wide statistics will be handled separately
                break;
        }
    }

    /**
     * Get current authenticated user
     * 
     * @return UserPrincipal
     */
    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new IllegalStateException("User not authenticated");
        }
        
        return (UserPrincipal) authentication.getPrincipal();
    }
}