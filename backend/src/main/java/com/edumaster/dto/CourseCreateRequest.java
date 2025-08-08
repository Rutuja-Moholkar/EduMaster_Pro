package com.edumaster.dto;

import com.edumaster.model.CourseLevel;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CourseCreateRequest {

    @NotBlank(message = "Course title is required")
    @Size(max = 255, message = "Course title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Course description is required")
    private String description;

    @Size(max = 500, message = "Short description must not exceed 500 characters")
    private String shortDescription;

    @NotNull(message = "Course price is required")
    @DecimalMin(value = "0.0", message = "Course price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Instructor ID is required")
    private Long instructorId;

    private CourseLevel level = CourseLevel.BEGINNER;

    @Size(max = 50, message = "Language must not exceed 50 characters")
    private String language = "English";

    private String requirements;

    private String learningOutcomes;

    private String thumbnailUrl;

    private Integer durationHours = 0;

    // Constructors
    public CourseCreateRequest() {}

    public CourseCreateRequest(String title, String description, BigDecimal price, 
                             Long categoryId, Long instructorId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.instructorId = instructorId;
    }

    // Getters and Setters
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public CourseLevel getLevel() {
        return level;
    }

    public void setLevel(CourseLevel level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getLearningOutcomes() {
        return learningOutcomes;
    }

    public void setLearningOutcomes(String learningOutcomes) {
        this.learningOutcomes = learningOutcomes;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }

    @Override
    public String toString() {
        return "CourseCreateRequest{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", categoryId=" + categoryId +
                ", instructorId=" + instructorId +
                ", level=" + level +
                '}';
    }
}