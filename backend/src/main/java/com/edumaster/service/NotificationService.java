package com.edumaster.service;

import com.edumaster.model.Course;
import com.edumaster.model.Notification;
import com.edumaster.model.User;
import com.edumaster.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    // Create notifications
    public Notification createNotification(User user, String title, String message, 
                                         Notification.NotificationType type) {
        logger.info("Creating notification for user {}: {}", user.getEmail(), title);
        
        Notification notification = new Notification(user, title, message, type);
        return notificationRepository.save(notification);
    }

    public Notification createNotification(User user, String title, String message, 
                                         Notification.NotificationType type,
                                         String relatedEntityType, Long relatedEntityId) {
        logger.info("Creating notification with related entity for user {}: {}", user.getEmail(), title);
        
        Notification notification = new Notification(user, title, message, type, relatedEntityType, relatedEntityId);
        return notificationRepository.save(notification);
    }

    // Specific notification types
    public void sendEnrollmentNotification(User user, Course course) {
        Notification notification = Notification.courseEnrollment(user, course.getTitle());
        notification.setRelatedEntityId(course.getId());
        notificationRepository.save(notification);
        
        logger.info("Enrollment notification sent to user {}", user.getEmail());
    }

    public void sendCourseCompletionNotification(User user, Course course) {
        Notification notification = Notification.courseCompleted(user, course.getTitle(), course.getId());
        notificationRepository.save(notification);
        
        logger.info("Course completion notification sent to user {}", user.getEmail());
    }

    public void sendPaymentSuccessNotification(User user, Course course) {
        Notification notification = Notification.paymentSuccess(user, course.getTitle(), course.getId());
        notificationRepository.save(notification);
        
        logger.info("Payment success notification sent to user {}", user.getEmail());
    }

    public void sendNewCourseNotification(User user, Course course) {
        Notification notification = Notification.newCourse(user, course.getTitle(), course.getId());
        notificationRepository.save(notification);
        
        logger.info("New course notification sent to user {}", user.getEmail());
    }

    public void sendUnenrollmentNotification(User user, Course course, String reason) {
        String message = "You have been unenrolled from " + course.getTitle() + ". Reason: " + reason;
        Notification notification = new Notification(
            user, "Course Unenrollment", message, Notification.NotificationType.WARNING,
            "ENROLLMENT", course.getId()
        );
        notificationRepository.save(notification);
        
        logger.info("Unenrollment notification sent to user {}", user.getEmail());
    }

    // Read operations
    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Page<Notification> getNotificationsByUser(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public List<Notification> getUnreadNotificationsByUser(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    public long getUnreadNotificationsCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    // Mark as read/unread
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.markAsRead();
        return notificationRepository.save(notification);
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = getUnreadNotificationsByUser(userId);
        unreadNotifications.forEach(Notification::markAsRead);
        notificationRepository.saveAll(unreadNotifications);
        
        logger.info("Marked all notifications as read for user ID: {}", userId);
    }

    // Delete operations
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
        logger.info("Deleted notification with ID: {}", notificationId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllNotificationsForUser(Long userId) {
        notificationRepository.deleteByUserId(userId);
        logger.info("Deleted all notifications for user ID: {}", userId);
    }
}