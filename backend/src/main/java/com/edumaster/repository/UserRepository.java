package com.edumaster.repository;

import com.edumaster.model.Role;
import com.edumaster.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Repository Interface
 * 
 * This repository provides data access methods for User entity.
 * It extends JpaRepository to get basic CRUD operations and adds
 * custom query methods for specific business requirements.
 * 
 * Key Features:
 * - Basic CRUD operations (inherited from JpaRepository)
 * - Custom finder methods for business logic
 * - Email-based user lookup (primary authentication method)
 * - Role-based user queries
 * - Account status management
 * - Bulk operations for admin functions
 * 
 * @author EduMaster Team
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email address
     * Primary method for user authentication
     * 
     * @param email User's email address
     * @return Optional<User>
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by email address and active status
     * Useful for authentication when we only want active users
     * 
     * @param email User's email address
     * @param isActive Account status
     * @return Optional<User>
     */
    Optional<User> findByEmailAndIsActive(String email, Boolean isActive);

    /**
     * Check if user exists by email
     * Useful for registration validation
     * 
     * @param email User's email address
     * @return true if user exists
     */
    Boolean existsByEmail(String email);

    /**
     * Find users by role
     * Useful for admin functions and role-based operations
     * 
     * @param role User role
     * @return List of users with the specified role
     */
    List<User> findByRole(Role role);

    /**
     * Find users by role with pagination
     * 
     * @param role User role
     * @param pageable Pagination parameters
     * @return Page of users with the specified role
     */
    Page<User> findByRole(Role role, Pageable pageable);

    /**
     * Find active users by role
     * 
     * @param role User role
     * @param isActive Account status
     * @return List of active users with the specified role
     */
    List<User> findByRoleAndIsActive(Role role, Boolean isActive);

    /**
     * Find users by verification status
     * Useful for email verification management
     * 
     * @param isVerified Verification status
     * @return List of users with the specified verification status
     */
    List<User> findByIsVerified(Boolean isVerified);

    /**
     * Find users by active status
     * 
     * @param isActive Account status
     * @return List of users with the specified active status
     */
    List<User> findByIsActive(Boolean isActive);

    /**
     * Find users by active status with pagination
     * 
     * @param isActive Account status
     * @param pageable Pagination parameters
     * @return Page of users with the specified active status
     */
    Page<User> findByIsActive(Boolean isActive, Pageable pageable);

    /**
     * Search users by first name or last name (case-insensitive)
     * 
     * @param firstName First name to search
     * @param lastName Last name to search
     * @param pageable Pagination parameters
     * @return Page of matching users
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        @Param("firstName") String firstName, 
        @Param("lastName") String lastName, 
        Pageable pageable);

    /**
     * Search users by email (case-insensitive)
     * 
     * @param email Email to search
     * @param pageable Pagination parameters
     * @return Page of matching users
     */
    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    /**
     * Count users by role
     * 
     * @param role User role
     * @return Number of users with the specified role
     */
    Long countByRole(Role role);

    /**
     * Count active users
     * 
     * @param isActive Account status
     * @return Number of users with the specified active status
     */
    Long countByIsActive(Boolean isActive);

    /**
     * Count verified users
     * 
     * @param isVerified Verification status
     * @return Number of users with the specified verification status
     */
    Long countByIsVerified(Boolean isVerified);

    /**
     * Update user verification status
     * 
     * @param userId User ID
     * @param isVerified New verification status
     * @return Number of updated records
     */
    @Modifying
    @Query("UPDATE User u SET u.isVerified = :isVerified WHERE u.id = :userId")
    int updateUserVerificationStatus(@Param("userId") Long userId, @Param("isVerified") Boolean isVerified);

    /**
     * Update user active status
     * 
     * @param userId User ID
     * @param isActive New active status
     * @return Number of updated records
     */
    @Modifying
    @Query("UPDATE User u SET u.isActive = :isActive WHERE u.id = :userId")
    int updateUserActiveStatus(@Param("userId") Long userId, @Param("isActive") Boolean isActive);

    /**
     * Find instructors (users with INSTRUCTOR or ADMIN role)
     * 
     * @param pageable Pagination parameters
     * @return Page of instructors
     */
    @Query("SELECT u FROM User u WHERE u.role IN (com.edumaster.model.Role.INSTRUCTOR, com.edumaster.model.Role.ADMIN) AND u.isActive = true")
    Page<User> findInstructors(Pageable pageable);

    /**
     * Find students (users with STUDENT role)
     * 
     * @param pageable Pagination parameters
     * @return Page of students
     */
    @Query("SELECT u FROM User u WHERE u.role = com.edumaster.model.Role.STUDENT AND u.isActive = true")
    Page<User> findStudents(Pageable pageable);

    /**
     * Get user statistics
     * Custom query to get counts by role and status
     */
    @Query("SELECT " +
           "COUNT(CASE WHEN u.role = com.edumaster.model.Role.STUDENT THEN 1 END) as students, " +
           "COUNT(CASE WHEN u.role = com.edumaster.model.Role.INSTRUCTOR THEN 1 END) as instructors, " +
           "COUNT(CASE WHEN u.role = com.edumaster.model.Role.ADMIN THEN 1 END) as admins, " +
           "COUNT(CASE WHEN u.isActive = true THEN 1 END) as activeUsers, " +
           "COUNT(CASE WHEN u.isVerified = true THEN 1 END) as verifiedUsers " +
           "FROM User u")
    Object[] getUserStatistics();
}