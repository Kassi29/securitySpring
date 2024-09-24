package com.kass.backend.validation.almacen;

import com.kass.backend.models.AlmacenModel;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class AlmacenValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AlmacenModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AlmacenModel almacenModel = (AlmacenModel) target;


        // Validación del nombre
        if (almacenModel.getName() == null || almacenModel.getName().trim().isEmpty()) {
            errors.rejectValue("name", "almacen.name.required", "El campo nombre es obligatorio.");
        }

        // Validación de la ubicación
        if (almacenModel.getLocation() == null || almacenModel.getLocation().trim().isEmpty()) {
            errors.rejectValue("location", "almacen.location.required", "El campo ubicación es obligatorio.");
        }

        // Validación de la latitud
        if (almacenModel.getLat() == 0) {
            errors.rejectValue("lat", "almacen.lat.required", "La latitud debe ser válida.");
        }

        // Validación de la longitud
        if (almacenModel.getLng() == 0) {
            errors.rejectValue("lng", "almacen.lng.required", "La longitud debe ser válida.");
        }
    }
}
