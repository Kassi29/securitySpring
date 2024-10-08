package com.kass.backend.validation;

import com.kass.backend.models.ProductModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductModel productModel = (ProductModel) target;

        // Validar nombre
        if (productModel.getName() == null || productModel.getName().trim().isEmpty()) {
            errors.rejectValue("name", "product.name.required", "El campo nombre es obligatorio.");
        }

        // Validar precio
        if (productModel.getPrice() <= 0) {
            errors.rejectValue("price", "product.price.invalid", "El precio debe ser mayor que cero.");
        }

        // Validar stock
        if (productModel.getStock() < 0) {
            errors.rejectValue("stock", "product.stock.invalid", "El stock no puede ser negativo.");
        }

        // Validar categorías
        if (productModel.getCategories() == null || productModel.getCategories().isEmpty()) {
            errors.rejectValue("categories", "product.categories.required", "Al menos una categoría es obligatoria.");
        } else if (productModel.getCategories().size() > 2) {
            errors.rejectValue("categories", "product.categories.limit", "Un producto no puede tener más de dos categorías.");
        }

        // Validar almacén
        if (productModel.getAlmacen() == null) {
            errors.rejectValue("almacen", "product.almacen.required", "El almacén es obligatorio.");
        }


    }
}
