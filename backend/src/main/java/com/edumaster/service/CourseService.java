package com.edumaster.service;

import com.edumaster.exception.ResourceNotFoundException;
import com.edumaster.model.*;
import com.edumaster.repository.CourseRepository;
import com.edumaster.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    // Create and Update Operations
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Course createCourse(Course course) {
        logger.info("Creating new course: {}", course.getTitle());
        
        if (course.getStatus() == null) {
            course.setStatus(CourseStatus.DRAFT);
        }
        
        Course savedCourse = courseRepository.save(course);
        logger.info("Course created successfully with ID: {}", savedCourse.getId());
        
        return savedCourse;
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Course updateCourse(Long courseId, Course courseDetails) {
        logger.info("Updating course with ID: {}", courseId);
        
        Course existingCourse = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));
        
        // Update fields
        existingCourse.setTitle(courseDetails.getTitle());
        existingCourse.setDescription(courseDetails.getDescription());
        existingCourse.setShortDescription(courseDetails.getShortDescription());
        existingCourse.setPrice(courseDetails.getPrice());
        existingCourse.setCategory(courseDetails.getCategory());
        existingCourse.setLevel(courseDetails.getLevel());
        existingCourse.setLanguage(courseDetails.getLanguage());
        existingCourse.setRequirements(courseDetails.getRequirements());
        existingCourse.setLearningOutcomes(courseDetails.getLearningOutcomes());
        existingCourse.setThumbnailUrl(courseDetails.getThumbnailUrl());
        existingCourse.setDurationHours(courseDetails.getDurationHours());
        
        Course updatedCourse = courseRepository.save(existingCourse);
        logger.info("Course updated successfully: {}", updatedCourse.getTitle());
        
        return updatedCourse;
    }

    // Read Operations
    public Page<Course> getAllPublishedCourses(Pageable pageable) {
        logger.info("Fetching all published courses with pagination");
        return courseRepository.findByStatus(CourseStatus.PUBLISHED, pageable);
    }

    public List<Course> getAllPublishedCourses() {
        logger.info("Fetching all published courses");
        return courseRepository.findByStatus(CourseStatus.PUBLISHED);
    }

    public Optional<Course> getCourseById(Long courseId) {
        logger.info("Fetching course with ID: {}", courseId);
        return courseRepository.findById(courseId);
    }

    public Course getCourseByIdOrThrow(Long courseId) {
        return courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));
    }

    public Course getPublishedCourseById(Long courseId) {
        Course course = getCourseByIdOrThrow(courseId);
        if (!course.isPublished()) {
            throw new ResourceNotFoundException("Published course not found with ID: " + courseId);
        }
        return course;
    }

    public Course getCourseWithFullDetails(Long courseId) {
        // For now, use regular findById - can be enhanced later with JOIN FETCH if needed
        return courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));
    }

    // Search and Filter Operations
    public Page<Course> searchCourses(String keyword, Pageable pageable) {
        logger.info("Searching courses with keyword: {}", keyword);
        return courseRepository.searchCourses(keyword, pageable);
    }

    public Page<Course> getCoursesWithFilters(String searchTerm, Long categoryId, CourseLevel level,
                                            BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        logger.info("Fetching courses with filters - searchTerm: {}, categoryId: {}, level: {}", 
                   searchTerm, categoryId, level);
        
        return courseRepository.findCoursesWithFilters(
            searchTerm, categoryId, level, minPrice, maxPrice, CourseStatus.PUBLISHED, pageable);
    }

    public Page<Course> getCoursesByCategory(Long categoryId, Pageable pageable) {
        logger.info("Fetching courses for category ID: {}", categoryId);
        return courseRepository.findByCategoryIdAndStatus(categoryId, CourseStatus.PUBLISHED, pageable);
    }

    public List<Course> getPopularCourses(int limit) {
        logger.info("Fetching top {} popular courses", limit);
        return courseRepository.findPopularCourses(Pageable.ofSize(limit)).getContent();
    }

    public List<Course> getRecentCourses(int limit) {
        logger.info("Fetching {} most recent courses", limit);
        return courseRepository.findRecentCourses(Pageable.ofSize(limit)).getContent();
    }

    public List<Course> getFreeCourses(int limit) {
        logger.info("Fetching {} free courses", limit);
        return courseRepository.findFreeCourses(Pageable.ofSize(limit)).getContent();
    }

    public List<Course> getCoursesWithMinRating(Double minRating, int limit) {
        logger.info("Fetching courses with minimum rating: {}", minRating);
        return courseRepository.findCoursesWithMinRating(minRating, Pageable.ofSize(limit)).getContent();
    }

    // Instructor-specific Operations
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public List<Course> getCoursesByInstructor(Long instructorId) {
        logger.info("Fetching courses for instructor ID: {}", instructorId);
        return courseRepository.findByInstructorId(instructorId);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Page<Course> getCoursesByInstructor(Long instructorId, Pageable pageable) {
        User instructor = userRepository.findById(instructorId)
            .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with ID: " + instructorId));
        
        return courseRepository.findByInstructor(instructor, pageable);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public List<Course> getCoursesByInstructorAndStatus(Long instructorId, CourseStatus status) {
        logger.info("Fetching courses for instructor ID: {} with status: {}", instructorId, status);
        return courseRepository.findByInstructorIdAndStatus(instructorId, status);
    }

    // Status Management Operations
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Course publishCourse(Long courseId) {
        logger.info("Publishing course with ID: {}", courseId);
        
        Course course = getCourseByIdOrThrow(courseId);
        
        // Validate course is ready for publishing
        validateCourseForPublishing(course);
        
        course.setStatus(CourseStatus.PUBLISHED);
        Course publishedCourse = courseRepository.save(course);
        
        logger.info("Course published successfully: {}", publishedCourse.getTitle());
        return publishedCourse;
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Course submitForApproval(Long courseId) {
        logger.info("Submitting course for approval with ID: {}", courseId);
        
        Course course = getCourseByIdOrThrow(courseId);
        
        if (course.getStatus() != CourseStatus.DRAFT) {
            throw new IllegalStateException("Only draft courses can be submitted for approval");
        }
        
        // Validate course is ready for approval
        validateCourseForPublishing(course);
        
        course.setStatus(CourseStatus.PENDING_APPROVAL);
        Course updatedCourse = courseRepository.save(course);
        
        logger.info("Course submitted for approval: {}", updatedCourse.getTitle());
        return updatedCourse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Course approveCourse(Long courseId) {
        logger.info("Approving course with ID: {}", courseId);
        
        Course course = getCourseByIdOrThrow(courseId);
        
        if (course.getStatus() != CourseStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Only courses pending approval can be approved");
        }
        
        course.setStatus(CourseStatus.PUBLISHED);
        Course approvedCourse = courseRepository.save(course);
        
        logger.info("Course approved and published: {}", approvedCourse.getTitle());
        return approvedCourse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Course suspendCourse(Long courseId, String reason) {
        logger.info("Suspending course with ID: {} for reason: {}", courseId, reason);
        
        Course course = getCourseByIdOrThrow(courseId);
        course.setStatus(CourseStatus.SUSPENDED);
        
        Course suspendedCourse = courseRepository.save(course);
        logger.info("Course suspended: {}", suspendedCourse.getTitle());
        
        return suspendedCourse;
    }

    // Delete Operations
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public void deleteCourse(Long courseId) {
        logger.info("Deleting course with ID: {}", courseId);
        
        Course course = getCourseByIdOrThrow(courseId);
        
        // Check if course has enrollments
        if (course.getTotalEnrollments() > 0) {
            throw new IllegalStateException("Cannot delete course with existing enrollments");
        }
        
        courseRepository.delete(course);
        logger.info("Course deleted successfully with ID: {}", courseId);
    }

    // Statistics Operations
    public long getTotalPublishedCourses() {
        return courseRepository.countByStatus(CourseStatus.PUBLISHED);
    }

    public long getTotalCoursesByInstructor(Long instructorId) {
        return courseRepository.countByInstructorId(instructorId);
    }

    public long getPublishedCoursesByInstructor(Long instructorId) {
        return courseRepository.countByInstructorIdAndStatus(instructorId, CourseStatus.PUBLISHED);
    }

    // Validation Methods
    private void validateCourseForPublishing(Course course) {
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new IllegalStateException("Course title is required for publishing");
        }
        
        if (course.getDescription() == null || course.getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Course description is required for publishing");
        }
        
        if (course.getPrice() == null) {
            throw new IllegalStateException("Course price is required for publishing");
        }
        
        if (course.getCategory() == null) {
            throw new IllegalStateException("Course category is required for publishing");
        }
        
        if (course.getInstructor() == null) {
            throw new IllegalStateException("Course instructor is required for publishing");
        }
        
        // Check if course has at least one lesson
        if (course.getLessons() == null || course.getLessons().isEmpty()) {
            throw new IllegalStateException("Course must have at least one lesson for publishing");
        }
    }

    // Additional utility methods
    public boolean courseExists(Long courseId) {
        return courseRepository.existsById(courseId);
    }

    public boolean isPublishedCourse(Long courseId) {
        return courseRepository.existsByIdAndPublished(courseId);
    }
}