package dev.soon.richardfeynmansaid.web;

import dev.soon.richardfeynmansaid.idea.domain.Idea;
import dev.soon.richardfeynmansaid.idea.service.IdeaService;
import dev.soon.richardfeynmansaid.security.TokenStatus;
import dev.soon.richardfeynmansaid.security.service.JwtService;
import dev.soon.richardfeynmansaid.user.domain.User;
import dev.soon.richardfeynmansaid.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final IdeaService ideaService;

    @GetMapping("/")
    public String home(@CookieValue(name = "AccessToken", required = false) String token, Model model) {
        log.info("cookie value={}", token);
        if(token == null) {
            return "home";
        }
        if(jwtService.validateToken(token) != TokenStatus.VALID) {
            return "home";
        }
        String email = jwtService.getEmailFromToken(token);
        User user = userRepository.findUserByEmail(email).get();

        List<Idea> ideas = ideaService.findAllIdeas();
        model.addAttribute("user", user);
        model.addAttribute("ideas", ideas);
        return "loginHomeV2";
    }
}
