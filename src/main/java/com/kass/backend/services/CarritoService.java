package com.kass.backend.services;

import com.kass.backend.models.CarritoModel;
import com.kass.backend.repositories.ICarrito;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CarritoService {
    private ICarrito iCarrito;

    public CarritoService(ICarrito iCarrito) {
        this.iCarrito = iCarrito;
    }

    public List<CarritoModel> listarCarritosPorEmail(String deliveryEmail) {
        return iCarrito.findByDeliveryEmail(deliveryEmail); // Nuevo método
    }
    public CarritoModel guardarCarrito(CarritoModel carrito) {
        carrito.setEstado("En almacen");
        return iCarrito.save(carrito);
    }
    public List<CarritoModel> listarCarritosPorUsuario(int idUsuario) {
        return iCarrito.findByIdUser(idUsuario);
    }

    public CarritoModel actualizarEstadoCarrito(int idCarrito, String nuevoEstado) {
        CarritoModel carrito = iCarrito.findById(idCarrito).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        carrito.setEstado(nuevoEstado);
        return iCarrito.save(carrito);
    }



}
