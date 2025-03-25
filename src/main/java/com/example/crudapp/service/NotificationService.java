package com.example.crudapp.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void createNotification(Long userId, String message) {
        // Implement notification logic here
        // For now, just log the notification
        System.out.println("Notification sent to user " + userId + ": " + message);
    }
}