package com.edumaster.service;

import com.edumaster.dto.JwtResponse;
import com.edumaster.dto.LoginRequest;
import com.edumaster.dto.RegisterRequest;
import com.edumaster.exception.UserAlreadyExistsException;
import com.edumaster.exception.InvalidCredentialsException;
import com.edumaster.model.Role;
import com.edumaster.model.User;
import com.edumaster.repository.UserRepository;
import com.edumaster.security.UserPrincipal;
import com.edumaster.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication Service
 * 
 * Handles user authentication, registration, and JWT token management.
 * This service contains the core business logic for user authentication.
 * 
 * Key Features:
 * - User registration with validation
 * - User authentication with JWT token generation
 * - Password encryption using BCrypt
 * - Email uniqueness validation
 * - Role assignment during registration
 * 
 * @author EduMaster Team
 */
@Service
@Transactional
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Register a new user
     * 
     * @param registerRequest Registration details
     * @return JwtResponse with user details and token
     * @throws UserAlreadyExistsException if email already exists
     * @throws IllegalArgumentException if passwords don't match
     */
    public JwtResponse registerUser(RegisterRequest registerRequest) {
        logger.info("Attempting to register user with email: {}", registerRequest.getEmail());

        // Validate password confirmation
        if (!registerRequest.isPasswordMatching()) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + registerRequest.getEmail());
        }

        // Create new user
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : Role.STUDENT);
        user.setPhone(registerRequest.getPhone());
        user.setBio(registerRequest.getBio());
        user.setIsVerified(false); // Email verification required
        user.setIsActive(true);

        // Save user to database
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: {}", savedUser.getId());

        // Create UserPrincipal for JWT generation
        UserPrincipal userPrincipal = UserPrincipal.create(savedUser);

        // Generate JWT tokens
        String accessToken = jwtUtil.generateToken(userPrincipal);
        String refreshToken = jwtUtil.generateRefreshToken(userPrincipal);

        // Create response
        JwtResponse response = new JwtResponse(
            accessToken,
            refreshToken,
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getFirstName(),
            savedUser.getLastName(),
            savedUser.getRole(),
            savedUser.getIsVerified(),
            savedUser.getIsActive(),
            savedUser.getProfilePictureUrl()
        );

        logger.info("Registration successful for user: {}", savedUser.getEmail());
        return response;
    }

    /**
     * Authenticate user and generate JWT token
     * 
     * @param loginRequest Login credentials
     * @return JwtResponse with user details and token
     * @throws InvalidCredentialsException if authentication fails
     */
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        logger.info("Attempting to authenticate user with email: {}", loginRequest.getEmail());

        try {
            // Authenticate user credentials
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            // Get authenticated user details
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            // Generate JWT tokens
            String accessToken = jwtUtil.generateToken(userPrincipal);
            String refreshToken = jwtUtil.generateRefreshToken(userPrincipal);

            // Create response
            JwtResponse response = new JwtResponse(
                accessToken,
                refreshToken,
                userPrincipal.getId(),
                userPrincipal.getEmail(),
                userPrincipal.getFirstName(),
                userPrincipal.getLastName(),
                userPrincipal.getRole(),
                userPrincipal.getIsVerified(),
                userPrincipal.getIsActive(),
                userPrincipal.getProfilePictureUrl()
            );

            logger.info("Authentication successful for user: {}", userPrincipal.getEmail());
            return response;

        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}", loginRequest.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    /**
     * Refresh JWT token
     * 
     * @param refreshToken Refresh token
     * @return New JWT access token
     * @throws IllegalArgumentException if refresh token is invalid
     */
    public JwtResponse refreshToken(String refreshToken) {
        logger.info("Attempting to refresh JWT token");

        try {
            // Validate refresh token
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new IllegalArgumentException("Invalid refresh token");
            }

            // Extract username from refresh token
            String username = jwtUtil.extractUsername(refreshToken);

            // Load user details
            User user = userRepository.findByEmailAndIsActive(username, true)
                .orElseThrow(() -> new IllegalArgumentException("User not found or inactive"));

            UserPrincipal userPrincipal = UserPrincipal.create(user);

            // Generate new tokens
            String newAccessToken = jwtUtil.generateToken(userPrincipal);
            String newRefreshToken = jwtUtil.generateRefreshToken(userPrincipal);

            JwtResponse response = new JwtResponse(
                newAccessToken,
                newRefreshToken,
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getIsVerified(),
                user.getIsActive(),
                user.getProfilePictureUrl()
            );

            logger.info("Token refresh successful for user: {}", user.getEmail());
            return response;

        } catch (Exception e) {
            logger.error("Token refresh failed: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }

    /**
     * Check if email is available for registration
     * 
     * @param email Email to check
     * @return true if email is available
     */
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    /**
     * Get user by email
     * 
     * @param email User email
     * @return User entity
     */
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}