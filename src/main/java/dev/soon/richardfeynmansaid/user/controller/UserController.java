package dev.soon.richardfeynmansaid.user.controller;

import dev.soon.richardfeynmansaid.user.controller.dto.SignupForm;
import dev.soon.richardfeynmansaid.user.service.UserService;
import dev.soon.richardfeynmansaid.web.validation.SignupFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {

    private final SignupFormValidator validator;
    private final UserService userService;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(validator);
    }

    @GetMapping("/signup")
    public String addForm(@ModelAttribute("signupForm") SignupForm form) {
        return "users/signupForm";
    }

    @PostMapping("/signup")
    public String save(@Validated @ModelAttribute SignupForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "users/signupForm";
        }
        userService.signup(form);
        return "redirect:/";
    }
}
