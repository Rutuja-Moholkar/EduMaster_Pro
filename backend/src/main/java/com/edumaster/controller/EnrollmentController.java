package com.edumaster.controller;

import com.edumaster.dto.ApiResponse;
import com.edumaster.model.Enrollment;
import com.edumaster.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/enrollments")
@Tag(name = "Enrollment Management", description = "Enrollment management API endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EnrollmentController {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private EnrollmentService enrollmentService;

    // Enrollment Operations

    @PostMapping("/free/{courseId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Enroll in free course", description = "Enroll user in a free course")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Enrollment successful"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request or already enrolled"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ApiResponse<Enrollment>> enrollInFreeCourse(
            @Parameter(description = "Course ID") @PathVariable Long courseId,
            @Parameter(description = "User ID") @RequestParam Long userId) {

        logger.info("Enrolling user {} in free course {}", userId, courseId);

        try {
            Enrollment enrollment = enrollmentService.enrollUserInFreeCourse(userId, courseId);

            ApiResponse<Enrollment> response = ApiResponse.success(
                "Successfully enrolled in course", enrollment);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error enrolling user {} in course {}: {}", userId, courseId, e.getMessage());
            
            HttpStatus status = e.getMessage().contains("not found") ? 
                               HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            
            ApiResponse<Enrollment> errorResponse = ApiResponse.error(
                "Enrollment failed", e.getMessage());
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    // Read Operations

    @GetMapping("/{enrollmentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get enrollment by ID", description = "Get enrollment details by ID")
    public ResponseEntity<ApiResponse<Enrollment>> getEnrollmentById(
            @Parameter(description = "Enrollment ID") @PathVariable Long enrollmentId) {

        logger.info("Fetching enrollment with ID: {}", enrollmentId);

        try {
            Enrollment enrollment = enrollmentService.getEnrollmentByIdOrThrow(enrollmentId);

            ApiResponse<Enrollment> response = ApiResponse.success(
                "Enrollment retrieved successfully", enrollment);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching enrollment with ID {}: {}", enrollmentId, e.getMessage());
            ApiResponse<Enrollment> errorResponse = ApiResponse.error(
                "Enrollment not found", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get user enrollments", description = "Get all enrollments for a user")
    public ResponseEntity<ApiResponse<Page<Enrollment>>> getUserEnrollments(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "enrollmentDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.info("Fetching enrollments for user {} with pagination", userId);

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                       Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Enrollment> enrollments = enrollmentService.getEnrollmentsByUser(userId, pageable);

            ApiResponse<Page<Enrollment>> response = ApiResponse.success(
                "User enrollments retrieved successfully", enrollments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching enrollments for user {}: {}", userId, e.getMessage());
            ApiResponse<Page<Enrollment>> errorResponse = ApiResponse.error(
                "Failed to fetch user enrollments", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get course enrollments", description = "Get all enrollments for a course (Instructor/Admin only)")
    public ResponseEntity<ApiResponse<Page<Enrollment>>> getCourseEnrollments(
            @Parameter(description = "Course ID") @PathVariable Long courseId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        logger.info("Fetching enrollments for course {} with pagination", courseId);

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("enrollmentDate").descending());
            Page<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId, pageable);

            ApiResponse<Page<Enrollment>> response = ApiResponse.success(
                "Course enrollments retrieved successfully", enrollments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching enrollments for course {}: {}", courseId, e.getMessage());
            ApiResponse<Page<Enrollment>> errorResponse = ApiResponse.error(
                "Failed to fetch course enrollments", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}/active")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get active learning courses", description = "Get courses user is currently learning")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getActiveLearning(
            @Parameter(description = "User ID") @PathVariable Long userId) {

        logger.info("Fetching active learning courses for user {}", userId);

        try {
            List<Enrollment> activeEnrollments = enrollmentService.getActiveLearningByUser(userId);

            ApiResponse<List<Enrollment>> response = ApiResponse.success(
                "Active learning courses retrieved successfully", activeEnrollments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching active learning for user {}: {}", userId, e.getMessage());
            ApiResponse<List<Enrollment>> errorResponse = ApiResponse.error(
                "Failed to fetch active learning courses", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}/completed")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get completed courses", description = "Get courses user has completed")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getCompletedCourses(
            @Parameter(description = "User ID") @PathVariable Long userId) {

        logger.info("Fetching completed courses for user {}", userId);

        try {
            List<Enrollment> completedEnrollments = enrollmentService.getCompletedCoursesByUser(userId);

            ApiResponse<List<Enrollment>> response = ApiResponse.success(
                "Completed courses retrieved successfully", completedEnrollments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching completed courses for user {}: {}", userId, e.getMessage());
            ApiResponse<List<Enrollment>> errorResponse = ApiResponse.error(
                "Failed to fetch completed courses", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}/dashboard")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get dashboard enrollments", description = "Get enrollments for user dashboard")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getDashboardEnrollments(
            @Parameter(description = "User ID") @PathVariable Long userId) {

        logger.info("Fetching dashboard enrollments for user {}", userId);

        try {
            List<Enrollment> dashboardEnrollments = enrollmentService.getUserDashboardEnrollments(userId);

            ApiResponse<List<Enrollment>> response = ApiResponse.success(
                "Dashboard enrollments retrieved successfully", dashboardEnrollments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching dashboard enrollments for user {}: {}", userId, e.getMessage());
            ApiResponse<List<Enrollment>> errorResponse = ApiResponse.error(
                "Failed to fetch dashboard enrollments", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Progress Management

    @PutMapping("/{enrollmentId}/progress")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Update enrollment progress", description = "Update progress percentage for an enrollment")
    public ResponseEntity<ApiResponse<Enrollment>> updateProgress(
            @Parameter(description = "Enrollment ID") @PathVariable Long enrollmentId,
            @Parameter(description = "Progress percentage (0-100)") @RequestParam BigDecimal progressPercentage) {

        logger.info("Updating progress for enrollment {} to {}%", enrollmentId, progressPercentage);

        try {
            // Validate progress percentage
            if (progressPercentage.compareTo(BigDecimal.ZERO) < 0 || 
                progressPercentage.compareTo(new BigDecimal("100")) > 0) {
                throw new IllegalArgumentException("Progress percentage must be between 0 and 100");
            }

            Enrollment updatedEnrollment = enrollmentService.updateEnrollmentProgress(enrollmentId, progressPercentage);

            ApiResponse<Enrollment> response = ApiResponse.success(
                "Progress updated successfully", updatedEnrollment);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error updating progress for enrollment {}: {}", enrollmentId, e.getMessage());
            ApiResponse<Enrollment> errorResponse = ApiResponse.error(
                "Failed to update progress", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/{enrollmentId}/complete")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Mark course as completed", description = "Mark an enrollment as completed")
    public ResponseEntity<ApiResponse<Enrollment>> markAsCompleted(
            @Parameter(description = "Enrollment ID") @PathVariable Long enrollmentId) {

        logger.info("Marking enrollment {} as completed", enrollmentId);

        try {
            Enrollment completedEnrollment = enrollmentService.markCourseAsCompleted(enrollmentId);

            ApiResponse<Enrollment> response = ApiResponse.success(
                "Course marked as completed", completedEnrollment);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error marking enrollment {} as completed: {}", enrollmentId, e.getMessage());
            ApiResponse<Enrollment> errorResponse = ApiResponse.error(
                "Failed to mark course as completed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Validation Endpoints

    @GetMapping("/check")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Check enrollment status", description = "Check if user is enrolled in a course")
    public ResponseEntity<ApiResponse<Boolean>> checkEnrollmentStatus(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Course ID") @RequestParam Long courseId) {

        logger.info("Checking enrollment status for user {} in course {}", userId, courseId);

        try {
            boolean isEnrolled = enrollmentService.isUserEnrolledInCourse(userId, courseId);

            ApiResponse<Boolean> response = ApiResponse.success(
                "Enrollment status checked", isEnrolled);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error checking enrollment status: {}", e.getMessage());
            ApiResponse<Boolean> errorResponse = ApiResponse.error(
                "Failed to check enrollment status", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/access")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Check course access", description = "Check if user can access course content")
    public ResponseEntity<ApiResponse<Boolean>> checkCourseAccess(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Course ID") @RequestParam Long courseId) {

        logger.info("Checking course access for user {} in course {}", userId, courseId);

        try {
            boolean canAccess = enrollmentService.canUserAccessCourse(userId, courseId);

            ApiResponse<Boolean> response = ApiResponse.success(
                "Course access checked", canAccess);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error checking course access: {}", e.getMessage());
            ApiResponse<Boolean> errorResponse = ApiResponse.error(
                "Failed to check course access", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Statistics Endpoints

    @GetMapping("/stats/user/{userId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get user enrollment statistics", description = "Get enrollment statistics for a user")
    public ResponseEntity<ApiResponse<UserEnrollmentStats>> getUserEnrollmentStats(
            @Parameter(description = "User ID") @PathVariable Long userId) {

        logger.info("Fetching enrollment statistics for user {}", userId);

        try {
            long totalEnrollments = enrollmentService.getTotalEnrollmentsByUser(userId);
            long completedEnrollments = enrollmentService.getCompletedEnrollmentsByUser(userId);
            long inProgressEnrollments = enrollmentService.getInProgressEnrollmentsByUser(userId);
            double learningProgress = enrollmentService.getUserLearningProgress(userId);

            UserEnrollmentStats stats = new UserEnrollmentStats(
                totalEnrollments, completedEnrollments, inProgressEnrollments, learningProgress);

            ApiResponse<UserEnrollmentStats> response = ApiResponse.success(
                "User enrollment statistics retrieved", stats);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching enrollment statistics for user {}: {}", userId, e.getMessage());
            ApiResponse<UserEnrollmentStats> errorResponse = ApiResponse.error(
                "Failed to fetch enrollment statistics", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/stats/course/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get course enrollment statistics", description = "Get enrollment statistics for a course")
    public ResponseEntity<ApiResponse<CourseEnrollmentStats>> getCourseEnrollmentStats(
            @Parameter(description = "Course ID") @PathVariable Long courseId) {

        logger.info("Fetching enrollment statistics for course {}", courseId);

        try {
            long totalEnrollments = enrollmentService.getTotalEnrollmentsByCourse(courseId);
            long completedEnrollments = enrollmentService.getCompletedEnrollmentsByCourse(courseId);
            double completionRate = enrollmentService.getCourseCompletionRate(courseId);

            CourseEnrollmentStats stats = new CourseEnrollmentStats(
                totalEnrollments, completedEnrollments, completionRate);

            ApiResponse<CourseEnrollmentStats> response = ApiResponse.success(
                "Course enrollment statistics retrieved", stats);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching enrollment statistics for course {}: {}", courseId, e.getMessage());
            ApiResponse<CourseEnrollmentStats> errorResponse = ApiResponse.error(
                "Failed to fetch enrollment statistics", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Admin Operations

    @DeleteMapping("/{enrollmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Unenroll user", description = "Remove user enrollment (Admin only)")
    public ResponseEntity<ApiResponse<String>> unenrollUser(
            @Parameter(description = "Enrollment ID") @PathVariable Long enrollmentId,
            @Parameter(description = "Reason for unenrollment") @RequestParam String reason) {

        logger.info("Unenrolling user - enrollment ID: {}", enrollmentId);

        try {
            enrollmentService.unenrollUser(enrollmentId, reason);

            ApiResponse<String> response = ApiResponse.success(
                "User unenrolled successfully", "Enrollment removed");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error unenrolling user - enrollment ID {}: {}", enrollmentId, e.getMessage());
            ApiResponse<String> errorResponse = ApiResponse.error(
                "Failed to unenroll user", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Inner classes for statistics
    public static class UserEnrollmentStats {
        private long totalEnrollments;
        private long completedEnrollments;
        private long inProgressEnrollments;
        private double learningProgress;

        public UserEnrollmentStats(long totalEnrollments, long completedEnrollments, 
                                  long inProgressEnrollments, double learningProgress) {
            this.totalEnrollments = totalEnrollments;
            this.completedEnrollments = completedEnrollments;
            this.inProgressEnrollments = inProgressEnrollments;
            this.learningProgress = learningProgress;
        }

        // Getters
        public long getTotalEnrollments() { return totalEnrollments; }
        public long getCompletedEnrollments() { return completedEnrollments; }
        public long getInProgressEnrollments() { return inProgressEnrollments; }
        public double getLearningProgress() { return learningProgress; }
    }

    public static class CourseEnrollmentStats {
        private long totalEnrollments;
        private long completedEnrollments;
        private double completionRate;

        public CourseEnrollmentStats(long totalEnrollments, long completedEnrollments, double completionRate) {
            this.totalEnrollments = totalEnrollments;
            this.completedEnrollments = completedEnrollments;
            this.completionRate = completionRate;
        }

        // Getters
        public long getTotalEnrollments() { return totalEnrollments; }
        public long getCompletedEnrollments() { return completedEnrollments; }
        public double getCompletionRate() { return completionRate; }
    }
}