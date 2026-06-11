package com.porraglobal.ranking.dto;

public record RankingEntryResponse(
        int position,
        Long userId,
        String username,
        String displayName,
        int totalPoints,
        int exactHits,
        int outcomeHits
) {
}
