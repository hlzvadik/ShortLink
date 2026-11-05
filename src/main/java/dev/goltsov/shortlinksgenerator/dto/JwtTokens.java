package dev.goltsov.shortlinksgenerator.dto;

public record JwtTokens(
        String accessToken,
        String refreshToken
) {
}
