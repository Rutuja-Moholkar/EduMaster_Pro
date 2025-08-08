package com.edumaster.repository;

import com.edumaster.model.Payment;
import com.edumaster.model.User;
import com.edumaster.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Basic queries
    List<Payment> findByUser(User user);
    
    Page<Payment> findByUser(User user, Pageable pageable);
    
    List<Payment> findByCourse(Course course);
    
    Page<Payment> findByCourse(Course course, Pageable pageable);
    
    Optional<Payment> findByUserAndCourse(User user, Course course);
    
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    Page<Payment> findByStatus(Payment.PaymentStatus status, Pageable pageable);
    
    // User-specific queries
    @Query("SELECT p FROM Payment p WHERE p.user.id = :userId")
    List<Payment> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Payment p WHERE p.user.id = :userId")
    Page<Payment> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.user.id = :userId AND p.status = :status")
    List<Payment> findByUserIdAndStatus(@Param("userId") Long userId, 
                                       @Param("status") Payment.PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE p.user.id = :userId ORDER BY p.paymentDate DESC")
    List<Payment> findByUserIdOrderByPaymentDateDesc(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Payment p WHERE p.user.id = :userId ORDER BY p.paymentDate DESC")
    Page<Payment> findByUserIdOrderByPaymentDateDesc(@Param("userId") Long userId, Pageable pageable);
    
    // Course-specific queries
    @Query("SELECT p FROM Payment p WHERE p.course.id = :courseId")
    List<Payment> findByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT p FROM Payment p WHERE p.course.id = :courseId")
    Page<Payment> findByCourseId(@Param("courseId") Long courseId, Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.course.id = :courseId AND p.status = :status")
    List<Payment> findByCourseIdAndStatus(@Param("courseId") Long courseId, 
                                         @Param("status") Payment.PaymentStatus status);
    
    // Instructor queries
    @Query("SELECT p FROM Payment p WHERE p.course.instructor.id = :instructorId")
    List<Payment> findByInstructorId(@Param("instructorId") Long instructorId);
    
    @Query("SELECT p FROM Payment p WHERE p.course.instructor.id = :instructorId")
    Page<Payment> findByInstructorId(@Param("instructorId") Long instructorId, Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.course.instructor.id = :instructorId AND p.status = :status")
    List<Payment> findByInstructorIdAndStatus(@Param("instructorId") Long instructorId, 
                                             @Param("status") Payment.PaymentStatus status);
    
    // Stripe-specific queries
    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
    
    List<Payment> findByStripePaymentMethodId(String stripePaymentMethodId);
    
    boolean existsByStripePaymentIntentId(String stripePaymentIntentId);
    
    // Date-based queries
    @Query("SELECT p FROM Payment p WHERE p.paymentDate >= :startDate AND p.paymentDate <= :endDate")
    List<Payment> findByPaymentDateBetween(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentDate >= :startDate AND p.paymentDate <= :endDate AND p.status = :status")
    List<Payment> findByPaymentDateBetweenAndStatus(@Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate,
                                                   @Param("status") Payment.PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentDate >= :date")
    List<Payment> findByPaymentDateAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT p FROM Payment p WHERE p.refundDate >= :startDate AND p.refundDate <= :endDate")
    List<Payment> findByRefundDateBetween(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    // Amount-based queries
    @Query("SELECT p FROM Payment p WHERE p.amount >= :minAmount AND p.amount <= :maxAmount")
    List<Payment> findByAmountBetween(@Param("minAmount") BigDecimal minAmount, 
                                     @Param("maxAmount") BigDecimal maxAmount);
    
    @Query("SELECT p FROM Payment p WHERE p.amount >= :amount")
    List<Payment> findByAmountGreaterThanEqual(@Param("amount") BigDecimal amount);
    
    // Statistics queries
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.course.id = :courseId")
    long countByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.course.instructor.id = :instructorId")
    long countByInstructorId(@Param("instructorId") Long instructorId);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    long countByStatus(@Param("status") Payment.PaymentStatus status);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.user.id = :userId AND p.status = :status")
    long countByUserIdAndStatus(@Param("userId") Long userId, 
                               @Param("status") Payment.PaymentStatus status);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.course.id = :courseId AND p.status = :status")
    long countByCourseIdAndStatus(@Param("courseId") Long courseId, 
                                 @Param("status") Payment.PaymentStatus status);
    
    // Revenue queries
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCEEDED'")
    BigDecimal getTotalRevenue();
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCEEDED' AND p.course.instructor.id = :instructorId")
    BigDecimal getTotalRevenueByInstructor(@Param("instructorId") Long instructorId);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCEEDED' AND p.course.id = :courseId")
    BigDecimal getTotalRevenueByCourse(@Param("courseId") Long courseId);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCEEDED' " +
           "AND p.paymentDate >= :startDate AND p.paymentDate <= :endDate")
    BigDecimal getTotalRevenueByDateRange(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(p.refundAmount) FROM Payment p WHERE p.status = 'REFUNDED'")
    BigDecimal getTotalRefunds();
    
    @Query("SELECT SUM(p.refundAmount) FROM Payment p WHERE p.status = 'REFUNDED' AND p.course.instructor.id = :instructorId")
    BigDecimal getTotalRefundsByInstructor(@Param("instructorId") Long instructorId);
    
    // Recent payments
    @Query("SELECT p FROM Payment p ORDER BY p.paymentDate DESC")
    List<Payment> findRecentPayments(Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.user.id = :userId ORDER BY p.paymentDate DESC")
    List<Payment> findRecentPaymentsByUser(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.course.instructor.id = :instructorId ORDER BY p.paymentDate DESC")
    List<Payment> findRecentPaymentsByInstructor(@Param("instructorId") Long instructorId, Pageable pageable);
    
    // Successful payments
    @Query("SELECT p FROM Payment p WHERE p.status = 'SUCCEEDED' ORDER BY p.paymentDate DESC")
    List<Payment> findSuccessfulPayments(Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.status = 'SUCCEEDED' AND p.user.id = :userId ORDER BY p.paymentDate DESC")
    List<Payment> findSuccessfulPaymentsByUser(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Payment p WHERE p.status = 'SUCCEEDED' AND p.course.id = :courseId ORDER BY p.paymentDate DESC")
    List<Payment> findSuccessfulPaymentsByCourse(@Param("courseId") Long courseId);
    
    // Failed payments
    @Query("SELECT p FROM Payment p WHERE p.status = 'FAILED' ORDER BY p.paymentDate DESC")
    List<Payment> findFailedPayments(Pageable pageable);
    
    // Refunded payments
    @Query("SELECT p FROM Payment p WHERE p.status = 'REFUNDED' ORDER BY p.refundDate DESC")
    List<Payment> findRefundedPayments(Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.status = 'REFUNDED' AND p.user.id = :userId ORDER BY p.refundDate DESC")
    List<Payment> findRefundedPaymentsByUser(@Param("userId") Long userId);
    
    // Advanced queries with joins
    @Query("SELECT p FROM Payment p " +
           "JOIN FETCH p.user " +
           "JOIN FETCH p.course " +
           "WHERE p.id = :paymentId")
    Optional<Payment> findByIdWithUserAndCourse(@Param("paymentId") Long paymentId);
    
    @Query("SELECT p FROM Payment p " +
           "JOIN FETCH p.course c " +
           "JOIN FETCH c.category " +
           "WHERE p.user.id = :userId")
    List<Payment> findByUserIdWithCourseAndCategory(@Param("userId") Long userId);
    
    // Payment verification
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Payment p " +
           "WHERE p.user.id = :userId AND p.course.id = :courseId AND p.status = 'SUCCEEDED'")
    boolean hasSuccessfulPayment(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    // Currency-based queries
    @Query("SELECT p FROM Payment p WHERE p.currency = :currency")
    List<Payment> findByCurrency(@Param("currency") String currency);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCEEDED' AND p.currency = :currency")
    BigDecimal getTotalRevenueByCurrency(@Param("currency") String currency);
    
    // Category-based payments
    @Query("SELECT p FROM Payment p WHERE p.course.category.id = :categoryId")
    List<Payment> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.course.category.id = :categoryId AND p.status = 'SUCCEEDED'")
    long countSuccessfulPaymentsByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.course.category.id = :categoryId AND p.status = 'SUCCEEDED'")
    BigDecimal getTotalRevenueByCategoryId(@Param("categoryId") Long categoryId);
}