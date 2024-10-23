package com.kass.backend.repositories;

import com.kass.backend.models.CarritoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICarrito extends JpaRepository<CarritoModel, Integer> {
}
