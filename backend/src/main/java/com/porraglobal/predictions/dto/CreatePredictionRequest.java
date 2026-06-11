package com.porraglobal.predictions.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreatePredictionRequest(
        @NotNull
        Long matchId,

        @NotNull @Min(0) @Max(99)
        Integer homeScore,

        @NotNull @Min(0) @Max(99)
        Integer awayScore
) {
}
