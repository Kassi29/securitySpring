package com.kass.backend.controllers;

import com.kass.backend.services.ComunidadService;
import com.kass.backend.validation.ComunidadValidation;
import com.kass.backend.validation.user.category.CategoryValidation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kass.backend.models.ComunidadModel;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/communities")
public class ComunidadController {

    private final ComunidadService comunidadService;
    private final ComunidadValidation comunidadValidation;

    public ComunidadController( final ComunidadService comunidadService , ComunidadValidation comunidadValidation ){
        this.comunidadService=comunidadService;
        this.comunidadValidation=comunidadValidation;
    }
    @GetMapping
    public List<ComunidadModel> getAllComunidades() {
        return comunidadService.getAllComunidades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunidadModel> getComunidadById(@PathVariable int id) {
        Optional<ComunidadModel> comunidadModelOptional = comunidadService.getComunidadById(id);
        return comunidadModelOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<?> addComunidad(@Valid @RequestBody ComunidadModel comunidadModel, BindingResult bindingResult) {
        comunidadValidation.validate(comunidadModel, bindingResult); // Cambia esto si tienes validación específica
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(comunidadService.save(comunidadModel));
    }

    private ResponseEntity<?> validation(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> errors.put("El campo " + fieldError.getField(), " ERROR:" + fieldError.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComunidad(@PathVariable int id, @Valid @RequestBody ComunidadModel comunidadModel, BindingResult bindingResult) {
        comunidadValidation.validate(comunidadModel, bindingResult); // Cambia esto si tienes validación específica
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }

        try {
            ComunidadModel updatedComunidad = comunidadService.update(id, comunidadModel);
            return ResponseEntity.ok(updatedComunidad);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComunidad(@PathVariable int id) {
        Optional<ComunidadModel> comunidadModelOptional = comunidadService.getComunidadById(id);
        if (comunidadModelOptional.isPresent()) {
            comunidadService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
