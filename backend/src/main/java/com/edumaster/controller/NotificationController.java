package com.edumaster.controller;

import com.edumaster.dto.ApiResponse;
import com.edumaster.model.Notification;
import com.edumaster.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/notifications")
@Tag(name = "Notification Management", description = "User notification management API endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    // Read Operations

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get user notifications", description = "Get all notifications for a user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ApiResponse<Page<Notification>>> getUserNotifications(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        logger.info("Fetching notifications for user {} with pagination", userId);

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Notification> notifications = notificationService.getNotificationsByUser(userId, pageable);

            ApiResponse<Page<Notification>> response = ApiResponse.success(
                "User notifications retrieved successfully", notifications);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching notifications for user {}: {}", userId, e.getMessage());
            ApiResponse<Page<Notification>> errorResponse = ApiResponse.error(
                "Failed to fetch user notifications", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get unread notifications", description = "Get all unread notifications for a user")
    public ResponseEntity<ApiResponse<List<Notification>>> getUnreadNotifications(
            @Parameter(description = "User ID") @PathVariable Long userId) {

        logger.info("Fetching unread notifications for user {}", userId);

        try {
            List<Notification> unreadNotifications = notificationService.getUnreadNotificationsByUser(userId);

            ApiResponse<List<Notification>> response = ApiResponse.success(
                "Unread notifications retrieved successfully", unreadNotifications);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching unread notifications for user {}: {}", userId, e.getMessage());
            ApiResponse<List<Notification>> errorResponse = ApiResponse.error(
                "Failed to fetch unread notifications", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}/count")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get unread notifications count", description = "Get count of unread notifications for a user")
    public ResponseEntity<ApiResponse<Long>> getUnreadNotificationsCount(
            @Parameter(description = "User ID") @PathVariable Long userId) {

        logger.info("Fetching unread notifications count for user {}", userId);

        try {
            long unreadCount = notificationService.getUnreadNotificationsCount(userId);

            ApiResponse<Long> response = ApiResponse.success(
                "Unread notifications count retrieved successfully", unreadCount);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching unread notifications count for user {}: {}", userId, e.getMessage());
            ApiResponse<Long> errorResponse = ApiResponse.error(
                "Failed to fetch unread notifications count", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Mark as Read Operations

    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Mark notification as read", description = "Mark a specific notification as read")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification marked as read"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ApiResponse<Notification>> markAsRead(
            @Parameter(description = "Notification ID") @PathVariable Long notificationId) {

        logger.info("Marking notification {} as read", notificationId);

        try {
            Notification notification = notificationService.markAsRead(notificationId);

            ApiResponse<Notification> response = ApiResponse.success(
                "Notification marked as read", notification);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error marking notification {} as read: {}", notificationId, e.getMessage());
            
            HttpStatus status = e.getMessage().contains("not found") ? 
                               HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            
            ApiResponse<Notification> errorResponse = ApiResponse.error(
                "Failed to mark notification as read", e.getMessage());
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    @PutMapping("/user/{userId}/read-all")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Mark all notifications as read", description = "Mark all notifications as read for a user")
    public ResponseEntity<ApiResponse<String>> markAllAsRead(
            @Parameter(description = "User ID") @PathVariable Long userId) {

        logger.info("Marking all notifications as read for user {}", userId);

        try {
            notificationService.markAllAsRead(userId);

            ApiResponse<String> response = ApiResponse.success(
                "All notifications marked as read", "Success");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error marking all notifications as read for user {}: {}", userId, e.getMessage());
            ApiResponse<String> errorResponse = ApiResponse.error(
                "Failed to mark all notifications as read", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Delete Operations

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Delete notification", description = "Delete a specific notification")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ApiResponse<String>> deleteNotification(
            @Parameter(description = "Notification ID") @PathVariable Long notificationId) {

        logger.info("Deleting notification {}", notificationId);

        try {
            notificationService.deleteNotification(notificationId);

            ApiResponse<String> response = ApiResponse.success(
                "Notification deleted successfully", "Deleted");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error deleting notification {}: {}", notificationId, e.getMessage());
            
            HttpStatus status = e.getMessage().contains("not found") ? 
                               HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            
            ApiResponse<String> errorResponse = ApiResponse.error(
                "Failed to delete notification", e.getMessage());
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    @DeleteMapping("/user/{userId}/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete all user notifications", description = "Delete all notifications for a user (Admin only)")
    public ResponseEntity<ApiResponse<String>> deleteAllUserNotifications(
            @Parameter(description = "User ID") @PathVariable Long userId) {

        logger.info("Deleting all notifications for user {}", userId);

        try {
            notificationService.deleteAllNotificationsForUser(userId);

            ApiResponse<String> response = ApiResponse.success(
                "All user notifications deleted successfully", "Deleted");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error deleting all notifications for user {}: {}", userId, e.getMessage());
            ApiResponse<String> errorResponse = ApiResponse.error(
                "Failed to delete all user notifications", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Create Notification (Admin/System use)

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create notification", description = "Create a new notification for a user (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Notification created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ApiResponse<Notification>> createNotification(
            @Parameter(description = "Notification details") @RequestBody NotificationCreateRequest request) {

        logger.info("Creating notification for user {}: {}", request.getUserId(), request.getTitle());

        try {
            // This would typically be done through services, but providing admin override
            // You might want to inject UserRepository to get the User entity
            // For now, this is a placeholder implementation
            
            ApiResponse<Notification> errorResponse = ApiResponse.error(
                "Direct notification creation not implemented", 
                "Use service methods for notification creation");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(errorResponse);

        } catch (Exception e) {
            logger.error("Error creating notification: {}", e.getMessage());
            ApiResponse<Notification> errorResponse = ApiResponse.error(
                "Failed to create notification", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Notification Statistics (Admin)

    @GetMapping("/stats/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user notification statistics", description = "Get notification statistics for a user (Admin only)")
    public ResponseEntity<ApiResponse<NotificationStats>> getUserNotificationStats(
            @Parameter(description = "User ID") @PathVariable Long userId) {

        logger.info("Fetching notification statistics for user {}", userId);

        try {
            List<Notification> allNotifications = notificationService.getNotificationsByUser(userId);
            long unreadCount = notificationService.getUnreadNotificationsCount(userId);
            
            long totalNotifications = allNotifications.size();
            long readCount = totalNotifications - unreadCount;
            
            // Count by type
            long infoCount = allNotifications.stream().mapToInt(n -> n.isInfo() ? 1 : 0).sum();
            long successCount = allNotifications.stream().mapToInt(n -> n.isSuccess() ? 1 : 0).sum();
            long warningCount = allNotifications.stream().mapToInt(n -> n.isWarning() ? 1 : 0).sum();
            long errorCount = allNotifications.stream().mapToInt(n -> n.isError() ? 1 : 0).sum();

            NotificationStats stats = new NotificationStats(
                totalNotifications, readCount, unreadCount, 
                infoCount, successCount, warningCount, errorCount
            );

            ApiResponse<NotificationStats> response = ApiResponse.success(
                "User notification statistics retrieved successfully", stats);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching notification statistics for user {}: {}", userId, e.getMessage());
            ApiResponse<NotificationStats> errorResponse = ApiResponse.error(
                "Failed to fetch notification statistics", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Inner classes for request/response DTOs

    public static class NotificationCreateRequest {
        private Long userId;
        private String title;
        private String message;
        private Notification.NotificationType type;
        private String relatedEntityType;
        private Long relatedEntityId;

        // Constructors
        public NotificationCreateRequest() {}

        // Getters and Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public Notification.NotificationType getType() { return type; }
        public void setType(Notification.NotificationType type) { this.type = type; }
        
        public String getRelatedEntityType() { return relatedEntityType; }
        public void setRelatedEntityType(String relatedEntityType) { this.relatedEntityType = relatedEntityType; }
        
        public Long getRelatedEntityId() { return relatedEntityId; }
        public void setRelatedEntityId(Long relatedEntityId) { this.relatedEntityId = relatedEntityId; }
    }

    public static class NotificationStats {
        private long totalNotifications;
        private long readNotifications;
        private long unreadNotifications;
        private long infoNotifications;
        private long successNotifications;
        private long warningNotifications;
        private long errorNotifications;

        public NotificationStats(long totalNotifications, long readNotifications, long unreadNotifications,
                               long infoNotifications, long successNotifications, 
                               long warningNotifications, long errorNotifications) {
            this.totalNotifications = totalNotifications;
            this.readNotifications = readNotifications;
            this.unreadNotifications = unreadNotifications;
            this.infoNotifications = infoNotifications;
            this.successNotifications = successNotifications;
            this.warningNotifications = warningNotifications;
            this.errorNotifications = errorNotifications;
        }

        // Getters
        public long getTotalNotifications() { return totalNotifications; }
        public long getReadNotifications() { return readNotifications; }
        public long getUnreadNotifications() { return unreadNotifications; }
        public long getInfoNotifications() { return infoNotifications; }
        public long getSuccessNotifications() { return successNotifications; }
        public long getWarningNotifications() { return warningNotifications; }
        public long getErrorNotifications() { return errorNotifications; }
    }
}