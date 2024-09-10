package com.kass.backend.validation.user;

import com.kass.backend.models.UserModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
            UserModel user = (UserModel) target;


        if (user.getName() == null || user.getName().trim().isEmpty()) {
            errors.rejectValue("name", "field.required", "El campo nombre es obligatorio.");
        }

        if (user.getLastname() == null || user.getLastname().trim().isEmpty()) {
            errors.rejectValue("lastname", "field.required", "El campo apellido es obligatorio.");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "field.required", "El campo correo electrónico es obligatorio.");
        } else if (!user.getEmail().matches("^[\\w-.]+@[\\w-.]+\\.[a-zA-Z]{2,6}$")) {
            errors.rejectValue("email", "field.invalid", "El correo electrónico debe ser válido.");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            errors.rejectValue("password", "field.required", "El campo contraseña es obligatorio.");
        }

        if (user.getAdmin() == null) {
            errors.rejectValue("admin", "field.required", "El campo admin es obligatorio.");
        }

        if (user.getEnabled() == null) {
            errors.rejectValue("enabled", "field.required", "El campo enabled es obligatorio.");
        }
    }
}


