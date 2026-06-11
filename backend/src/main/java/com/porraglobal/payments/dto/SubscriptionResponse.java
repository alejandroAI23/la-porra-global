package com.porraglobal.payments.dto;

import com.porraglobal.payments.entity.Subscription;

import java.time.Instant;

public record SubscriptionResponse(
        Long id,
        Subscription.Plan plan,
        Subscription.Status status,
        Instant startsAt,
        Instant endsAt
) {
}
