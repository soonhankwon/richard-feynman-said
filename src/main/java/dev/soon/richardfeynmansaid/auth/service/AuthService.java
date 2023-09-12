package dev.soon.richardfeynmansaid.auth.service;

import dev.soon.richardfeynmansaid.security.service.JwtService;
import dev.soon.richardfeynmansaid.user.domain.User;
import dev.soon.richardfeynmansaid.user.repository.UserRepository;
import dev.soon.richardfeynmansaid.auth.controller.dto.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void login(LoginForm form, HttpServletResponse response) {
        log.info("email={} password={}", form.email(), form.password());
        User user = userRepository.findUserByEmail(form.email())
                .orElseThrow(() -> new IllegalArgumentException("no exist email"));

        if(!user.isPasswordValid(form, bCryptPasswordEncoder))
            throw new IllegalArgumentException("password invalid");

        String token = jwtService.generateToken(form.email());
        log.info("token={}", token);
        Cookie cookie = new Cookie("AccessToken", token);
        response.addCookie(cookie);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> optionalCookie = Arrays.stream(request.getCookies())
                .filter(i -> i.getName().equals("AccessToken"))
                .findFirst();

        if(optionalCookie.isPresent()) {
            Cookie cookie = optionalCookie.get();
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}
