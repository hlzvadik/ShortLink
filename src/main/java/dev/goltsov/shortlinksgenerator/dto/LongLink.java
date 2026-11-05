package dev.goltsov.shortlinksgenerator.dto;

import jakarta.validation.constraints.NotBlank;

public record LongLink(
        @NotBlank String longLink
) {
}
