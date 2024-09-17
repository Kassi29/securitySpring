package com.kass.backend.validation.user;

import com.kass.backend.services.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistsByEmailValidation implements ConstraintValidator<ExistsByEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || userService == null) {
            return true;
        }
        return !userService.existsByEmail(email);
    }
}
