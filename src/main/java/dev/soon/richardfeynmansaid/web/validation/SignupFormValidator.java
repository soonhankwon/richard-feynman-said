package dev.soon.richardfeynmansaid.web.validation;

import dev.soon.richardfeynmansaid.user.controller.dto.SignupForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SignupFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SignupForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignupForm form = (SignupForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "required");

        if(!form.email().contains("@")) {
            errors.reject("invalid.email.format");
        }

        if(!form.password().equals(form.passwordConfirm())) {
            errors.reject("invalid.password.confirm");
        }
    }
}
