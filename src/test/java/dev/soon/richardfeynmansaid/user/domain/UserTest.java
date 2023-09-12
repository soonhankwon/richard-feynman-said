package dev.soon.richardfeynmansaid.user.domain;

import dev.soon.richardfeynmansaid.user.controller.dto.SignupForm;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void getEmail() {
        SignupForm form = new SignupForm("abc@naver.com", "1234");
        User user = new User(form);
        assertThat(user.getEmail()).isEqualTo("abc@naver.com");
    }
}