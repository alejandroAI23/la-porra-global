package com.porraglobal.notifications.service;

import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.common.security.CurrentUserProvider;
import com.porraglobal.notifications.entity.Notification;
import com.porraglobal.notifications.mapper.NotificationMapper;
import com.porraglobal.notifications.repository.NotificationRepository;
import com.porraglobal.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private CurrentUserProvider currentUserProvider;

    private NotificationService notificationService;

    private User user;

    @BeforeEach
    void setUp() {
        var mapper = Mappers.getMapper(NotificationMapper.class);
        notificationService = new NotificationService(notificationRepository, mapper, currentUserProvider);
        user = User.builder().id(1L).username("juan").build();
    }

    @Test
    void getMyNotifications_unreadOnly_shouldFilter() {
        when(currentUserProvider.requireId()).thenReturn(1L);
        when(notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(Notification.builder().id(1L).user(user)
                        .type(Notification.Type.SYSTEM).title("Hola").read(false).build()));

        var result = notificationService.getMyNotifications(true);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).read()).isFalse();
    }

    @Test
    void markAsRead_shouldSetReadTrue_whenOwned() {
        var notif = Notification.builder().id(5L).user(user)
                .type(Notification.Type.SYSTEM).title("Hola").read(false).build();
        when(currentUserProvider.requireId()).thenReturn(1L);
        when(notificationRepository.findById(5L)).thenReturn(Optional.of(notif));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = notificationService.markAsRead(5L);

        assertThat(result.read()).isTrue();
    }

    @Test
    void markAsRead_shouldThrow_whenNotOwned() {
        var other = User.builder().id(2L).build();
        var notif = Notification.builder().id(5L).user(other)
                .type(Notification.Type.SYSTEM).title("Hola").build();
        when(currentUserProvider.requireId()).thenReturn(1L);
        when(notificationRepository.findById(5L)).thenReturn(Optional.of(notif));

        assertThatThrownBy(() -> notificationService.markAsRead(5L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
