package dev.soon.richardfeynmansaid.auth.controller;

import dev.soon.richardfeynmansaid.auth.controller.dto.KakaoOauthLoginParam;
import dev.soon.richardfeynmansaid.auth.controller.dto.LoginForm;
import dev.soon.richardfeynmansaid.auth.controller.dto.NaverOauthLoginParam;
import dev.soon.richardfeynmansaid.auth.service.AuthService;
import dev.soon.richardfeynmansaid.auth.service.OauthService;
import dev.soon.richardfeynmansaid.security.service.JwtService;
import dev.soon.richardfeynmansaid.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthController {

    private final OauthService<KakaoOauthLoginParam> kakaoOauthService;
    private final OauthService<NaverOauthLoginParam> naverOauthService;
    private final AuthService authService;
    private final JwtService jwtService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm form, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectUrl, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "/login/loginForm";
        }

        authService.login(form, response);
        return "redirect:" + redirectUrl;
    }

    /**
     * OAuth 로그인 시 인증 코드를 넘겨받은 후 첫 로그인 시 회원 가입
     */
    @GetMapping("/oauth/{provider}")
    public String oauthCallback(@PathVariable String provider, HttpServletResponse response,
                                @RequestParam String code, @RequestParam(required = false) String state,
                                @RequestParam(defaultValue = "/") String redirectUrl) {
        log.info("code={}", code);
        if(provider.equals("kakao")) {
            User loginUser = kakaoOauthService.login(new KakaoOauthLoginParam(provider, code));
            String accessToken = jwtService.generateToken(loginUser.getEmail());
            Cookie cookie = new Cookie("AccessToken", accessToken);
            cookie.setPath("/");
            response.addCookie(cookie);
            log.info("cookie={}", cookie.getValue());
            return "redirect:" + redirectUrl;
        }
        if(provider.equals("naver")) {
            User loginUser = naverOauthService.login(new NaverOauthLoginParam(provider, code, state));
            String accessToken = jwtService.generateToken(loginUser.getEmail());
            Cookie cookie = new Cookie("AccessToken", accessToken);
            cookie.setPath("/");
            response.addCookie(cookie);
            log.info("cookie={}", cookie.getValue());
            return "redirect:" + redirectUrl;
        }
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        authService.logout(request, response);
        return "redirect:/";
    }
}
