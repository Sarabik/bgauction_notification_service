package com.bgauction.notificationservice.service.impl;

import com.bgauction.notificationservice.model.Notification;
import com.bgauction.notificationservice.repository.NotificationRepository;
import com.bgauction.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> findAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> findAllNotificationsByUserId(Long id) {
        return notificationRepository.findAllByUserId(id);
    }
}
