package com.porraglobal.users.dto;

import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(max = 80)
        String displayName,

        @Size(max = 255)
        String avatarUrl
) {
}
