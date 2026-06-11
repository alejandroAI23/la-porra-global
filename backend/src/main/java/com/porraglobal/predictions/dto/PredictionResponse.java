package com.porraglobal.predictions.dto;

import java.time.Instant;

public record PredictionResponse(
        Long id,
        Long matchId,
        int predictedHomeScore,
        int predictedAwayScore,
        Integer pointsAwarded,
        Instant createdAt,
        Instant updatedAt
) {
}
