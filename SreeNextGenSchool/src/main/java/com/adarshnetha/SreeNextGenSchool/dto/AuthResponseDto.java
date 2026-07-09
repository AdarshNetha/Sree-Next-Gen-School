package com.adarshnetha.SreeNextGenSchool.dto;

import java.time.Instant;

public record AuthResponseDto(
        String username,
        String token,
        Instant expiresAt,
        String message
) {
}
