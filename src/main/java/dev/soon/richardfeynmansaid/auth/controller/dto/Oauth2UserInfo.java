package dev.soon.richardfeynmansaid.auth.controller.dto;

public interface Oauth2UserInfo {
    String getProvider();
    String getEmail();
    String getNickName();
    String getImageUrl();
}
