package com.edumaster.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Authentication Entry Point
 * 
 * This class handles authentication failures and unauthorized access attempts.
 * When a user tries to access a secured endpoint without proper authentication,
 * this entry point returns a structured JSON error response.
 * 
 * Key Features:
 * - Returns JSON error responses instead of redirect to login page
 * - Provides detailed error information for debugging
 * - Maintains consistent error response format
 * - Logs authentication failures for security monitoring
 * 
 * @author EduMaster Team
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        logger.error("Responding with unauthorized error. Message - {}", authException.getMessage());
        logger.error("Request URI: {}", request.getRequestURI());
        logger.error("Request Method: {}", request.getMethod());
        
        // Set response content type and status
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        // Create error response body
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());
        body.put("timestamp", System.currentTimeMillis());
        
        // Additional context for better debugging
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            if (authHeader.startsWith("Bearer ")) {
                body.put("tokenPresent", true);
                body.put("tokenValid", false);
            } else {
                body.put("tokenPresent", true);
                body.put("tokenFormat", "Invalid format - missing 'Bearer ' prefix");
            }
        } else {
            body.put("tokenPresent", false);
            body.put("hint", "Include 'Authorization: Bearer <token>' header");
        }
        
        // Convert response to JSON and write to response
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}