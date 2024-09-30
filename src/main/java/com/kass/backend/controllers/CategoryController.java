package com.kass.backend.controllers;


import com.kass.backend.models.CategoryModel;
import com.kass.backend.services.CategoryService;
import com.kass.backend.validation.user.category.CategoryValidation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryValidation categoryValidation;


    public CategoryController(final CategoryService categoryService, CategoryValidation categoryValidation) {
        this.categoryService = categoryService;
        this.categoryValidation = categoryValidation;

    }

    @GetMapping
    public List<CategoryModel> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> getCategoryById(@PathVariable int id) {
        Optional<CategoryModel> categoryModelOptional = categoryService.getCategoryById(id);
        return categoryModelOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryModel categoryModel, BindingResult bindingResult) {
        categoryValidation.validate(categoryModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(categoryModel));
    }

    private ResponseEntity<?> validation(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            errors.put("El campo " + fieldError.getField(), " ERROR:" + fieldError.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryModel categoryModel, BindingResult bindingResult) {
        categoryValidation.validate(categoryModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }

        try {
            CategoryModel updatedCategory = categoryService.update(id, categoryModel);
            return ResponseEntity.ok(updatedCategory);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        Optional<CategoryModel> categoryModelOptional = categoryService.getCategoryById(id);
        if (categoryModelOptional.isPresent()) {
            categoryService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}