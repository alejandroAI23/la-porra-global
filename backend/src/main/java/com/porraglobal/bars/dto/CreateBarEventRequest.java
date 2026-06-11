package com.porraglobal.bars.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateBarEventRequest(
        @NotBlank @Size(max = 120)
        String title,

        @Size(max = 500)
        String description,

        @NotNull @Future
        Instant startsAt,

        Long matchId,

        @Positive
        Integer capacity
) {
}
