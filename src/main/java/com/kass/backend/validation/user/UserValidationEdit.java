package com.kass.backend.validation.user;

import com.kass.backend.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidationEdit implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;

        if (userDto.getName() == null || userDto.getName().trim().isEmpty()) {
            errors.rejectValue("name", "field.required", "El campo nombre es obligatorio.");
        }

        if (userDto.getLastname() == null || userDto.getLastname().trim().isEmpty()) {
            errors.rejectValue("lastname", "field.required", "El campo apellido es obligatorio.");
        }

        if (userDto.getEmail() == null || userDto.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "field.required", "El campo correo electrónico es obligatorio.");
        } else if (!userDto.getEmail().matches("^[\\w-.]+@[\\w-.]+\\.[a-zA-Z]{2,6}$")) {
            errors.rejectValue("email", "field.invalid", "El correo electrónico debe ser válido.");
        }

        // Puedes agregar más validaciones según sea necesario
    }
}
