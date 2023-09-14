package dev.soon.richardfeynmansaid.auth.controller.dto;

public record OauthTokenResDto(
        String token_type,
        String access_token,
        Integer expires_in,
        String refresh_token,
        Integer refresh_token_expires_in) {
}
