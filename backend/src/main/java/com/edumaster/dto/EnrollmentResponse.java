package com.edumaster.dto;

import com.edumaster.model.Enrollment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EnrollmentResponse {

    private Long id;
    private LocalDateTime enrollmentDate;
    private Enrollment.CompletionStatus completionStatus;
    private LocalDateTime completionDate;
    private BigDecimal progressPercentage;
    
    // User info
    private Long userId;
    private String userEmail;
    private String userFullName;
    
    // Course info
    private Long courseId;
    private String courseTitle;
    private String courseDescription;
    private String courseThumbnailUrl;
    private BigDecimal coursePrice;
    private Integer courseDurationHours;
    
    // Progress info
    private int totalLessons;
    private int completedLessons;
    private boolean isCompleted;
    private boolean isInProgress;

    // Constructors
    public EnrollmentResponse() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Enrollment.CompletionStatus getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(Enrollment.CompletionStatus completionStatus) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseThumbnailUrl() {
        return courseThumbnailUrl;
    }

    public void setCourseThumbnailUrl(String courseThumbnailUrl) {
        this.courseThumbnailUrl = courseThumbnailUrl;
    }

    public BigDecimal getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(BigDecimal coursePrice) {
        this.coursePrice = coursePrice;
    }

    public Integer getCourseDurationHours() {
        return courseDurationHours;
    }

    public void setCourseDurationHours(Integer courseDurationHours) {
        this.courseDurationHours = courseDurationHours;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(int totalLessons) {
        this.totalLessons = totalLessons;
    }

    public int getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(int completedLessons) {
        this.completedLessons = completedLessons;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean inProgress) {
        isInProgress = inProgress;
    }

    @Override
    public String toString() {
        return "EnrollmentResponse{" +
                "id=" + id +
                ", userFullName='" + userFullName + '\'' +
                ", courseTitle='" + courseTitle + '\'' +
                ", completionStatus=" + completionStatus +
                ", progressPercentage=" + progressPercentage +
                '}';
    }
}