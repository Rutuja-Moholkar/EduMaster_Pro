package com.edumaster.controller;

import com.edumaster.dto.ApiResponse;
import com.edumaster.model.Payment;
import com.edumaster.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payment Management", description = "Payment processing and management API endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    // Payment Processing

    @PostMapping("/create-intent")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Create payment intent", description = "Create a Stripe payment intent for course purchase")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Payment intent created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request or course not purchasable"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User or course not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ApiResponse<PaymentIntentResponse>> createPaymentIntent(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Course ID") @RequestParam Long courseId) {

        logger.info("Creating payment intent for user {} and course {}", userId, courseId);

        try {
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(userId, courseId);

            PaymentIntentResponse response = new PaymentIntentResponse(
                paymentIntent.getId(),
                paymentIntent.getClientSecret(),
                paymentIntent.getAmount(),
                paymentIntent.getCurrency()
            );

            ApiResponse<PaymentIntentResponse> apiResponse = ApiResponse.success(
                "Payment intent created successfully", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

        } catch (StripeException e) {
            logger.error("Stripe error creating payment intent for user {} and course {}: {}", 
                        userId, courseId, e.getMessage());
            ApiResponse<PaymentIntentResponse> errorResponse = ApiResponse.error(
                "Failed to create payment intent", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            logger.error("Error creating payment intent for user {} and course {}: {}", 
                        userId, courseId, e.getMessage());
            
            HttpStatus status = e.getMessage().contains("not found") ? 
                               HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            
            ApiResponse<PaymentIntentResponse> errorResponse = ApiResponse.error(
                "Failed to create payment intent", e.getMessage());
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    @PostMapping("/confirm")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Confirm payment", description = "Confirm payment with payment method and complete enrollment")
    public ResponseEntity<ApiResponse<Payment>> confirmPayment(
            @Parameter(description = "Payment Intent ID") @RequestParam String paymentIntentId,
            @Parameter(description = "Payment Method ID") @RequestParam String paymentMethodId) {

        logger.info("Confirming payment intent: {}", paymentIntentId);

        try {
            Payment payment = paymentService.confirmPayment(paymentIntentId, paymentMethodId);

            ApiResponse<Payment> response = ApiResponse.success(
                "Payment confirmed successfully", payment);

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            logger.error("Stripe error confirming payment {}: {}", paymentIntentId, e.getMessage());
            ApiResponse<Payment> errorResponse = ApiResponse.error(
                "Payment confirmation failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            logger.error("Error confirming payment {}: {}", paymentIntentId, e.getMessage());
            ApiResponse<Payment> errorResponse = ApiResponse.error(
                "Payment confirmation failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/webhook")
    @Operation(summary = "Stripe webhook", description = "Handle Stripe webhook events")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        logger.info("Received Stripe webhook");

        try {
            // In a real implementation, you would verify the webhook signature
            // and parse the event properly. This is a simplified version.
            
            // For now, we'll handle basic events
            // You should implement proper webhook signature verification
            
            logger.info("Webhook processed successfully");
            return ResponseEntity.ok("Webhook processed");

        } catch (Exception e) {
            logger.error("Error processing webhook: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook processing failed");
        }
    }

    // Read Operations

    @GetMapping("/{paymentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get payment by ID", description = "Get payment details by ID")
    public ResponseEntity<ApiResponse<Payment>> getPaymentById(
            @Parameter(description = "Payment ID") @PathVariable Long paymentId) {

        logger.info("Fetching payment with ID: {}", paymentId);

        try {
            Payment payment = paymentService.getPaymentByIdOrThrow(paymentId);

            ApiResponse<Payment> response = ApiResponse.success(
                "Payment retrieved successfully", payment);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching payment with ID {}: {}", paymentId, e.getMessage());
            ApiResponse<Payment> errorResponse = ApiResponse.error(
                "Payment not found", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get user payments", description = "Get all payments for a user")
    public ResponseEntity<ApiResponse<Page<Payment>>> getUserPayments(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        logger.info("Fetching payments for user {} with pagination", userId);

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("paymentDate").descending());
            Page<Payment> payments = paymentService.getPaymentsByUser(userId, pageable);

            ApiResponse<Page<Payment>> response = ApiResponse.success(
                "User payments retrieved successfully", payments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching payments for user {}: {}", userId, e.getMessage());
            ApiResponse<Page<Payment>> errorResponse = ApiResponse.error(
                "Failed to fetch user payments", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get course payments", description = "Get all payments for a course (Instructor/Admin only)")
    public ResponseEntity<ApiResponse<Page<Payment>>> getCoursePayments(
            @Parameter(description = "Course ID") @PathVariable Long courseId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        logger.info("Fetching payments for course {} with pagination", courseId);

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("paymentDate").descending());
            Page<Payment> payments = paymentService.getPaymentsByCourse(courseId, pageable);

            ApiResponse<Page<Payment>> response = ApiResponse.success(
                "Course payments retrieved successfully", payments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching payments for course {}: {}", courseId, e.getMessage());
            ApiResponse<Page<Payment>> errorResponse = ApiResponse.error(
                "Failed to fetch course payments", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/instructor/{instructorId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get instructor payments", description = "Get all payments for instructor's courses")
    public ResponseEntity<ApiResponse<Page<Payment>>> getInstructorPayments(
            @Parameter(description = "Instructor ID") @PathVariable Long instructorId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        logger.info("Fetching payments for instructor {} with pagination", instructorId);

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("paymentDate").descending());
            Page<Payment> payments = paymentService.getPaymentsByInstructor(instructorId, pageable);

            ApiResponse<Page<Payment>> response = ApiResponse.success(
                "Instructor payments retrieved successfully", payments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching payments for instructor {}: {}", instructorId, e.getMessage());
            ApiResponse<Page<Payment>> errorResponse = ApiResponse.error(
                "Failed to fetch instructor payments", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Refund Operations

    @PostMapping("/{paymentId}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Refund payment", description = "Process refund for a payment (Admin only)")
    public ResponseEntity<ApiResponse<Payment>> refundPayment(
            @Parameter(description = "Payment ID") @PathVariable Long paymentId,
            @Parameter(description = "Refund amount") @RequestParam BigDecimal refundAmount,
            @Parameter(description = "Refund reason") @RequestParam String reason) {

        logger.info("Processing refund for payment {} - Amount: {}", paymentId, refundAmount);

        try {
            Payment refundedPayment = paymentService.refundPayment(paymentId, refundAmount, reason);

            ApiResponse<Payment> response = ApiResponse.success(
                "Refund processed successfully", refundedPayment);

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            logger.error("Stripe error processing refund for payment {}: {}", paymentId, e.getMessage());
            ApiResponse<Payment> errorResponse = ApiResponse.error(
                "Refund processing failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            logger.error("Error processing refund for payment {}: {}", paymentId, e.getMessage());
            ApiResponse<Payment> errorResponse = ApiResponse.error(
                "Refund processing failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Verification Endpoints

    @GetMapping("/verify")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Verify payment", description = "Check if user has paid for a course")
    public ResponseEntity<ApiResponse<Boolean>> verifyPayment(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Course ID") @RequestParam Long courseId) {

        logger.info("Verifying payment for user {} and course {}", userId, courseId);

        try {
            boolean hasPaid = paymentService.hasUserPaidForCourse(userId, courseId);

            ApiResponse<Boolean> response = ApiResponse.success(
                "Payment verification completed", hasPaid);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error verifying payment: {}", e.getMessage());
            ApiResponse<Boolean> errorResponse = ApiResponse.error(
                "Payment verification failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Statistics and Analytics

    @GetMapping("/analytics")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get payment analytics", description = "Get overall payment analytics (Admin only)")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPaymentAnalytics() {
        logger.info("Fetching payment analytics");

        try {
            Map<String, Object> analytics = paymentService.getPaymentAnalytics();

            ApiResponse<Map<String, Object>> response = ApiResponse.success(
                "Payment analytics retrieved successfully", analytics);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching payment analytics: {}", e.getMessage());
            ApiResponse<Map<String, Object>> errorResponse = ApiResponse.error(
                "Failed to fetch payment analytics", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/analytics/instructor/{instructorId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get instructor payment analytics", description = "Get payment analytics for instructor")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInstructorPaymentAnalytics(
            @Parameter(description = "Instructor ID") @PathVariable Long instructorId) {

        logger.info("Fetching payment analytics for instructor {}", instructorId);

        try {
            Map<String, Object> analytics = paymentService.getInstructorPaymentAnalytics(instructorId);

            ApiResponse<Map<String, Object>> response = ApiResponse.success(
                "Instructor payment analytics retrieved successfully", analytics);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching payment analytics for instructor {}: {}", instructorId, e.getMessage());
            ApiResponse<Map<String, Object>> errorResponse = ApiResponse.error(
                "Failed to fetch instructor payment analytics", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/revenue/total")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get total revenue", description = "Get total platform revenue (Admin only)")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalRevenue() {
        try {
            BigDecimal totalRevenue = paymentService.getTotalRevenue();

            ApiResponse<BigDecimal> response = ApiResponse.success(
                "Total revenue retrieved successfully", totalRevenue);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching total revenue: {}", e.getMessage());
            ApiResponse<BigDecimal> errorResponse = ApiResponse.error(
                "Failed to fetch total revenue", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/revenue/instructor/{instructorId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get instructor revenue", description = "Get total revenue for instructor")
    public ResponseEntity<ApiResponse<BigDecimal>> getInstructorRevenue(
            @Parameter(description = "Instructor ID") @PathVariable Long instructorId) {

        try {
            BigDecimal instructorRevenue = paymentService.getTotalRevenueByInstructor(instructorId);

            ApiResponse<BigDecimal> response = ApiResponse.success(
                "Instructor revenue retrieved successfully", instructorRevenue);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching revenue for instructor {}: {}", instructorId, e.getMessage());
            ApiResponse<BigDecimal> errorResponse = ApiResponse.error(
                "Failed to fetch instructor revenue", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/revenue/course/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get course revenue", description = "Get total revenue for course")
    public ResponseEntity<ApiResponse<BigDecimal>> getCourseRevenue(
            @Parameter(description = "Course ID") @PathVariable Long courseId) {

        try {
            BigDecimal courseRevenue = paymentService.getTotalRevenueByCourse(courseId);

            ApiResponse<BigDecimal> response = ApiResponse.success(
                "Course revenue retrieved successfully", courseRevenue);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching revenue for course {}: {}", courseId, e.getMessage());
            ApiResponse<BigDecimal> errorResponse = ApiResponse.error(
                "Failed to fetch course revenue", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/revenue/daterange")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get revenue by date range", description = "Get revenue for specific date range (Admin only)")
    public ResponseEntity<ApiResponse<BigDecimal>> getRevenueByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        try {
            BigDecimal revenue = paymentService.getTotalRevenueByDateRange(startDate, endDate);

            ApiResponse<BigDecimal> response = ApiResponse.success(
                "Revenue by date range retrieved successfully", revenue);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching revenue by date range: {}", e.getMessage());
            ApiResponse<BigDecimal> errorResponse = ApiResponse.error(
                "Failed to fetch revenue by date range", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Recent payments
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get recent payments", description = "Get recent payments (Admin only)")
    public ResponseEntity<ApiResponse<List<Payment>>> getRecentPayments(
            @Parameter(description = "Number of recent payments") @RequestParam(defaultValue = "10") int limit) {

        try {
            List<Payment> recentPayments = paymentService.getRecentPayments(limit);

            ApiResponse<List<Payment>> response = ApiResponse.success(
                "Recent payments retrieved successfully", recentPayments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching recent payments: {}", e.getMessage());
            ApiResponse<List<Payment>> errorResponse = ApiResponse.error(
                "Failed to fetch recent payments", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Inner class for payment intent response
    public static class PaymentIntentResponse {
        private String paymentIntentId;
        private String clientSecret;
        private Long amount;
        private String currency;

        public PaymentIntentResponse(String paymentIntentId, String clientSecret, Long amount, String currency) {
            this.paymentIntentId = paymentIntentId;
            this.clientSecret = clientSecret;
            this.amount = amount;
            this.currency = currency;
        }

        // Getters
        public String getPaymentIntentId() { return paymentIntentId; }
        public String getClientSecret() { return clientSecret; }
        public Long getAmount() { return amount; }
        public String getCurrency() { return currency; }
    }
}