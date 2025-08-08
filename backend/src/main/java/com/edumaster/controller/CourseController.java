package com.edumaster.controller;

import com.edumaster.dto.ApiResponse;
import com.edumaster.model.Course;
import com.edumaster.model.CourseLevel;
import com.edumaster.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/courses")
@Tag(name = "Course Management", description = "Course management API endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    // Public endpoints - No authentication required

    @GetMapping("/public")
    @Operation(summary = "Get all published courses", description = "Retrieve all published courses with pagination")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<Course>>> getAllPublishedCourses(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "12") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.info("Fetching published courses - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                       Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Course> courses = courseService.getAllPublishedCourses(pageable);

            ApiResponse<Page<Course>> response = ApiResponse.success(
                "Courses retrieved successfully", courses);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching published courses: {}", e.getMessage());
            ApiResponse<Page<Course>> errorResponse = ApiResponse.error(
                "Failed to fetch courses", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/public/{courseId}")
    @Operation(summary = "Get published course by ID", description = "Retrieve a published course by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Course retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<ApiResponse<Course>> getPublishedCourseById(
            @Parameter(description = "Course ID") @PathVariable Long courseId) {

        logger.info("Fetching published course with ID: {}", courseId);

        try {
            Course course = courseService.getPublishedCourseById(courseId);

            ApiResponse<Course> response = ApiResponse.success(
                "Course retrieved successfully", course);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching course with ID {}: {}", courseId, e.getMessage());
            ApiResponse<Course> errorResponse = ApiResponse.error(
                "Course not found", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/public/search")
    @Operation(summary = "Search published courses", description = "Search published courses by keyword")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<ApiResponse<Page<Course>>> searchCourses(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "12") int size) {

        logger.info("Searching courses with keyword: {}", keyword);

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Course> courses = courseService.searchCourses(keyword, pageable);

            ApiResponse<Page<Course>> response = ApiResponse.success(
                "Search completed successfully", courses);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error searching courses: {}", e.getMessage());
            ApiResponse<Page<Course>> errorResponse = ApiResponse.error(
                "Search failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/public/filter")
    @Operation(summary = "Filter published courses", description = "Filter published courses by various criteria")
    public ResponseEntity<ApiResponse<Page<Course>>> filterCourses(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) CourseLevel level,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        logger.info("Filtering courses with criteria - searchTerm: {}, categoryId: {}, level: {}", 
                   searchTerm, categoryId, level);

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                       Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Course> courses = courseService.getCoursesWithFilters(
                searchTerm, categoryId, level, minPrice, maxPrice, pageable);

            ApiResponse<Page<Course>> response = ApiResponse.success(
                "Courses filtered successfully", courses);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error filtering courses: {}", e.getMessage());
            ApiResponse<Page<Course>> errorResponse = ApiResponse.error(
                "Filter failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/public/popular")
    @Operation(summary = "Get popular courses", description = "Get most popular published courses")
    public ResponseEntity<ApiResponse<List<Course>>> getPopularCourses(
            @RequestParam(defaultValue = "10") int limit) {

        logger.info("Fetching top {} popular courses", limit);

        try {
            List<Course> courses = courseService.getPopularCourses(limit);

            ApiResponse<List<Course>> response = ApiResponse.success(
                "Popular courses retrieved successfully", courses);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching popular courses: {}", e.getMessage());
            ApiResponse<List<Course>> errorResponse = ApiResponse.error(
                "Failed to fetch popular courses", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/public/recent")
    @Operation(summary = "Get recent courses", description = "Get most recently published courses")
    public ResponseEntity<ApiResponse<List<Course>>> getRecentCourses(
            @RequestParam(defaultValue = "10") int limit) {

        logger.info("Fetching {} recent courses", limit);

        try {
            List<Course> courses = courseService.getRecentCourses(limit);

            ApiResponse<List<Course>> response = ApiResponse.success(
                "Recent courses retrieved successfully", courses);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching recent courses: {}", e.getMessage());
            ApiResponse<List<Course>> errorResponse = ApiResponse.error(
                "Failed to fetch recent courses", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/public/free")
    @Operation(summary = "Get free courses", description = "Get published free courses")
    public ResponseEntity<ApiResponse<List<Course>>> getFreeCourses(
            @RequestParam(defaultValue = "10") int limit) {

        logger.info("Fetching {} free courses", limit);

        try {
            List<Course> courses = courseService.getFreeCourses(limit);

            ApiResponse<List<Course>> response = ApiResponse.success(
                "Free courses retrieved successfully", courses);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching free courses: {}", e.getMessage());
            ApiResponse<List<Course>> errorResponse = ApiResponse.error(
                "Failed to fetch free courses", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Instructor endpoints - Authentication required

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Create new course", description = "Create a new course (Instructor/Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Course created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ApiResponse<Course>> createCourse(
            @Parameter(description = "Course details") @Valid @RequestBody Course course) {

        logger.info("Creating new course: {}", course.getTitle());

        try {
            Course createdCourse = courseService.createCourse(course);

            ApiResponse<Course> response = ApiResponse.success(
                "Course created successfully", createdCourse);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error creating course: {}", e.getMessage());
            ApiResponse<Course> errorResponse = ApiResponse.error(
                "Failed to create course", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Update course", description = "Update an existing course (Instructor/Admin only)")
    public ResponseEntity<ApiResponse<Course>> updateCourse(
            @Parameter(description = "Course ID") @PathVariable Long courseId,
            @Parameter(description = "Updated course details") @Valid @RequestBody Course courseDetails) {

        logger.info("Updating course with ID: {}", courseId);

        try {
            Course updatedCourse = courseService.updateCourse(courseId, courseDetails);

            ApiResponse<Course> response = ApiResponse.success(
                "Course updated successfully", updatedCourse);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error updating course with ID {}: {}", courseId, e.getMessage());
            ApiResponse<Course> errorResponse = ApiResponse.error(
                "Failed to update course", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/{courseId}/publish")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Publish course", description = "Publish a draft course (Instructor/Admin only)")
    public ResponseEntity<ApiResponse<Course>> publishCourse(
            @Parameter(description = "Course ID") @PathVariable Long courseId) {

        logger.info("Publishing course with ID: {}", courseId);

        try {
            Course publishedCourse = courseService.publishCourse(courseId);

            ApiResponse<Course> response = ApiResponse.success(
                "Course published successfully", publishedCourse);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error publishing course with ID {}: {}", courseId, e.getMessage());
            ApiResponse<Course> errorResponse = ApiResponse.error(
                "Failed to publish course", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/{courseId}/submit-approval")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Submit course for approval", description = "Submit course for admin approval (Instructor only)")
    public ResponseEntity<ApiResponse<Course>> submitForApproval(
            @Parameter(description = "Course ID") @PathVariable Long courseId) {

        logger.info("Submitting course for approval with ID: {}", courseId);

        try {
            Course submittedCourse = courseService.submitForApproval(courseId);

            ApiResponse<Course> response = ApiResponse.success(
                "Course submitted for approval", submittedCourse);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error submitting course for approval with ID {}: {}", courseId, e.getMessage());
            ApiResponse<Course> errorResponse = ApiResponse.error(
                "Failed to submit course for approval", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/instructor/{instructorId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get courses by instructor", description = "Get all courses by instructor ID")
    public ResponseEntity<ApiResponse<Page<Course>>> getCoursesByInstructor(
            @Parameter(description = "Instructor ID") @PathVariable Long instructorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Fetching courses for instructor ID: {}", instructorId);

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Course> courses = courseService.getCoursesByInstructor(instructorId, pageable);

            ApiResponse<Page<Course>> response = ApiResponse.success(
                "Instructor courses retrieved successfully", courses);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching courses for instructor {}: {}", instructorId, e.getMessage());
            ApiResponse<Page<Course>> errorResponse = ApiResponse.error(
                "Failed to fetch instructor courses", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Admin endpoints

    @PostMapping("/{courseId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Approve course", description = "Approve a pending course (Admin only)")
    public ResponseEntity<ApiResponse<Course>> approveCourse(
            @Parameter(description = "Course ID") @PathVariable Long courseId) {

        logger.info("Approving course with ID: {}", courseId);

        try {
            Course approvedCourse = courseService.approveCourse(courseId);

            ApiResponse<Course> response = ApiResponse.success(
                "Course approved successfully", approvedCourse);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error approving course with ID {}: {}", courseId, e.getMessage());
            ApiResponse<Course> errorResponse = ApiResponse.error(
                "Failed to approve course", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/{courseId}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Suspend course", description = "Suspend a published course (Admin only)")
    public ResponseEntity<ApiResponse<Course>> suspendCourse(
            @Parameter(description = "Course ID") @PathVariable Long courseId,
            @Parameter(description = "Suspension reason") @RequestParam String reason) {

        logger.info("Suspending course with ID: {} for reason: {}", courseId, reason);

        try {
            Course suspendedCourse = courseService.suspendCourse(courseId, reason);

            ApiResponse<Course> response = ApiResponse.success(
                "Course suspended successfully", suspendedCourse);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error suspending course with ID {}: {}", courseId, e.getMessage());
            ApiResponse<Course> errorResponse = ApiResponse.error(
                "Failed to suspend course", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Delete course", description = "Delete a course (Instructor/Admin only)")
    public ResponseEntity<ApiResponse<String>> deleteCourse(
            @Parameter(description = "Course ID") @PathVariable Long courseId) {

        logger.info("Deleting course with ID: {}", courseId);

        try {
            courseService.deleteCourse(courseId);

            ApiResponse<String> response = ApiResponse.success(
                "Course deleted successfully", "Course with ID " + courseId + " has been deleted");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error deleting course with ID {}: {}", courseId, e.getMessage());
            ApiResponse<String> errorResponse = ApiResponse.error(
                "Failed to delete course", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Statistics endpoints

    @GetMapping("/stats/total-published")
    @Operation(summary = "Get total published courses count", description = "Get count of all published courses")
    public ResponseEntity<ApiResponse<Long>> getTotalPublishedCourses() {
        try {
            long count = courseService.getTotalPublishedCourses();

            ApiResponse<Long> response = ApiResponse.success(
                "Total published courses count retrieved", count);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting total published courses count: {}", e.getMessage());
            ApiResponse<Long> errorResponse = ApiResponse.error(
                "Failed to get courses count", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}