package com.porraglobal.bars.dto;

import java.time.Instant;

public record BarEventResponse(
        Long id,
        Long barId,
        Long matchId,
        String title,
        String description,
        Instant startsAt,
        Integer capacity,
        Instant createdAt
) {
}
