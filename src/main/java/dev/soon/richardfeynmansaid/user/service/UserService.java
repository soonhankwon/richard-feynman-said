package dev.soon.richardfeynmansaid.user.service;

import dev.soon.richardfeynmansaid.user.controller.dto.SignupForm;
import dev.soon.richardfeynmansaid.user.domain.User;
import dev.soon.richardfeynmansaid.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signup(SignupForm form) {
        SignupForm encodedForm = new SignupForm(form.email(), bCryptPasswordEncoder.encode(form.password()));
        User user = new User(encodedForm);
        userRepository.save(user);
    }
}
