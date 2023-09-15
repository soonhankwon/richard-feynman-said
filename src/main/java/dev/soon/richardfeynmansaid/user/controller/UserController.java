package dev.soon.richardfeynmansaid.user.controller;

import dev.soon.richardfeynmansaid.user.controller.dto.SignupForm;
import dev.soon.richardfeynmansaid.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
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
    public String save(@Validated @ModelAttribute SignupForm form, BindingResult bindingResult) {
        if(!form.password().equals(form.passwordConfirm())) {
            bindingResult.reject("passwordConfirm");
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "users/signupForm";
        }
        userService.signup(form);
        return "redirect:/";
    }
}
