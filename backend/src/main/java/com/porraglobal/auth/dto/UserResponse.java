package com.porraglobal.auth.dto;

import java.time.Instant;
import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String email,
        String displayName,
        String avatarUrl,
        Set<String> roles,
        Instant createdAt
) {
}
