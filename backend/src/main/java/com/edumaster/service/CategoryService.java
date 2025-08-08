package com.edumaster.service;

import com.edumaster.exception.ResourceNotFoundException;
import com.edumaster.exception.UserAlreadyExistsException;
import com.edumaster.model.Category;
import com.edumaster.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    // Create Operations
    @PreAuthorize("hasRole('ADMIN')")
    public Category createCategory(Category category) {
        logger.info("Creating new category: {}", category.getName());
        
        // Check if category with same name already exists
        if (categoryRepository.existsByName(category.getName())) {
            throw new UserAlreadyExistsException("Category already exists with name: " + category.getName());
        }
        
        Category savedCategory = categoryRepository.save(category);
        logger.info("Category created successfully with ID: {}", savedCategory.getId());
        
        return savedCategory;
    }

    // Read Operations
    public List<Category> getAllCategories() {
        logger.info("Fetching all categories");
        return categoryRepository.findAllByOrderByNameAsc();
    }

    public Page<Category> getAllCategories(Pageable pageable) {
        logger.info("Fetching all categories with pagination");
        return categoryRepository.findAllByOrderByNameAsc(pageable);
    }

    public Optional<Category> getCategoryById(Long categoryId) {
        logger.info("Fetching category with ID: {}", categoryId);
        return categoryRepository.findById(categoryId);
    }

    public Category getCategoryByIdOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
    }

    public Optional<Category> getCategoryByName(String name) {
        logger.info("Fetching category with name: {}", name);
        return categoryRepository.findByName(name);
    }

    public Category getCategoryByNameOrThrow(String name) {
        return categoryRepository.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
    }

    // Search Operations
    public List<Category> searchCategories(String searchTerm) {
        logger.info("Searching categories with term: {}", searchTerm);
        return categoryRepository.searchCategories(searchTerm);
    }

    public Page<Category> searchCategories(String searchTerm, Pageable pageable) {
        logger.info("Searching categories with term: {} and pagination", searchTerm);
        return categoryRepository.searchCategories(searchTerm, pageable);
    }

    // Categories with published courses
    public List<Category> getCategoriesWithPublishedCourses() {
        logger.info("Fetching categories that have published courses");
        return categoryRepository.findCategoriesHavingPublishedCourses();
    }

    public Page<Category> getCategoriesWithPublishedCourses(Pageable pageable) {
        logger.info("Fetching categories that have published courses with pagination");
        return categoryRepository.findCategoriesHavingPublishedCourses(pageable);
    }

    public List<Category> getCategoriesOrderedByCourseCount() {
        logger.info("Fetching categories ordered by course count");
        return categoryRepository.findCategoriesOrderedByCourseCount();
    }

    // Update Operations
    @PreAuthorize("hasRole('ADMIN')")
    public Category updateCategory(Long categoryId, Category categoryDetails) {
        logger.info("Updating category with ID: {}", categoryId);
        
        Category existingCategory = getCategoryByIdOrThrow(categoryId);
        
        // Check if new name conflicts with existing category (excluding current one)
        if (!existingCategory.getName().equals(categoryDetails.getName()) && 
            categoryRepository.existsByName(categoryDetails.getName())) {
            throw new UserAlreadyExistsException("Category already exists with name: " + categoryDetails.getName());
        }
        
        // Update fields
        existingCategory.setName(categoryDetails.getName());
        existingCategory.setDescription(categoryDetails.getDescription());
        
        Category updatedCategory = categoryRepository.save(existingCategory);
        logger.info("Category updated successfully: {}", updatedCategory.getName());
        
        return updatedCategory;
    }

    // Delete Operations
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(Long categoryId) {
        logger.info("Deleting category with ID: {}", categoryId);
        
        Category category = getCategoryByIdOrThrow(categoryId);
        
        // Check if category has courses
        long courseCount = categoryRepository.countPublishedCoursesByCategoryId(categoryId);
        if (courseCount > 0) {
            throw new IllegalStateException("Cannot delete category with existing courses. " +
                                          "Please reassign or delete the courses first.");
        }
        
        categoryRepository.delete(category);
        logger.info("Category deleted successfully with ID: {}", categoryId);
    }

    // Statistics Operations
    public long getTotalCategories() {
        return categoryRepository.count();
    }

    public long getPublishedCoursesCountByCategory(Long categoryId) {
        return categoryRepository.countPublishedCoursesByCategoryId(categoryId);
    }

    public long getCategoriesWithCoursesCount() {
        return categoryRepository.findCategoriesHavingPublishedCourses().size();
    }

    // Validation Methods
    public boolean categoryExists(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    public boolean categoryExistsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    // Utility Methods
    public Category getOrCreateCategory(String categoryName, String description) {
        Optional<Category> existingCategory = categoryRepository.findByName(categoryName);
        
        if (existingCategory.isPresent()) {
            return existingCategory.get();
        }
        
        Category newCategory = new Category();
        newCategory.setName(categoryName);
        newCategory.setDescription(description);
        
        return categoryRepository.save(newCategory);
    }

    // Popular categories (categories with most published courses)
    public List<Category> getPopularCategories(int limit) {
        logger.info("Fetching top {} popular categories", limit);
        List<Category> categories = categoryRepository.findCategoriesOrderedByCourseCount();
        
        return categories.size() > limit ? categories.subList(0, limit) : categories;
    }

    // Categories for dropdown/select components
    public List<Category> getCategoriesForDropdown() {
        logger.info("Fetching categories for dropdown");
        return categoryRepository.findAllByOrderByNameAsc();
    }
}