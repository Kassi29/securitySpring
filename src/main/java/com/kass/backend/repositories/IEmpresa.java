package com.kass.backend.repositories;

import com.kass.backend.models.EmpresaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmpresa extends JpaRepository<EmpresaModel, Integer> {
}
