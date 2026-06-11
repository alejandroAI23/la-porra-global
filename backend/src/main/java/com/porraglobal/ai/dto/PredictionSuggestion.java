package com.porraglobal.ai.dto;

public record PredictionSuggestion(
        Long matchId,
        String homeTeam,
        String awayTeam,
        int suggestedHomeScore,
        int suggestedAwayScore,
        String confidence,
        String rationale
) {
}
