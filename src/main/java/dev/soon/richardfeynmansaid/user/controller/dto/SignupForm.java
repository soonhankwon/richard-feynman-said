package dev.soon.richardfeynmansaid.user.controller.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record SignupForm(
        @Email
        String email,
        @NotBlank(message = "패스워드는 필수값입니다")
        String password,
        @NotBlank(message = "패스워드 확인은 필수값입니다")
        String passwordConfirm) {
}
