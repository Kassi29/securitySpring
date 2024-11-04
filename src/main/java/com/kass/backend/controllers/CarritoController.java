package com.kass.backend.controllers;

import com.kass.backend.models.CarritoModel;
import com.kass.backend.services.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<CarritoModel>> obtenerCarritosPorUsuario(@PathVariable int idUsuario) {
        List<CarritoModel> carritos = carritoService.listarCarritosPorUsuario(idUsuario);
        return ResponseEntity.ok(carritos);
    }

    @PostMapping
    public ResponseEntity<CarritoModel> crearCarrito(@RequestBody CarritoModel carrito) {
        try {
            CarritoModel nuevoCarrito = carritoService.guardarCarrito(carrito);
            return ResponseEntity.ok(nuevoCarrito);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Manejo de errores
        }
    }
    @PutMapping("/{idCarrito}")
    public ResponseEntity<CarritoModel> actualizarEstadoCarrito(@PathVariable int idCarrito, @RequestBody String nuevoEstado) {
        try {
            CarritoModel carritoActualizado = carritoService.actualizarEstadoCarrito(idCarrito, nuevoEstado);
            return ResponseEntity.ok(carritoActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Manejo de errores
        }
    }
    @GetMapping("/email/{deliveryEmail}") // Nuevo endpoint
    public ResponseEntity<List<CarritoModel>> obtenerCarritosPorEmail(@PathVariable String deliveryEmail) {
        List<CarritoModel> carritos = carritoService.listarCarritosPorEmail(deliveryEmail);
        return ResponseEntity.ok(carritos);
    }


}
