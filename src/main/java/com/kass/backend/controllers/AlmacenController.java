package com.kass.backend.controllers;

import com.kass.backend.models.AlmacenModel;
import com.kass.backend.services.AlmacenService;
import com.kass.backend.validation.almacen.AlmacenValidator;
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
@RequestMapping("/almacen")
public class AlmacenController {

    private final AlmacenService almacenService;
    private final AlmacenValidator validator;

    public AlmacenController(AlmacenService almacenService, AlmacenValidator almacenValidator) {
        this.almacenService = almacenService;
        this.validator = almacenValidator;
    }

    @GetMapping
    public List<AlmacenModel> getAllAlmacenes(){
        return almacenService.getAllAlmacen();
    }



    @GetMapping("/{id}")
    public ResponseEntity<AlmacenModel> getAlmacenById(@PathVariable int id) {
        Optional<AlmacenModel> categoryModelOptional = almacenService.findById(id);
        return categoryModelOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<?> addAlmacen(@Valid @RequestBody AlmacenModel almacenModel, BindingResult bindingResult) {
        validator.validate(almacenModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(almacenService.save(almacenModel));
    }

    private ResponseEntity<?> validation(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> errors.put("El campo " + fieldError.getField(), " ERROR:" + fieldError.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlmacen(@PathVariable int id) {
        Optional<AlmacenModel> categoryModelOptional = almacenService.findById(id);
        if (categoryModelOptional.isPresent()) {
            almacenService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable int id, @Valid @RequestBody AlmacenModel almacenModel, BindingResult bindingResult) {
        validator.validate(almacenModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }

        try {
           AlmacenModel updatedAlmcen = almacenService.update(id,almacenModel);
            return ResponseEntity.ok(updatedAlmcen);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
