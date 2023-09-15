package dev.soon.richardfeynmansaid.idea.controller.dto;

import javax.validation.constraints.NotBlank;

public record IdeaEditReqDto(
        Long id,
        @NotBlank(message = "주제는 필수입니다") String topic,
        String description) {
}
