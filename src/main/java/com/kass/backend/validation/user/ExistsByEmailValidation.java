package com.kass.backend.validation.user;

import com.kass.backend.services.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistsByEmailValidation implements ConstraintValidator<ExistsByEmail, String> {

    private final UserService userService;


    public ExistsByEmailValidation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !userService.existsByEmail(email);
    }
}
