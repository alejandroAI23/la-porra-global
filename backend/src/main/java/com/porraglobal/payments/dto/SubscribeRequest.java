package com.porraglobal.payments.dto;

import com.porraglobal.payments.entity.Subscription;
import jakarta.validation.constraints.NotNull;

public record SubscribeRequest(
        @NotNull
        Subscription.Plan plan
) {
}
