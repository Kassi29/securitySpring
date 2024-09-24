package com.kass.backend.repositories;


import com.kass.backend.models.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategory  extends JpaRepository<CategoryModel, Integer> {
}
