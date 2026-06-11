package com.porraglobal.leagues.dto;

import com.porraglobal.leagues.entity.League;

import java.time.Instant;

public record LeagueResponse(
        Long id,
        String name,
        String description,
        String inviteCode,
        League.Type type,
        Long ownerId,
        String ownerUsername,
        int maxMembers,
        long memberCount,
        Instant createdAt
) {
}
