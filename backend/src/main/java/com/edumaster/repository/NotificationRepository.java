package com.edumaster.repository;

import com.edumaster.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Basic queries
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
    
    List<Notification> findByUserIdAndIsReadTrueOrderByCreatedAtDesc(Long userId);
    
    // Count queries
    long countByUserIdAndIsReadFalse(Long userId);
    
    long countByUserId(Long userId);
    
    // Type-based queries
    List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, Notification.NotificationType type);
    
    // Date-based queries
    List<Notification> findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(Long userId, LocalDateTime date);
    
    List<Notification> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long userId, 
                                                                          LocalDateTime startDate, 
                                                                          LocalDateTime endDate);
    
    // Related entity queries
    List<Notification> findByUserIdAndRelatedEntityTypeAndRelatedEntityIdOrderByCreatedAtDesc(
        Long userId, String relatedEntityType, Long relatedEntityId);
    
    // Bulk operations
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId AND n.isRead = false")
    void markAllAsReadByUserId(@Param("userId") Long userId);
}