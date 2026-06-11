package com.porraglobal.tournament.dto;

public record TeamResponse(
        Long id,
        String name,
        String code,
        String flagEmoji,
        String groupName
) {
}
