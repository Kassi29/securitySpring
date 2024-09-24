package com.kass.backend.validation.user.category;


import com.kass.backend.models.CategoryModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryModel categoryModel = (CategoryModel) target;

        if(categoryModel.getName() == null || categoryModel.getName().trim().isEmpty()) {
            errors.rejectValue("name", "category.name.required","El campo nombre es obligatorio.");
        }

        if(categoryModel.getDescription() == null || categoryModel.getDescription().trim().isEmpty()) {
            errors.rejectValue("description", "field.description.required","El campo descripcion es obligatorio.");
        }

    }
}
