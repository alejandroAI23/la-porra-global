package com.porraglobal.payments.mapper;

import com.porraglobal.payments.dto.SubscriptionResponse;
import com.porraglobal.payments.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponse toResponse(Subscription subscription);
}
