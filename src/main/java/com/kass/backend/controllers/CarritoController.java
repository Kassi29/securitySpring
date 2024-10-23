package com.kass.backend.controllers;

import com.kass.backend.models.CarritoModel;
import com.kass.backend.services.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping
    public ResponseEntity<CarritoModel> crearCarrito(@RequestBody CarritoModel carrito) {
        try {
            // Aquí podrías realizar alguna conversión si es necesario
            CarritoModel nuevoCarrito = carritoService.guardarCarrito(carrito);
            return ResponseEntity.ok(nuevoCarrito);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Manejo de errores
        }
    }

}
