package com.porraglobal.leagues.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JoinLeagueRequest(
        @NotBlank @Size(max = 10)
        String inviteCode
) {
}
