package com.edumaster.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lessons", indexes = {
    @Index(name = "idx_lessons_course", columnList = "course_id"),
    @Index(name = "idx_lessons_order", columnList = "course_id, order_index")
})
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Course is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotBlank(message = "Lesson title is required")
    @Size(max = 255, message = "Lesson title must not exceed 255 characters")
    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Min(value = 0, message = "Duration cannot be negative")
    @Column(name = "duration_minutes")
    private Integer durationMinutes = 0;

    @NotNull(message = "Order index is required")
    @Min(value = 0, message = "Order index cannot be negative")
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "is_free_preview")
    private Boolean isFreePreview = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType = ContentType.VIDEO;

    @Column(name = "content_url", length = 500)
    private String contentUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseProgress> courseProgress;

    public enum ContentType {
        VIDEO,
        TEXT,
        PDF,
        QUIZ
    }

    public Lesson() {}

    public Lesson(Course course, String title, Integer orderIndex) {
        this.course = course;
        this.title = title;
        this.orderIndex = orderIndex;
    }

    public boolean isVideo() {
        return contentType == ContentType.VIDEO;
    }

    public boolean isText() {
        return contentType == ContentType.TEXT;
    }

    public boolean isPdf() {
        return contentType == ContentType.PDF;
    }

    public boolean isQuiz() {
        return contentType == ContentType.QUIZ;
    }

    public String getFormattedDuration() {
        if (durationMinutes == null || durationMinutes == 0) {
            return "0 min";
        }
        
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;
        
        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%dm", minutes);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Boolean getIsFreePreview() {
        return isFreePreview;
    }

    public void setIsFreePreview(Boolean isFreePreview) {
        this.isFreePreview = isFreePreview;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
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

    public List<CourseProgress> getCourseProgress() {
        return courseProgress;
    }

    public void setCourseProgress(List<CourseProgress> courseProgress) {
        this.courseProgress = courseProgress;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", course=" + (course != null ? course.getTitle() : "null") +
                ", title='" + title + '\'' +
                ", orderIndex=" + orderIndex +
                ", contentType=" + contentType +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}