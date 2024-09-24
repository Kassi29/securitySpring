package com.kass.backend.repositories;

import com.kass.backend.models.AlmacenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAlmacen  extends JpaRepository<AlmacenModel, Integer> {
}
