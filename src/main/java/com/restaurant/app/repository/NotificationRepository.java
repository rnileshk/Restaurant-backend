package com.restaurant.app.repository;

import com.restaurant.app.entity.Notification;
import com.restaurant.app.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(
            User user
    );

    List<Notification> findByReadStatus(
            Boolean readStatus
    );
}