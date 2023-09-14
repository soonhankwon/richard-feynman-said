package dev.soon.richardfeynmansaid.auth.service;

import dev.soon.richardfeynmansaid.user.domain.User;

public interface OauthService<T> {
    User login(T params);
}
