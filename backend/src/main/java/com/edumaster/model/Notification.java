package com.edumaster.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notifications_user", columnList = "user_id"),
    @Index(name = "idx_notifications_read", columnList = "is_read"),
    @Index(name = "idx_notifications_type", columnList = "type"),
    @Index(name = "idx_notifications_created", columnList = "created_at")
})
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Message is required")
    @Lob
    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type = NotificationType.INFO;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Size(max = 50, message = "Related entity type must not exceed 50 characters")
    @Column(name = "related_entity_type", length = 50)
    private String relatedEntityType;

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum NotificationType {
        INFO,
        SUCCESS,
        WARNING,
        ERROR
    }

    public Notification() {}

    public Notification(User user, String title, String message, NotificationType type) {
        this.user = user;
        this.title = title;
        this.message = message;
        this.type = type;
    }

    public Notification(User user, String title, String message, NotificationType type, 
                       String relatedEntityType, Long relatedEntityId) {
        this.user = user;
        this.title = title;
        this.message = message;
        this.type = type;
        this.relatedEntityType = relatedEntityType;
        this.relatedEntityId = relatedEntityId;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public void markAsUnread() {
        this.isRead = false;
    }

    public boolean isInfo() {
        return type == NotificationType.INFO;
    }

    public boolean isSuccess() {
        return type == NotificationType.SUCCESS;
    }

    public boolean isWarning() {
        return type == NotificationType.WARNING;
    }

    public boolean isError() {
        return type == NotificationType.ERROR;
    }

    public String getTypeIcon() {
        return switch (type) {
            case INFO -> "ℹ️";
            case SUCCESS -> "✅";
            case WARNING -> "⚠️";
            case ERROR -> "❌";
        };
    }

    // Static factory methods for common notification types
    public static Notification courseEnrollment(User user, String courseTitle) {
        return new Notification(
            user,
            "Course Enrollment Successful",
            "You have successfully enrolled in " + courseTitle,
            NotificationType.SUCCESS,
            "COURSE",
            null
        );
    }

    public static Notification paymentSuccess(User user, String courseTitle, Long courseId) {
        return new Notification(
            user,
            "Payment Successful",
            "Your payment for " + courseTitle + " has been processed successfully",
            NotificationType.SUCCESS,
            "PAYMENT",
            courseId
        );
    }

    public static Notification courseCompleted(User user, String courseTitle, Long courseId) {
        return new Notification(
            user,
            "Course Completed!",
            "Congratulations! You have completed " + courseTitle,
            NotificationType.SUCCESS,
            "COURSE",
            courseId
        );
    }

    public static Notification newCourse(User user, String courseTitle, Long courseId) {
        return new Notification(
            user,
            "New Course Available",
            "A new course " + courseTitle + " is now available",
            NotificationType.INFO,
            "COURSE",
            courseId
        );
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getRelatedEntityType() {
        return relatedEntityType;
    }

    public void setRelatedEntityType(String relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }

    public Long getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(Long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", user=" + (user != null ? user.getEmail() : "null") +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}