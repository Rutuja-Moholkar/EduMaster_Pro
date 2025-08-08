package com.edumaster.service;

import com.edumaster.exception.ResourceNotFoundException;
import com.edumaster.model.*;
import com.edumaster.repository.PaymentRepository;
import com.edumaster.repository.CourseRepository;
import com.edumaster.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.RefundCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private NotificationService notificationService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
        logger.info("Stripe API initialized");
    }

    // Payment Intent Creation
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public PaymentIntent createPaymentIntent(Long userId, Long courseId) throws StripeException {
        logger.info("Creating payment intent for user {} and course {}", userId, courseId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

        // Validate course is published and not free
        if (!course.isPublished()) {
            throw new IllegalStateException("Cannot purchase unpublished course");
        }

        if (course.isFree()) {
            throw new IllegalStateException("Cannot create payment for free course");
        }

        // Check if user is already enrolled
        if (enrollmentService.isUserEnrolledInCourse(userId, courseId)) {
            throw new IllegalStateException("User is already enrolled in this course");
        }

        // Check if there's already a successful payment
        if (paymentRepository.hasSuccessfulPayment(userId, courseId)) {
            throw new IllegalStateException("Payment already exists for this course");
        }

        // Convert price to cents (Stripe requires amounts in smallest currency unit)
        long amountInCents = course.getPrice().multiply(new BigDecimal("100")).longValue();

        // Create payment intent
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(amountInCents)
            .setCurrency("usd")
            .putMetadata("user_id", userId.toString())
            .putMetadata("course_id", courseId.toString())
            .putMetadata("course_title", course.getTitle())
            .putMetadata("user_email", user.getEmail())
            .setDescription("Payment for course: " + course.getTitle())
            .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Create payment record
        Payment payment = new Payment(user, course, course.getPrice());
        payment.setStripePaymentIntentId(paymentIntent.getId());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        paymentRepository.save(payment);

        logger.info("Payment intent created: {} for user {} and course {}", 
                   paymentIntent.getId(), userId, courseId);

        return paymentIntent;
    }

    // Payment Confirmation
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Payment confirmPayment(String paymentIntentId, String paymentMethodId) throws StripeException {
        logger.info("Confirming payment intent: {}", paymentIntentId);

        // Find payment record
        Payment payment = paymentRepository.findByStripePaymentIntentId(paymentIntentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found for payment intent: " + paymentIntentId));

        try {
            // Confirm payment intent with Stripe
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            
            PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder()
                .setPaymentMethod(paymentMethodId)
                .setReturnUrl("https://your-domain.com/return") // Configure this URL
                .build();

            paymentIntent = paymentIntent.confirm(confirmParams);

            // Update payment record
            payment.setStripePaymentMethodId(paymentMethodId);

            if ("succeeded".equals(paymentIntent.getStatus())) {
                payment.markAsSucceeded();
                Payment savedPayment = paymentRepository.save(payment);

                // Enroll user in course
                enrollmentService.enrollUserInCourse(payment.getUser().getId(), payment.getCourse().getId());

                // Send notifications
                notificationService.sendPaymentSuccessNotification(payment.getUser(), payment.getCourse());

                logger.info("Payment confirmed and user enrolled - Payment ID: {}", savedPayment.getId());
                return savedPayment;

            } else if ("requires_action".equals(paymentIntent.getStatus())) {
                // Payment requires additional authentication
                logger.info("Payment requires additional action: {}", paymentIntentId);
                return payment;

            } else {
                payment.markAsFailed();
                paymentRepository.save(payment);
                throw new RuntimeException("Payment failed with status: " + paymentIntent.getStatus());
            }

        } catch (StripeException e) {
            logger.error("Stripe error confirming payment {}: {}", paymentIntentId, e.getMessage());
            payment.markAsFailed();
            paymentRepository.save(payment);
            throw e;
        }
    }

    // Webhook Handler for Stripe Events
    public void handleStripeWebhook(String paymentIntentId, String eventType) {
        logger.info("Handling Stripe webhook - Payment Intent: {}, Event: {}", paymentIntentId, eventType);

        Optional<Payment> paymentOpt = paymentRepository.findByStripePaymentIntentId(paymentIntentId);
        if (paymentOpt.isEmpty()) {
            logger.warn("Payment not found for webhook event: {}", paymentIntentId);
            return;
        }

        Payment payment = paymentOpt.get();

        try {
            switch (eventType) {
                case "payment_intent.succeeded":
                    handlePaymentSucceeded(payment);
                    break;
                case "payment_intent.payment_failed":
                    handlePaymentFailed(payment);
                    break;
                case "payment_intent.canceled":
                    handlePaymentCanceled(payment);
                    break;
                default:
                    logger.info("Unhandled webhook event type: {}", eventType);
            }
        } catch (Exception e) {
            logger.error("Error handling webhook for payment {}: {}", payment.getId(), e.getMessage());
        }
    }

    private void handlePaymentSucceeded(Payment payment) {
        if (!payment.isSuccessful()) {
            payment.markAsSucceeded();
            paymentRepository.save(payment);

            // Enroll user in course if not already enrolled
            if (!enrollmentService.isUserEnrolledInCourse(payment.getUser().getId(), payment.getCourse().getId())) {
                enrollmentService.enrollUserInCourse(payment.getUser().getId(), payment.getCourse().getId());
            }

            // Send notification
            notificationService.sendPaymentSuccessNotification(payment.getUser(), payment.getCourse());

            logger.info("Payment succeeded via webhook - Payment ID: {}", payment.getId());
        }
    }

    private void handlePaymentFailed(Payment payment) {
        payment.markAsFailed();
        paymentRepository.save(payment);
        logger.info("Payment failed via webhook - Payment ID: {}", payment.getId());
    }

    private void handlePaymentCanceled(Payment payment) {
        payment.setStatus(Payment.PaymentStatus.CANCELED);
        paymentRepository.save(payment);
        logger.info("Payment canceled via webhook - Payment ID: {}", payment.getId());
    }

    // Read Operations
    public Optional<Payment> getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public Payment getPaymentByIdOrThrow(Long paymentId) {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + paymentId));
    }

    public List<Payment> getPaymentsByUser(Long userId) {
        return paymentRepository.findByUserIdOrderByPaymentDateDesc(userId);
    }

    public Page<Payment> getPaymentsByUser(Long userId, Pageable pageable) {
        return paymentRepository.findByUserIdOrderByPaymentDateDesc(userId, pageable);
    }

    public List<Payment> getPaymentsByCourse(Long courseId) {
        return paymentRepository.findByCourseId(courseId);
    }

    public Page<Payment> getPaymentsByCourse(Long courseId, Pageable pageable) {
        return paymentRepository.findByCourseId(courseId, pageable);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public List<Payment> getPaymentsByInstructor(Long instructorId) {
        return paymentRepository.findByInstructorId(instructorId);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Page<Payment> getPaymentsByInstructor(Long instructorId, Pageable pageable) {
        return paymentRepository.findByInstructorId(instructorId, pageable);
    }

    // Refund Operations
    @PreAuthorize("hasRole('ADMIN')")
    public Payment refundPayment(Long paymentId, BigDecimal refundAmount, String reason) throws StripeException {
        logger.info("Processing refund for payment {} - Amount: {}, Reason: {}", paymentId, refundAmount, reason);

        Payment payment = getPaymentByIdOrThrow(paymentId);

        if (!payment.isSuccessful()) {
            throw new IllegalStateException("Cannot refund unsuccessful payment");
        }

        if (payment.isRefunded()) {
            throw new IllegalStateException("Payment is already refunded");
        }

        // Validate refund amount
        BigDecimal maxRefundAmount = payment.getAmount().subtract(payment.getRefundAmount());
        if (refundAmount.compareTo(maxRefundAmount) > 0) {
            throw new IllegalArgumentException("Refund amount exceeds available amount");
        }

        try {
            // Create refund with Stripe
            long refundAmountInCents = refundAmount.multiply(new BigDecimal("100")).longValue();
            
            RefundCreateParams refundParams = RefundCreateParams.builder()
                .setPaymentIntent(payment.getStripePaymentIntentId())
                .setAmount(refundAmountInCents)
                .setReason(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER)
                .putMetadata("reason", reason)
                .build();

            Refund stripeRefund = Refund.create(refundParams);

            if ("succeeded".equals(stripeRefund.getStatus())) {
                // Update payment record
                payment.refund(refundAmount, reason);
                Payment refundedPayment = paymentRepository.save(payment);

                logger.info("Refund processed successfully - Payment ID: {}, Refund Amount: {}", 
                           paymentId, refundAmount);

                return refundedPayment;
            } else {
                throw new RuntimeException("Refund failed with status: " + stripeRefund.getStatus());
            }

        } catch (StripeException e) {
            logger.error("Stripe error processing refund for payment {}: {}", paymentId, e.getMessage());
            throw e;
        }
    }

    // Statistics Operations
    public BigDecimal getTotalRevenue() {
        BigDecimal revenue = paymentRepository.getTotalRevenue();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public BigDecimal getTotalRevenueByInstructor(Long instructorId) {
        BigDecimal revenue = paymentRepository.getTotalRevenueByInstructor(instructorId);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    public BigDecimal getTotalRevenueByCourse(Long courseId) {
        BigDecimal revenue = paymentRepository.getTotalRevenueByCourse(courseId);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    public BigDecimal getTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal revenue = paymentRepository.getTotalRevenueByDateRange(startDate, endDate);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    public BigDecimal getTotalRefunds() {
        BigDecimal refunds = paymentRepository.getTotalRefunds();
        return refunds != null ? refunds : BigDecimal.ZERO;
    }

    public long getTotalPaymentsByUser(Long userId) {
        return paymentRepository.countByUserId(userId);
    }

    public long getSuccessfulPaymentsByUser(Long userId) {
        return paymentRepository.countByUserIdAndStatus(userId, Payment.PaymentStatus.SUCCEEDED);
    }

    public long getTotalPaymentsByCourse(Long courseId) {
        return paymentRepository.countByCourseId(courseId);
    }

    public long getSuccessfulPaymentsByCourse(Long courseId) {
        return paymentRepository.countByCourseIdAndStatus(courseId, Payment.PaymentStatus.SUCCEEDED);
    }

    // Recent payments
    public List<Payment> getRecentPayments(int limit) {
        return paymentRepository.findRecentPayments(Pageable.ofSize(limit));
    }

    public List<Payment> getRecentPaymentsByUser(Long userId, int limit) {
        return paymentRepository.findRecentPaymentsByUser(userId, Pageable.ofSize(limit));
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public List<Payment> getRecentPaymentsByInstructor(Long instructorId, int limit) {
        return paymentRepository.findRecentPaymentsByInstructor(instructorId, Pageable.ofSize(limit));
    }

    // Payment verification
    public boolean hasUserPaidForCourse(Long userId, Long courseId) {
        return paymentRepository.hasSuccessfulPayment(userId, courseId);
    }

    // Payment status queries
    public List<Payment> getPaymentsByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    public List<Payment> getSuccessfulPayments(int limit) {
        return paymentRepository.findSuccessfulPayments(Pageable.ofSize(limit));
    }

    public List<Payment> getFailedPayments(int limit) {
        return paymentRepository.findFailedPayments(Pageable.ofSize(limit));
    }

    public List<Payment> getRefundedPayments(int limit) {
        return paymentRepository.findRefundedPayments(Pageable.ofSize(limit));
    }

    // Admin operations
    @PreAuthorize("hasRole('ADMIN')")
    public List<Payment> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Payment> getSuccessfulPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByPaymentDateBetweenAndStatus(startDate, endDate, Payment.PaymentStatus.SUCCEEDED);
    }

    // Payment analytics
    public Map<String, Object> getPaymentAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        analytics.put("totalRevenue", getTotalRevenue());
        analytics.put("totalRefunds", getTotalRefunds());
        analytics.put("netRevenue", getTotalRevenue().subtract(getTotalRefunds()));
        analytics.put("totalSuccessfulPayments", paymentRepository.countByStatus(Payment.PaymentStatus.SUCCEEDED));
        analytics.put("totalFailedPayments", paymentRepository.countByStatus(Payment.PaymentStatus.FAILED));
        analytics.put("totalRefundedPayments", paymentRepository.countByStatus(Payment.PaymentStatus.REFUNDED));
        
        return analytics;
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Map<String, Object> getInstructorPaymentAnalytics(Long instructorId) {
        Map<String, Object> analytics = new HashMap<>();
        
        BigDecimal totalRevenue = getTotalRevenueByInstructor(instructorId);
        BigDecimal totalRefunds = paymentRepository.getTotalRefundsByInstructor(instructorId);
        
        analytics.put("totalRevenue", totalRevenue);
        analytics.put("totalRefunds", totalRefunds != null ? totalRefunds : BigDecimal.ZERO);
        analytics.put("netRevenue", totalRevenue.subtract(totalRefunds != null ? totalRefunds : BigDecimal.ZERO));
        analytics.put("totalPayments", paymentRepository.countByInstructorId(instructorId));
        analytics.put("successfulPayments", paymentRepository.findByInstructorIdAndStatus(instructorId, Payment.PaymentStatus.SUCCEEDED).size());
        
        return analytics;
    }
}