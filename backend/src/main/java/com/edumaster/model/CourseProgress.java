package com.edumaster.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_progress", 
       uniqueConstraints = @UniqueConstraint(name = "unique_progress", columnNames = {"enrollment_id", "lesson_id"}),
       indexes = {
           @Index(name = "idx_progress_enrollment", columnList = "enrollment_id"),
           @Index(name = "idx_progress_lesson", columnList = "lesson_id")
       })
public class CourseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Enrollment is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @NotNull(message = "Lesson is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(nullable = false)
    private Boolean completed = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Min(value = 0, message = "Watch time cannot be negative")
    @Column(name = "watch_time_seconds")
    private Integer watchTimeSeconds = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CourseProgress() {}

    public CourseProgress(Enrollment enrollment, Lesson lesson) {
        this.enrollment = enrollment;
        this.lesson = lesson;
    }

    public void markAsCompleted() {
        this.completed = true;
        this.completedAt = LocalDateTime.now();
    }

    public void markAsIncomplete() {
        this.completed = false;
        this.completedAt = null;
    }

    public void updateWatchTime(int seconds) {
        this.watchTimeSeconds = seconds;
    }

    public double getCompletionPercentage() {
        if (lesson.getDurationMinutes() == null || lesson.getDurationMinutes() == 0) {
            return completed ? 100.0 : 0.0;
        }
        
        int totalSeconds = lesson.getDurationMinutes() * 60;
        if (watchTimeSeconds >= totalSeconds) {
            return 100.0;
        }
        
        return (watchTimeSeconds * 100.0) / totalSeconds;
    }

    public String getFormattedWatchTime() {
        if (watchTimeSeconds == null || watchTimeSeconds == 0) {
            return "0 sec";
        }
        
        int minutes = watchTimeSeconds / 60;
        int seconds = watchTimeSeconds % 60;
        
        if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
        if (completed && completedAt == null) {
            this.completedAt = LocalDateTime.now();
        } else if (!completed) {
            this.completedAt = null;
        }
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Integer getWatchTimeSeconds() {
        return watchTimeSeconds;
    }

    public void setWatchTimeSeconds(Integer watchTimeSeconds) {
        this.watchTimeSeconds = watchTimeSeconds;
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
        return "CourseProgress{" +
                "id=" + id +
                ", enrollment=" + (enrollment != null ? enrollment.getId() : "null") +
                ", lesson=" + (lesson != null ? lesson.getTitle() : "null") +
                ", completed=" + completed +
                ", watchTimeSeconds=" + watchTimeSeconds +
                '}';
    }
}