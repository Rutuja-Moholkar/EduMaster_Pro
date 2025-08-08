package com.edumaster.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payments_user", columnList = "user_id"),
    @Index(name = "idx_payments_course", columnList = "course_id"),
    @Index(name = "idx_payments_status", columnList = "status"),
    @Index(name = "idx_payments_stripe", columnList = "stripe_payment_intent_id")
})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Course is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", message = "Amount cannot be negative")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Size(max = 3, message = "Currency code must not exceed 3 characters")
    @Column(length = 3)
    private String currency = "USD";

    @Column(name = "stripe_payment_intent_id", unique = true)
    private String stripePaymentIntentId;

    @Column(name = "stripe_payment_method_id")
    private String stripePaymentMethodId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @CreationTimestamp
    @Column(name = "payment_date", nullable = false, updatable = false)
    private LocalDateTime paymentDate;

    @DecimalMin(value = "0.0", message = "Refund amount cannot be negative")
    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount = BigDecimal.ZERO;

    @Column(name = "refund_date")
    private LocalDateTime refundDate;

    @Lob
    @Column(name = "refund_reason")
    private String refundReason;

    public enum PaymentStatus {
        PENDING,
        SUCCEEDED,
        FAILED,
        REFUNDED,
        CANCELED
    }

    public Payment() {}

    public Payment(User user, Course course, BigDecimal amount) {
        this.user = user;
        this.course = course;
        this.amount = amount;
    }

    public boolean isSuccessful() {
        return status == PaymentStatus.SUCCEEDED;
    }

    public boolean isFailed() {
        return status == PaymentStatus.FAILED;
    }

    public boolean isRefunded() {
        return status == PaymentStatus.REFUNDED;
    }

    public boolean isPending() {
        return status == PaymentStatus.PENDING;
    }

    public void markAsSucceeded() {
        this.status = PaymentStatus.SUCCEEDED;
    }

    public void markAsFailed() {
        this.status = PaymentStatus.FAILED;
    }

    public void refund(BigDecimal refundAmount, String reason) {
        this.status = PaymentStatus.REFUNDED;
        this.refundAmount = refundAmount;
        this.refundReason = reason;
        this.refundDate = LocalDateTime.now();
    }

    public BigDecimal getNetAmount() {
        return amount.subtract(refundAmount);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public String getStripePaymentMethodId() {
        return stripePaymentMethodId;
    }

    public void setStripePaymentMethodId(String stripePaymentMethodId) {
        this.stripePaymentMethodId = stripePaymentMethodId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public LocalDateTime getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(LocalDateTime refundDate) {
        this.refundDate = refundDate;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", user=" + (user != null ? user.getEmail() : "null") +
                ", course=" + (course != null ? course.getTitle() : "null") +
                ", amount=" + amount +
                ", status=" + status +
                ", paymentDate=" + paymentDate +
                '}';
    }
}