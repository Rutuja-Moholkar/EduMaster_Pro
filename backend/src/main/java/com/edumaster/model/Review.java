package com.edumaster.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews", 
       uniqueConstraints = @UniqueConstraint(name = "unique_review", columnNames = {"user_id", "course_id"}),
       indexes = {
           @Index(name = "idx_reviews_course", columnList = "course_id"),
           @Index(name = "idx_reviews_rating", columnList = "rating"),
           @Index(name = "idx_reviews_approved", columnList = "is_approved")
       })
public class Review {

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

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    @Column(nullable = false)
    private Integer rating;

    @Lob
    private String comment;

    @Column(name = "is_approved")
    private Boolean isApproved = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Review() {}

    public Review(User user, Course course, Integer rating, String comment) {
        this.user = user;
        this.course = course;
        this.rating = rating;
        this.comment = comment;
    }

    public boolean isExcellentRating() {
        return rating >= 4;
    }

    public boolean isPoorRating() {
        return rating <= 2;
    }

    public void approve() {
        this.isApproved = true;
    }

    public void disapprove() {
        this.isApproved = false;
    }

    public String getRatingDescription() {
        return switch (rating) {
            case 1 -> "Poor";
            case 2 -> "Fair";
            case 3 -> "Good";
            case 4 -> "Very Good";
            case 5 -> "Excellent";
            default -> "Unknown";
        };
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", user=" + (user != null ? user.getFullName() : "null") +
                ", course=" + (course != null ? course.getTitle() : "null") +
                ", rating=" + rating +
                ", isApproved=" + isApproved +
                ", createdAt=" + createdAt +
                '}';
    }
}