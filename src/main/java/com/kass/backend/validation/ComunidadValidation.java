package com.kass.backend.validation;

import com.kass.backend.models.ComunidadModel;
import com.kass.backend.models.EmpresaModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ComunidadValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ComunidadModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ComunidadModel categoryModel = (ComunidadModel) target;

        if(categoryModel.getName() == null || categoryModel.getName().trim().isEmpty()) {
            errors.rejectValue("name", "category.name.required","El campo nombre es obligatorio.");
        }

        if(categoryModel.getLocation() == null || categoryModel.getLocation().trim().isEmpty()) {
            errors.rejectValue("ubicacion", "field.description.required","El campo descripcion es obligatorio.");
        }

    }

}
