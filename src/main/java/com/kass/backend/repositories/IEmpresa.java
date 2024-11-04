package com.kass.backend.repositories;

import com.kass.backend.models.EmpresaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IEmpresa extends JpaRepository<EmpresaModel, Integer> {
    List<EmpresaModel> findByLocation(String location);
}
