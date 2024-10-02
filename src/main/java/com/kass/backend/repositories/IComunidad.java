package com.kass.backend.repositories;

import com.kass.backend.models.CategoryModel;
import com.kass.backend.models.ComunidadModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IComunidad extends JpaRepository<ComunidadModel, Integer> {
}
