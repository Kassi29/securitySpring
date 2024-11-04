package com.kass.backend.repositories;

import com.kass.backend.models.CarritoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICarrito extends JpaRepository<CarritoModel, Integer> {
    List<CarritoModel> findByIdUser(int idUser);
    List<CarritoModel> findByDeliveryEmail(String deliveryEmail);
}
