package com.edumaster.controller;

import com.edumaster.dto.ApiResponse;
import com.edumaster.dto.JwtResponse;
import com.edumaster.dto.LoginRequest;
import com.edumaster.dto.RegisterRequest;
import com.edumaster.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * 
 * REST API endpoints for user authentication operations.
 * Handles user registration, login, token refresh, and email availability checks.
 * 
 * Base URL: /api/v1/auth
 * 
 * Key Features:
 * - User registration with validation
 * - User authentication with JWT tokens
 * - Token refresh functionality
 * - Email availability checking
 * - Comprehensive error handling
 * - API documentation with OpenAPI
 * 
 * @author EduMaster Team
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * Register a new user
     * 
     * @param registerRequest User registration details
     * @return JWT response with user details and tokens
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Register a new user account with email and password")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<ApiResponse<JwtResponse>> registerUser(
            @Parameter(description = "User registration details") 
            @Valid @RequestBody RegisterRequest registerRequest) {
        
        logger.info("Registration request received for email: {}", registerRequest.getEmail());
        
        try {
            JwtResponse jwtResponse = authService.registerUser(registerRequest);
            
            ApiResponse<JwtResponse> response = ApiResponse.success(
                "User registered successfully. Please verify your email address.", 
                jwtResponse
            );
            
            logger.info("User registered successfully: {}", registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            logger.error("Registration failed for email: {} - Error: {}", registerRequest.getEmail(), e.getMessage());
            
            ApiResponse<JwtResponse> errorResponse = ApiResponse.error(
                "Registration failed", 
                e.getMessage()
            );
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Authenticate user login
     * 
     * @param loginRequest User login credentials
     * @return JWT response with user details and tokens
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with email and password")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<ApiResponse<JwtResponse>> loginUser(
            @Parameter(description = "User login credentials") 
            @Valid @RequestBody LoginRequest loginRequest) {
        
        logger.info("Login request received for email: {}", loginRequest.getEmail());
        
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            
            ApiResponse<JwtResponse> response = ApiResponse.success(
                "Login successful", 
                jwtResponse
            );
            
            logger.info("Login successful for user: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Login failed for email: {} - Error: {}", loginRequest.getEmail(), e.getMessage());
            
            ApiResponse<JwtResponse> errorResponse = ApiResponse.error(
                "Login failed", 
                e.getMessage()
            );
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * Refresh JWT token
     * 
     * @param refreshToken Refresh token from client
     * @return New JWT tokens
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT token", description = "Generate new access token using refresh token")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid refresh token"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Refresh token expired")
    })
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(
            @Parameter(description = "Refresh token") 
            @RequestParam String refreshToken) {
        
        logger.info("Token refresh request received");
        
        try {
            JwtResponse jwtResponse = authService.refreshToken(refreshToken);
            
            ApiResponse<JwtResponse> response = ApiResponse.success(
                "Token refreshed successfully", 
                jwtResponse
            );
            
            logger.info("Token refresh successful");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Token refresh failed: {}", e.getMessage());
            
            ApiResponse<JwtResponse> errorResponse = ApiResponse.error(
                "Token refresh failed", 
                e.getMessage()
            );
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * Check email availability
     * 
     * @param email Email address to check
     * @return Availability status
     */
    @GetMapping("/check-email")
    @Operation(summary = "Check email availability", description = "Check if email is available for registration")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Email availability checked"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid email format")
    })
    public ResponseEntity<ApiResponse<Boolean>> checkEmailAvailability(
            @Parameter(description = "Email address to check") 
            @RequestParam String email) {
        
        logger.info("Email availability check for: {}", email);
        
        try {
            boolean isAvailable = authService.isEmailAvailable(email);
            
            String message = isAvailable ? "Email is available" : "Email is already taken";
            ApiResponse<Boolean> response = ApiResponse.success(message, isAvailable);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Email availability check failed for: {} - Error: {}", email, e.getMessage());
            
            ApiResponse<Boolean> errorResponse = ApiResponse.error(
                "Failed to check email availability", 
                e.getMessage()
            );
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Health check endpoint for authentication service
     * 
     * @return Service status
     */
    @GetMapping("/health")
    @Operation(summary = "Authentication service health check", description = "Check if authentication service is running")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service is healthy")
    })
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        ApiResponse<String> response = ApiResponse.success(
            "Authentication service is running", 
            "OK"
        );
        
        return ResponseEntity.ok(response);
    }
}