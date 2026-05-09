package com.restaurant.app.service;

import com.restaurant.app.entity.Notification;

import com.restaurant.app.repository.NotificationRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository
    ) {
        this.notificationRepository = notificationRepository;
    }

    public Notification saveNotification(
            Notification notification
    ) {

        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {

        return notificationRepository.findAll();
    }
}