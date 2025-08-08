package com.edumaster.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "enrollments", 
       uniqueConstraints = @UniqueConstraint(name = "unique_enrollment", columnNames = {"user_id", "course_id"}),
       indexes = {
           @Index(name = "idx_enrollments_user", columnList = "user_id"),
           @Index(name = "idx_enrollments_course", columnList = "course_id"),
           @Index(name = "idx_enrollments_status", columnList = "completion_status")
       })
public class Enrollment {

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

    @CreationTimestamp
    @Column(name = "enrollment_date", nullable = false, updatable = false)
    private LocalDateTime enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "completion_status", nullable = false)
    private CompletionStatus completionStatus = CompletionStatus.ENROLLED;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @DecimalMin(value = "0.0", message = "Progress percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Progress percentage cannot exceed 100")
    @Column(name = "progress_percentage", precision = 5, scale = 2)
    private BigDecimal progressPercentage = BigDecimal.ZERO;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseProgress> courseProgress;

    public enum CompletionStatus {
        ENROLLED,
        IN_PROGRESS,
        COMPLETED
    }

    public Enrollment() {}

    public Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    public boolean isCompleted() {
        return completionStatus == CompletionStatus.COMPLETED;
    }

    public boolean isInProgress() {
        return completionStatus == CompletionStatus.IN_PROGRESS;
    }

    public void markAsCompleted() {
        this.completionStatus = CompletionStatus.COMPLETED;
        this.completionDate = LocalDateTime.now();
        this.progressPercentage = new BigDecimal("100.00");
    }

    public void updateProgress(BigDecimal newProgress) {
        this.progressPercentage = newProgress;
        if (newProgress.compareTo(BigDecimal.ZERO) > 0 && completionStatus == CompletionStatus.ENROLLED) {
            this.completionStatus = CompletionStatus.IN_PROGRESS;
        }
        if (newProgress.compareTo(new BigDecimal("100.00")) == 0) {
            markAsCompleted();
        }
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

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public CompletionStatus getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(CompletionStatus completionStatus) {
        this.completionStatus = completionStatus;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public BigDecimal getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(BigDecimal progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public List<CourseProgress> getCourseProgress() {
        return courseProgress;
    }

    public void setCourseProgress(List<CourseProgress> courseProgress) {
        this.courseProgress = courseProgress;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", user=" + (user != null ? user.getEmail() : "null") +
                ", course=" + (course != null ? course.getTitle() : "null") +
                ", completionStatus=" + completionStatus +
                ", progressPercentage=" + progressPercentage +
                '}';
    }
}