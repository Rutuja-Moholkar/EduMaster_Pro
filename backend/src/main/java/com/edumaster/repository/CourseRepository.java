package com.edumaster.repository;

import com.edumaster.model.Course;
import com.edumaster.model.CourseLevel;
import com.edumaster.model.CourseStatus;
import com.edumaster.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Basic queries using method names
    List<Course> findByStatus(CourseStatus status);
    
    Page<Course> findByStatus(CourseStatus status, Pageable pageable);
    
    List<Course> findByInstructor(User instructor);
    
    Page<Course> findByInstructor(User instructor, Pageable pageable);
    
    List<Course> findByInstructorAndStatus(User instructor, CourseStatus status);
    
    // Category-based queries
    List<Course> findByCategoryId(Long categoryId);
    
    Page<Course> findByCategoryIdAndStatus(Long categoryId, CourseStatus status, Pageable pageable);
    
    // Search queries
    Page<Course> findByTitleContainingIgnoreCaseAndStatus(String searchTerm, CourseStatus status, Pageable pageable);
    
    List<Course> findByTitleContainingIgnoreCase(String searchTerm);
    
    // Level and price queries
    List<Course> findByLevel(CourseLevel level);
    
    List<Course> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<Course> findByStatusAndLevel(CourseStatus status, CourseLevel level);
    
    // Free and paid courses
    List<Course> findByStatusAndPrice(CourseStatus status, BigDecimal price);
    
    List<Course> findByStatusAndPriceGreaterThan(CourseStatus status, BigDecimal price);
    
    // Instructor-specific queries
    List<Course> findByInstructorId(Long instructorId);
    
    List<Course> findByInstructorIdAndStatus(Long instructorId, CourseStatus status);
    
    // Count queries
    long countByStatus(CourseStatus status);
    
    long countByInstructorId(Long instructorId);
    
    long countByCategoryId(Long categoryId);
    
    // Additional missing methods needed by CourseService - using simple method names
    
    Page<Course> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageable);
    
    // Simplified search method
    default Page<Course> searchCourses(String searchTerm, Pageable pageable) {
        return findByTitleContainingOrDescriptionContaining(searchTerm, searchTerm, pageable);
    }
    
    // Basic filter methods - we'll handle complex filtering in service layer
    Page<Course> findByLevelAndStatus(CourseLevel level, CourseStatus status, Pageable pageable);
    Page<Course> findByPriceGreaterThanEqualAndPriceLessThanEqualAndStatus(BigDecimal minPrice, BigDecimal maxPrice, CourseStatus status, Pageable pageable);
    
    // Simplified filter method - service layer will handle the null checks
    default Page<Course> findCoursesWithFilters(String searchTerm, Long categoryId, CourseLevel level,
                                               BigDecimal minPrice, BigDecimal maxPrice, CourseStatus status, Pageable pageable) {
        // Return basic published courses - complex filtering will be handled in service layer
        return findByStatus(status, pageable);
    }
    
    // Popular courses - for now just order by creation date (can be enhanced later)
    default Page<Course> findPopularCourses(Pageable pageable) {
        return findByStatusOrderByCreatedAtDesc(CourseStatus.PUBLISHED, pageable);
    }
    
    // Recent courses based on creation date
    Page<Course> findByStatusOrderByCreatedAtDesc(CourseStatus status, Pageable pageable);
    default Page<Course> findRecentCourses(Pageable pageable) {
        return findByStatusOrderByCreatedAtDesc(CourseStatus.PUBLISHED, pageable);
    }
    
    // Free courses
    Page<Course> findByStatusAndPrice(CourseStatus status, BigDecimal price, Pageable pageable);
    default Page<Course> findFreeCourses(Pageable pageable) {
        return findByStatusAndPrice(CourseStatus.PUBLISHED, BigDecimal.ZERO, pageable);
    }
    
    // Courses with minimum rating - for now just return all published courses
    default Page<Course> findCoursesWithMinRating(Double minRating, Pageable pageable) {
        return findByStatus(CourseStatus.PUBLISHED, pageable);
    }
    
    long countByInstructorIdAndStatus(Long instructorId, CourseStatus status);
    
    boolean existsByIdAndStatus(Long courseId, CourseStatus status);
    default boolean existsByIdAndPublished(Long courseId) {
        return existsByIdAndStatus(courseId, CourseStatus.PUBLISHED);
    }
}