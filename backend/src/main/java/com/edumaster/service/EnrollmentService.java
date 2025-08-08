package com.edumaster.service;

import com.edumaster.exception.ResourceNotFoundException;
import com.edumaster.model.*;
import com.edumaster.repository.EnrollmentRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    // Enrollment Operations
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Enrollment enrollUserInCourse(Long userId, Long courseId) {
        logger.info("Enrolling user {} in course {}", userId, courseId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

        // Validate course is published
        if (!course.isPublished()) {
            throw new IllegalStateException("Cannot enroll in unpublished course");
        }

        // Check if user is already enrolled
        if (enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new IllegalStateException("User is already enrolled in this course");
        }

        // For paid courses, check if payment exists (this should be called after payment)
        if (!course.isFree()) {
            // This method should be called from PaymentService after successful payment
            logger.info("Enrolling user in paid course - payment should have been verified");
        }

        // Create enrollment
        Enrollment enrollment = new Enrollment(user, course);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Send notification
        notificationService.sendEnrollmentNotification(user, course);

        logger.info("User {} successfully enrolled in course {}", userId, courseId);
        return savedEnrollment;
    }

    public Enrollment enrollUserInFreeCourse(Long userId, Long courseId) {
        logger.info("Enrolling user {} in free course {}", userId, courseId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

        // Validate course is free and published
        if (!course.isFree()) {
            throw new IllegalStateException("Course is not free - payment required");
        }

        if (!course.isPublished()) {
            throw new IllegalStateException("Cannot enroll in unpublished course");
        }

        // Check if user is already enrolled
        if (enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new IllegalStateException("User is already enrolled in this course");
        }

        // Create enrollment
        Enrollment enrollment = new Enrollment(user, course);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Send notification
        notificationService.sendEnrollmentNotification(user, course);

        logger.info("User {} successfully enrolled in free course {}", userId, courseId);
        return savedEnrollment;
    }

    // Read Operations
    public Optional<Enrollment> getEnrollmentById(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId);
    }

    public Enrollment getEnrollmentByIdOrThrow(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + enrollmentId));
    }

    public Optional<Enrollment> getEnrollmentByUserAndCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

        return enrollmentRepository.findByUserAndCourse(user, course);
    }

    public List<Enrollment> getEnrollmentsByUser(Long userId) {
        logger.info("Fetching enrollments for user {}", userId);
        return enrollmentRepository.findByUserId(userId);
    }

    public Page<Enrollment> getEnrollmentsByUser(Long userId, Pageable pageable) {
        logger.info("Fetching enrollments for user {} with pagination", userId);
        return enrollmentRepository.findByUserId(userId, pageable);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        logger.info("Fetching enrollments for course {}", courseId);
        return enrollmentRepository.findByCourseId(courseId);
    }

    public Page<Enrollment> getEnrollmentsByCourse(Long courseId, Pageable pageable) {
        logger.info("Fetching enrollments for course {} with pagination", courseId);
        return enrollmentRepository.findByCourseId(courseId, pageable);
    }

    // Status-based queries
    public List<Enrollment> getEnrollmentsByUserAndStatus(Long userId, Enrollment.CompletionStatus status) {
        return enrollmentRepository.findByUserIdAndCompletionStatus(userId, status);
    }

    public List<Enrollment> getActiveLearningByUser(Long userId) {
        return enrollmentRepository.findActiveLearningByUser(userId);
    }

    public List<Enrollment> getCompletedCoursesByUser(Long userId) {
        return enrollmentRepository.findCompletedCoursesByUser(userId);
    }

    // Instructor queries
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public List<Enrollment> getEnrollmentsByInstructor(Long instructorId) {
        return enrollmentRepository.findByInstructorId(instructorId);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Page<Enrollment> getEnrollmentsByInstructor(Long instructorId, Pageable pageable) {
        return enrollmentRepository.findByInstructorId(instructorId, pageable);
    }

    // Progress Management
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Enrollment updateEnrollmentProgress(Long enrollmentId, BigDecimal progressPercentage) {
        logger.info("Updating progress for enrollment {} to {}%", enrollmentId, progressPercentage);

        Enrollment enrollment = getEnrollmentByIdOrThrow(enrollmentId);
        
        BigDecimal oldProgress = enrollment.getProgressPercentage();
        enrollment.updateProgress(progressPercentage);
        
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        // Send completion notification if course is completed
        if (enrollment.isCompleted() && oldProgress.compareTo(new BigDecimal("100.00")) < 0) {
            notificationService.sendCourseCompletionNotification(
                enrollment.getUser(), enrollment.getCourse());
        }

        logger.info("Progress updated for enrollment {}: {}% -> {}%", 
                   enrollmentId, oldProgress, progressPercentage);
        
        return updatedEnrollment;
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Enrollment markCourseAsCompleted(Long enrollmentId) {
        logger.info("Marking enrollment {} as completed", enrollmentId);

        Enrollment enrollment = getEnrollmentByIdOrThrow(enrollmentId);
        enrollment.markAsCompleted();
        
        Enrollment completedEnrollment = enrollmentRepository.save(enrollment);

        // Send completion notification
        notificationService.sendCourseCompletionNotification(
            enrollment.getUser(), enrollment.getCourse());

        logger.info("Enrollment {} marked as completed", enrollmentId);
        return completedEnrollment;
    }

    // Validation Methods
    public boolean isUserEnrolledInCourse(Long userId, Long courseId) {
        return enrollmentRepository.isUserEnrolledInCourse(userId, courseId);
    }

    public boolean canUserAccessCourse(Long userId, Long courseId) {
        // Check if user is enrolled or if course is free with preview access
        if (isUserEnrolledInCourse(userId, courseId)) {
            return true;
        }

        // Check if course has free preview lessons
        Course course = courseRepository.findById(courseId).orElse(null);
        return course != null && course.isFree();
    }

    // Statistics Operations
    public long getTotalEnrollmentsByUser(Long userId) {
        return enrollmentRepository.countByUserId(userId);
    }

    public long getTotalEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.countByCourseId(courseId);
    }

    public long getCompletedEnrollmentsByUser(Long userId) {
        return enrollmentRepository.countByUserIdAndCompletionStatus(userId, Enrollment.CompletionStatus.COMPLETED);
    }

    public long getInProgressEnrollmentsByUser(Long userId) {
        return enrollmentRepository.countByUserIdAndCompletionStatus(userId, Enrollment.CompletionStatus.IN_PROGRESS);
    }

    public long getCompletedEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.countByCourseIdAndCompletionStatus(courseId, Enrollment.CompletionStatus.COMPLETED);
    }

    public double getCourseCompletionRate(Long courseId) {
        long totalEnrollments = getTotalEnrollmentsByCourse(courseId);
        if (totalEnrollments == 0) {
            return 0.0;
        }
        
        long completedEnrollments = getCompletedEnrollmentsByCourse(courseId);
        return (completedEnrollments * 100.0) / totalEnrollments;
    }

    public double getUserLearningProgress(Long userId) {
        long totalEnrollments = getTotalEnrollmentsByUser(userId);
        if (totalEnrollments == 0) {
            return 0.0;
        }
        
        long completedEnrollments = getCompletedEnrollmentsByUser(userId);
        return (completedEnrollments * 100.0) / totalEnrollments;
    }

    // Recent activity
    public List<Enrollment> getRecentEnrollments(int limit) {
        return enrollmentRepository.findRecentEnrollments(Pageable.ofSize(limit));
    }

    public List<Enrollment> getRecentEnrollmentsByUser(Long userId, int limit) {
        return enrollmentRepository.findRecentEnrollmentsByUser(userId, Pageable.ofSize(limit));
    }

    // Dashboard data
    public List<Enrollment> getUserDashboardEnrollments(Long userId) {
        // Get active learning and recently enrolled courses
        List<Enrollment.CompletionStatus> activeStatuses = List.of(
            Enrollment.CompletionStatus.ENROLLED,
            Enrollment.CompletionStatus.IN_PROGRESS
        );
        
        return enrollmentRepository.findByUserIdAndCompletionStatusIn(
            userId, activeStatuses, Pageable.ofSize(10));
    }

    // Unenrollment (if needed)
    @PreAuthorize("hasRole('ADMIN')")
    public void unenrollUser(Long enrollmentId, String reason) {
        logger.info("Unenrolling user - enrollment ID: {}, reason: {}", enrollmentId, reason);

        Enrollment enrollment = getEnrollmentByIdOrThrow(enrollmentId);
        
        // Log the unenrollment
        logger.info("Unenrolling user {} from course {} - reason: {}", 
                   enrollment.getUser().getEmail(), 
                   enrollment.getCourse().getTitle(), 
                   reason);

        enrollmentRepository.delete(enrollment);

        // Send notification about unenrollment
        notificationService.sendUnenrollmentNotification(
            enrollment.getUser(), enrollment.getCourse(), reason);
    }

    // Bulk operations
    @PreAuthorize("hasRole('ADMIN')")
    public List<Enrollment> getEnrollmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return enrollmentRepository.findByEnrollmentDateBetween(startDate, endDate);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public List<Enrollment> getEnrollmentsForInstructorCourses(Long instructorId) {
        return enrollmentRepository.findByInstructorId(instructorId);
    }

    // Category-based enrollments
    public long getEnrollmentsByCategory(Long categoryId) {
        return enrollmentRepository.countByCategoryId(categoryId);
    }
}