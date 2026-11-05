package dev.goltsov.shortlinksgenerator.dto;

import jakarta.validation.constraints.NotBlank;

public record ShortLink(
        @NotBlank String shortLink
) {
}
