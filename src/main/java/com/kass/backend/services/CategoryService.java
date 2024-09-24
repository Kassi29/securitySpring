package com.kass.backend.services;

import com.kass.backend.models.CategoryModel;
import com.kass.backend.repositories.ICategory;
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


    //eliminar
    @Transactional
    public Optional<CategoryModel> delete (int id){
        Optional<CategoryModel> categoryModelOptional = iCategory.findById(id);
        categoryModelOptional.ifPresent(iCategory::delete);
        return categoryModelOptional;
    }


}
