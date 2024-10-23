package com.kass.backend.validation.user;

import com.kass.backend.dto.DeliveryDTO;
import com.kass.backend.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class DeliveryValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return DeliveryDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DeliveryDTO deliveryDto = (DeliveryDTO) target;

        if (deliveryDto.getEmpresa() != null) {
            errors.rejectValue("empresaId", "field.required", "El campo empresa es obligatorio ");
        }


        // Puedes agregar más validaciones según sea necesario
    }
}