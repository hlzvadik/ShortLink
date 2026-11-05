package dev.goltsov.shortlinksgenerator.dto;

public record AuthRequest(
        String username,
        String password
) {
}
