package com.porraglobal.notifications.dto;

import com.porraglobal.notifications.entity.Notification;

import java.time.Instant;

public record NotificationResponse(
        Long id,
        Notification.Type type,
        String title,
        String message,
        boolean read,
        Instant createdAt
) {
}
