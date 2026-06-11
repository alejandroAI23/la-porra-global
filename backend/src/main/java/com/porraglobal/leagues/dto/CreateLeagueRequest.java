package com.porraglobal.leagues.dto;

import com.porraglobal.leagues.entity.League;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateLeagueRequest(
        @NotBlank @Size(max = 80)
        String name,

        @Size(max = 255)
        String description,

        League.Type type,

        @Min(2) @Max(500)
        Integer maxMembers
) {
}
