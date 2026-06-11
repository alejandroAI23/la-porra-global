package com.porraglobal.notifications.mapper;

import com.porraglobal.notifications.dto.NotificationResponse;
import com.porraglobal.notifications.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toResponse(Notification notification);
}
