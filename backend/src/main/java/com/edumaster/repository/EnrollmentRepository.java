package com.edumaster.repository;

import com.edumaster.model.Course;
import com.edumaster.model.Enrollment;
import com.edumaster.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Basic queries
    List<Enrollment> findByUser(User user);
    
    Page<Enrollment> findByUser(User user, Pageable pageable);
    
    List<Enrollment> findByCourse(Course course);
    
    Page<Enrollment> findByCourse(Course course, Pageable pageable);
    
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
    
    boolean existsByUserAndCourse(User user, Course course);
    
    // User-specific queries
    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId")
    List<Enrollment> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId")
    Page<Enrollment> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId AND e.completionStatus = :status")
    List<Enrollment> findByUserIdAndCompletionStatus(@Param("userId") Long userId, 
                                                    @Param("status") Enrollment.CompletionStatus status);
    
    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId AND e.completionStatus = :status")
    Page<Enrollment> findByUserIdAndCompletionStatus(@Param("userId") Long userId, 
                                                    @Param("status") Enrollment.CompletionStatus status, 
                                                    Pageable pageable);
    
    // Course-specific queries
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId")
    List<Enrollment> findByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId")
    Page<Enrollment> findByCourseId(@Param("courseId") Long courseId, Pageable pageable);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId AND e.completionStatus = :status")
    List<Enrollment> findByCourseIdAndCompletionStatus(@Param("courseId") Long courseId, 
                                                      @Param("status") Enrollment.CompletionStatus status);
    
    // Instructor queries
    @Query("SELECT e FROM Enrollment e WHERE e.course.instructor.id = :instructorId")
    List<Enrollment> findByInstructorId(@Param("instructorId") Long instructorId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course.instructor.id = :instructorId")
    Page<Enrollment> findByInstructorId(@Param("instructorId") Long instructorId, Pageable pageable);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course.instructor.id = :instructorId AND e.completionStatus = :status")
    List<Enrollment> findByInstructorIdAndCompletionStatus(@Param("instructorId") Long instructorId, 
                                                          @Param("status") Enrollment.CompletionStatus status);
    
    // Date-based queries
    @Query("SELECT e FROM Enrollment e WHERE e.enrollmentDate >= :startDate AND e.enrollmentDate <= :endDate")
    List<Enrollment> findByEnrollmentDateBetween(@Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT e FROM Enrollment e WHERE e.enrollmentDate >= :date")
    List<Enrollment> findByEnrollmentDateAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT e FROM Enrollment e WHERE e.completionDate >= :startDate AND e.completionDate <= :endDate")
    List<Enrollment> findByCompletionDateBetween(@Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate);
    
    // Progress-based queries
    @Query("SELECT e FROM Enrollment e WHERE e.progressPercentage >= :minProgress")
    List<Enrollment> findByProgressPercentageGreaterThanEqual(@Param("minProgress") java.math.BigDecimal minProgress);
    
    @Query("SELECT e FROM Enrollment e WHERE e.progressPercentage >= :minProgress AND e.progressPercentage <= :maxProgress")
    List<Enrollment> findByProgressPercentageBetween(@Param("minProgress") java.math.BigDecimal minProgress, 
                                                    @Param("maxProgress") java.math.BigDecimal maxProgress);
    
    // Statistics queries
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId")
    long countByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.instructor.id = :instructorId")
    long countByInstructorId(@Param("instructorId") Long instructorId);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.completionStatus = :status")
    long countByCompletionStatus(@Param("status") Enrollment.CompletionStatus status);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.user.id = :userId AND e.completionStatus = :status")
    long countByUserIdAndCompletionStatus(@Param("userId") Long userId, 
                                         @Param("status") Enrollment.CompletionStatus status);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.completionStatus = :status")
    long countByCourseIdAndCompletionStatus(@Param("courseId") Long courseId, 
                                           @Param("status") Enrollment.CompletionStatus status);
    
    // Recent enrollments
    @Query("SELECT e FROM Enrollment e ORDER BY e.enrollmentDate DESC")
    List<Enrollment> findRecentEnrollments(Pageable pageable);
    
    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId ORDER BY e.enrollmentDate DESC")
    List<Enrollment> findRecentEnrollmentsByUser(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId ORDER BY e.enrollmentDate DESC")
    List<Enrollment> findRecentEnrollmentsByCourse(@Param("courseId") Long courseId, Pageable pageable);
    
    // Active learning (recent activity)
    @Query("SELECT e FROM Enrollment e WHERE e.completionStatus = 'IN_PROGRESS' " +
           "AND e.user.id = :userId ORDER BY e.enrollmentDate DESC")
    List<Enrollment> findActiveLearningByUser(@Param("userId") Long userId);
    
    // Course completion queries
    @Query("SELECT e FROM Enrollment e WHERE e.completionStatus = 'COMPLETED' " +
           "AND e.user.id = :userId ORDER BY e.completionDate DESC")
    List<Enrollment> findCompletedCoursesByUser(@Param("userId") Long userId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.completionStatus = 'COMPLETED' " +
           "AND e.course.id = :courseId ORDER BY e.completionDate DESC")
    List<Enrollment> findCompletedEnrollmentsByCourse(@Param("courseId") Long courseId);
    
    // Advanced queries with joins
    @Query("SELECT e FROM Enrollment e " +
           "JOIN FETCH e.user " +
           "JOIN FETCH e.course " +
           "WHERE e.id = :enrollmentId")
    Optional<Enrollment> findByIdWithUserAndCourse(@Param("enrollmentId") Long enrollmentId);
    
    @Query("SELECT e FROM Enrollment e " +
           "JOIN FETCH e.course c " +
           "JOIN FETCH c.category " +
           "WHERE e.user.id = :userId")
    List<Enrollment> findByUserIdWithCourseAndCategory(@Param("userId") Long userId);
    
    // Enrollment verification
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Enrollment e " +
           "WHERE e.user.id = :userId AND e.course.id = :courseId")
    boolean isUserEnrolledInCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    // Dashboard queries
    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId AND e.completionStatus IN :statuses " +
           "ORDER BY e.enrollmentDate DESC")
    List<Enrollment> findByUserIdAndCompletionStatusIn(@Param("userId") Long userId, 
                                                      @Param("statuses") List<Enrollment.CompletionStatus> statuses,
                                                      Pageable pageable);
    
    // Category-based enrollments
    @Query("SELECT e FROM Enrollment e WHERE e.course.category.id = :categoryId")
    List<Enrollment> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.category.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);
}