package dev.soon.richardfeynmansaid.auth.service;

import dev.soon.richardfeynmansaid.auth.controller.dto.KakaoOauthLoginParam;
import dev.soon.richardfeynmansaid.auth.controller.dto.KakaoUserInfo;
import dev.soon.richardfeynmansaid.auth.controller.dto.Oauth2UserInfo;
import dev.soon.richardfeynmansaid.auth.controller.dto.OauthTokenResDto;
import dev.soon.richardfeynmansaid.user.domain.User;
import dev.soon.richardfeynmansaid.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOauthService implements OauthService<KakaoOauthLoginParam> {

    private final UserRepository userRepository;
    private final InMemoryClientRegistrationRepository inMemoryRepository;

    /**
     * @InMemoryRepository application-oauth properties 정보를 담고 있음
     * @getToken() 넘겨받은 code 로 Oauth 서버에 Token 요청
     * @getUserProfile 첫 로그인 시 회원 가입
     * 유저 인증 후 Jwt AccessToken 생성
     */

    @Transactional
    @Override
    public User login(KakaoOauthLoginParam params) {

        ClientRegistration provider = inMemoryRepository.findByRegistrationId(params.provider());
        log.info("redirect uri = {}", provider.getRedirectUri());
        OauthTokenResDto tokenResponse = getToken(params.code(), provider);
        log.info("OauthAccessToken = {}", tokenResponse.access_token());
        return getUserFromKakao(params.provider(), tokenResponse, provider);
    }

    private OauthTokenResDto getToken(String code, ClientRegistration provider) {
        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(requestToken(code, provider))
                .retrieve()
                .bodyToMono(OauthTokenResDto.class)
                .block();
    }

    private MultiValueMap<String, String> requestToken(String code, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("client_id", provider.getClientId());
        return formData;
    }

    private User getUserFromKakao(String providerName, OauthTokenResDto tokenResponse, ClientRegistration provider) {
        Map<String, Object> userAttributes = getUserAttribute(provider, tokenResponse);
        if (!providerName.equals("kakao")) {
            throw new RuntimeException("invalid provider name");
        }

        Oauth2UserInfo oauth2UserInfo = new KakaoUserInfo(userAttributes);
        if (oauth2UserInfo.getProvider() == null) {
            throw new RuntimeException("provider is null");
        }

        String email = oauth2UserInfo.getEmail();
        String nickname = oauth2UserInfo.getNickName();
        String imageUrl = oauth2UserInfo.getImageUrl();
        String oauth2Provider = oauth2UserInfo.getProvider();

        log.info("imageUrl={}", imageUrl);

        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isEmpty()) {
            User user = new User(email, nickname, imageUrl, oauth2Provider);
            userRepository.save(user);
            return user;
        }
        return optionalUser.get();
    }

    private Map<String, Object> getUserAttribute(ClientRegistration provider, OauthTokenResDto tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(String.valueOf(tokenResponse.access_token())))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }
}
