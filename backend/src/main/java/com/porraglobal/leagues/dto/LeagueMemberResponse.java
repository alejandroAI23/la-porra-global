package com.porraglobal.leagues.dto;

import com.porraglobal.leagues.entity.LeagueMember;

import java.time.Instant;

public record LeagueMemberResponse(
        Long id,
        Long userId,
        String username,
        String displayName,
        LeagueMember.MemberRole role,
        Instant joinedAt
) {
}
