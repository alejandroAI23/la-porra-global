package com.porraglobal.notifications.controller;

import com.porraglobal.notifications.dto.NotificationResponse;
import com.porraglobal.notifications.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notificaciones del usuario")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Listar mis notificaciones")
    public List<NotificationResponse> myNotifications(
            @RequestParam(defaultValue = "false") boolean unreadOnly) {
        return notificationService.getMyNotifications(unreadOnly);
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Número de notificaciones sin leer")
    public Map<String, Long> unreadCount() {
        return Map.of("count", notificationService.getUnreadCount());
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Marcar una notificación como leída")
    public NotificationResponse markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }
}
