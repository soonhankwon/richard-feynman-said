package dev.soon.richardfeynmansaid.idea.controller.dto;

import javax.validation.constraints.NotBlank;

public record IdeaSaveReqDto(
        @NotBlank
        String topic,
        String description) {
}
