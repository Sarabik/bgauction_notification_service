package com.bgauction.notificationservice.service;

import com.bgauction.notificationservice.model.Notification;

import java.util.List;

public interface NotificationService {
    Notification saveNotification(Notification notification);
    List<Notification> findAllNotifications();
    List<Notification> findAllNotificationsByUserId(Long id);
}
