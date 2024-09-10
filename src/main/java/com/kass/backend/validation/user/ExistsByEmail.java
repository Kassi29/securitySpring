package com.kass.backend.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExistsByEmailValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByEmail {
    String message() default "El correo electrónico proporcionado ya existe.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
