package dev.soon.richardfeynmansaid.user.controller;

import dev.soon.richardfeynmansaid.user.controller.dto.SignupForm;
import dev.soon.richardfeynmansaid.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String addForm(@ModelAttribute("signupForm") SignupForm form) {
        return "users/signupForm";
    }

    @PostMapping("/signup")
    public String save(@ModelAttribute SignupForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/signupForm";
        }
        userService.signup(form);
        return "redirect:/";
    }
}
