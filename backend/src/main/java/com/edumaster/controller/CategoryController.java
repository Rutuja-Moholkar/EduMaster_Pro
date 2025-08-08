package com.edumaster.controller;

import com.edumaster.dto.ApiResponse;
import com.edumaster.model.Category;
import com.edumaster.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Category Management", description = "Category management API endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    // Public endpoints - No authentication required

    @GetMapping("/public")
    @Operation(summary = "Get all categories", description = "Retrieve all categories")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        logger.info("Fetching all categories");

        try {
            List<Category> categories = categoryService.getAllCategories();

            ApiResponse<List<Category>> response = ApiResponse.success(
                "Categories retrieved successfully", categories);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching categories: {}", e.getMessage());
            ApiResponse<List<Category>> errorResponse = ApiResponse.error(
                "Failed to fetch categories", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/public/paginated")
    @Operation(summary = "Get categories with pagination", description = "Retrieve categories with pagination")
    public ResponseEntity<ApiResponse<Page<Category>>> getAllCategoriesPaginated(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        logger.info("Fetching categories with pagination - page: {}, size: {}", page, size);

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                       Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Category> categories = categoryService.getAllCategories(pageable);

            ApiResponse<Page<Category>> response = ApiResponse.success(
                "Categories retrieved successfully", categories);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching categories with pagination: {}", e.getMessage());
            ApiResponse<Page<Category>> errorResponse = ApiResponse.error(
                "Failed to fetch categories", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/public/{categoryId}")
    @Operation(summary = "Get category by ID", description = "Retrieve a category by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<ApiResponse<Category>> getCategoryById(
            @Parameter(description = "Category ID") @PathVariable Long categoryId) {

        logger.info("Fetching category with ID: {}", categoryId);

        try {
            Category category = categoryService.getCategoryByIdOrThrow(categoryId);

            ApiResponse<Category> response = ApiResponse.success(
                "Category retrieved successfully", category);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching category with ID {}: {}", categoryId, e.getMessage());
            ApiResponse<Category> errorResponse = ApiResponse.error(
                "Category not found", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/public/search")
    @Operation(summary = "Search categories", description = "Search categories by name or description")
    public ResponseEntity<ApiResponse<List<Category>>> searchCategories(
            @Parameter(description = "Search term") @RequestParam String searchTerm) {

        logger.info("Searching categories with term: {}", searchTerm);

        try {
            List<Category> categories = categoryService.searchCategories(searchTerm);

            ApiResponse<List<Category>> response = ApiResponse.success(
                "Search completed successfully", categories);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error searching categories: {}", e.getMessage());
            ApiResponse<List<Category>> errorResponse = ApiResponse.error(
                "Search failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/public/with-courses")
    @Operation(summary = "Get categories with published courses", description = "Get categories that have published courses")
    public ResponseEntity<ApiResponse<List<Category>>> getCategoriesWithCourses() {
        logger.info("Fetching categories with published courses");

        try {
            List<Category> categories = categoryService.getCategoriesWithPublishedCourses();

            ApiResponse<List<Category>> response = ApiResponse.success(
                "Categories with courses retrieved successfully", categories);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching categories with courses: {}", e.getMessage());
            ApiResponse<List<Category>> errorResponse = ApiResponse.error(
                "Failed to fetch categories with courses", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/public/popular")
    @Operation(summary = "Get popular categories", description = "Get categories ordered by course count")
    public ResponseEntity<ApiResponse<List<Category>>> getPopularCategories(
            @Parameter(description = "Maximum number of categories to return") @RequestParam(defaultValue = "10") int limit) {

        logger.info("Fetching top {} popular categories", limit);

        try {
            List<Category> categories = categoryService.getPopularCategories(limit);

            ApiResponse<List<Category>> response = ApiResponse.success(
                "Popular categories retrieved successfully", categories);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching popular categories: {}", e.getMessage());
            ApiResponse<List<Category>> errorResponse = ApiResponse.error(
                "Failed to fetch popular categories", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/public/dropdown")
    @Operation(summary = "Get categories for dropdown", description = "Get all categories formatted for dropdown/select components")
    public ResponseEntity<ApiResponse<List<Category>>> getCategoriesForDropdown() {
        logger.info("Fetching categories for dropdown");

        try {
            List<Category> categories = categoryService.getCategoriesForDropdown();

            ApiResponse<List<Category>> response = ApiResponse.success(
                "Categories for dropdown retrieved successfully", categories);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching categories for dropdown: {}", e.getMessage());
            ApiResponse<List<Category>> errorResponse = ApiResponse.error(
                "Failed to fetch categories for dropdown", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Admin endpoints - Authentication required

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new category", description = "Create a new category (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Category created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Category already exists")
    })
    public ResponseEntity<ApiResponse<Category>> createCategory(
            @Parameter(description = "Category details") @Valid @RequestBody Category category) {

        logger.info("Creating new category: {}", category.getName());

        try {
            Category createdCategory = categoryService.createCategory(category);

            ApiResponse<Category> response = ApiResponse.success(
                "Category created successfully", createdCategory);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error creating category: {}", e.getMessage());
            
            HttpStatus status = e.getMessage().contains("already exists") ? 
                               HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
            
            ApiResponse<Category> errorResponse = ApiResponse.error(
                "Failed to create category", e.getMessage());
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update category", description = "Update an existing category (Admin only)")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @Parameter(description = "Category ID") @PathVariable Long categoryId,
            @Parameter(description = "Updated category details") @Valid @RequestBody Category categoryDetails) {

        logger.info("Updating category with ID: {}", categoryId);

        try {
            Category updatedCategory = categoryService.updateCategory(categoryId, categoryDetails);

            ApiResponse<Category> response = ApiResponse.success(
                "Category updated successfully", updatedCategory);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error updating category with ID {}: {}", categoryId, e.getMessage());
            
            HttpStatus status = e.getMessage().contains("not found") ? 
                               HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            
            ApiResponse<Category> errorResponse = ApiResponse.error(
                "Failed to update category", e.getMessage());
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete category", description = "Delete a category (Admin only)")
    public ResponseEntity<ApiResponse<String>> deleteCategory(
            @Parameter(description = "Category ID") @PathVariable Long categoryId) {

        logger.info("Deleting category with ID: {}", categoryId);

        try {
            categoryService.deleteCategory(categoryId);

            ApiResponse<String> response = ApiResponse.success(
                "Category deleted successfully", "Category with ID " + categoryId + " has been deleted");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error deleting category with ID {}: {}", categoryId, e.getMessage());
            
            HttpStatus status = e.getMessage().contains("not found") ? 
                               HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            
            ApiResponse<String> errorResponse = ApiResponse.error(
                "Failed to delete category", e.getMessage());
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    // Statistics endpoints

    @GetMapping("/stats/total")
    @Operation(summary = "Get total categories count", description = "Get count of all categories")
    public ResponseEntity<ApiResponse<Long>> getTotalCategories() {
        try {
            long count = categoryService.getTotalCategories();

            ApiResponse<Long> response = ApiResponse.success(
                "Total categories count retrieved", count);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting total categories count: {}", e.getMessage());
            ApiResponse<Long> errorResponse = ApiResponse.error(
                "Failed to get categories count", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{categoryId}/courses-count")
    @Operation(summary = "Get courses count by category", description = "Get count of published courses in a category")
    public ResponseEntity<ApiResponse<Long>> getCoursesCountByCategory(
            @Parameter(description = "Category ID") @PathVariable Long categoryId) {

        try {
            long count = categoryService.getPublishedCoursesCountByCategory(categoryId);

            ApiResponse<Long> response = ApiResponse.success(
                "Courses count retrieved for category", count);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting courses count for category {}: {}", categoryId, e.getMessage());
            ApiResponse<Long> errorResponse = ApiResponse.error(
                "Failed to get courses count", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}