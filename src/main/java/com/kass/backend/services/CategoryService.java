package com.kass.backend.services;

import com.kass.backend.models.CategoryModel;
import com.kass.backend.repositories.ICategory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final ICategory iCategory;

    public CategoryService(ICategory iCategory) {
        this.iCategory = iCategory;
    }

    //listar
    @Transactional
    public List<CategoryModel> getAllCategories() {
        return iCategory.findAll();
    }

    //guardar
    public CategoryModel save(CategoryModel category) {
        return iCategory.save(category);
    }

    //ver
    public Optional<CategoryModel> getCategoryById(int id) {
        return iCategory.findById(id);
    }

    @Transactional
    public CategoryModel update(int id, CategoryModel categoryModel) {
        Optional<CategoryModel> existingCategoryOptional = getCategoryById(id);
        if (existingCategoryOptional.isPresent()) {
            CategoryModel existingCategory = existingCategoryOptional.get();
            existingCategory.setName(categoryModel.getName());
            existingCategory.setDescription(categoryModel.getDescription());
            return iCategory.save(existingCategory);
        }
        throw new EntityNotFoundException("Categoría con ID: " + id + " no encontrada");
    }



    //eliminar
    @Transactional
    public Optional<CategoryModel> delete (int id){
        Optional<CategoryModel> categoryModelOptional = iCategory.findById(id);
        categoryModelOptional.ifPresent(iCategory::delete);
        return categoryModelOptional;
    }


}
