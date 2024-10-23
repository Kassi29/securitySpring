package com.kass.backend.services;

import com.kass.backend.models.CarritoModel;
import com.kass.backend.repositories.ICarrito;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarritoService {
    private ICarrito iCarrito;

    public CarritoService(ICarrito iCarrito) {
        this.iCarrito = iCarrito;
    }
    public CarritoModel guardarCarrito(CarritoModel carrito) {
        return iCarrito.save(carrito);
    }


}
