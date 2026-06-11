package com.porraglobal.notifications.service;

import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.common.security.CurrentUserProvider;
import com.porraglobal.notifications.dto.NotificationResponse;
import com.porraglobal.notifications.entity.Notification;
import com.porraglobal.notifications.mapper.NotificationMapper;
import com.porraglobal.notifications.repository.NotificationRepository;
import com.porraglobal.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final CurrentUserProvider currentUserProvider;

    /** Punto de entrada para que otros módulos generen notificaciones. */
    @Transactional
    public void notifyUser(User user, Notification.Type type, String title, String message) {
        notificationRepository.save(Notification.builder()
                .user(user)
                .type(type)
                .title(title)
                .message(message)
                .build());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications(boolean unreadOnly) {
        Long userId = currentUserProvider.requireId();
        List<Notification> list = unreadOnly
                ? notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId)
                : notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return list.stream().map(notificationMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public long getUnreadCount() {
        return notificationRepository.countByUserIdAndReadFalse(currentUserProvider.requireId());
    }

    @Transactional
    public NotificationResponse markAsRead(Long id) {
        Long userId = currentUserProvider.requireId();
        Notification notification = notificationRepository.findById(id)
                .filter(n -> n.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Notificación", id));
        notification.setRead(true);
        return notificationMapper.toResponse(notificationRepository.save(notification));
    }
}
