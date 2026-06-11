package com.porraglobal.bars.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record BarResponse(
        Long id,
        String name,
        String description,
        String address,
        String city,
        BigDecimal latitude,
        BigDecimal longitude,
        String phone,
        Long ownerId,
        boolean verified,
        Instant createdAt
) {
}
