package com.porraglobal.tournament.dto;

import com.porraglobal.tournament.entity.Match;

import java.time.Instant;

public record MatchResponse(
        Long id,
        TeamResponse homeTeam,
        TeamResponse awayTeam,
        Instant kickoffAt,
        Match.Stage stage,
        Match.Status status,
        Integer homeScore,
        Integer awayScore,
        String venue
) {
}
