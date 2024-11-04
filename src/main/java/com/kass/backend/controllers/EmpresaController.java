package com.kass.backend.controllers;

import com.kass.backend.models.EmpresaModel;
import com.kass.backend.services.EmpresaService;
import com.kass.backend.validation.EmpresaValidation;
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
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;
    private final EmpresaValidation empresaValidation;
    public EmpresaController(EmpresaService empresaService, EmpresaValidation empresaValidation) {
        this.empresaService = empresaService;
        this.empresaValidation = empresaValidation;
    }

    @GetMapping
    public List<EmpresaModel> getAllEmpresas() {
        return empresaService.getAllEmpresas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaModel> getEmpresaById(@PathVariable int id) {
        Optional<EmpresaModel> empresaModelOptional = empresaService.getEmpresabyId(id);
        return empresaModelOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<?> addEmpresa(@Valid @RequestBody EmpresaModel empresaModel, BindingResult bindingResult) {
        empresaValidation.validate(empresaModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaService.save(empresaModel));
    }

    private ResponseEntity<?> validation(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> errors.put("El campo " + fieldError.getField(), " ERROR:" + fieldError.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmpresa(@PathVariable int id, @Valid @RequestBody EmpresaModel empresaModel, BindingResult bindingResult) {
        empresaValidation.validate(empresaModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }

        try {
            EmpresaModel updatedEmpresa = empresaService.update(id, empresaModel);
            return ResponseEntity.ok(updatedEmpresa);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable int id) {
        Optional<EmpresaModel> empresaModelOptional = empresaService.getEmpresabyId(id);
        if (empresaModelOptional.isPresent()) {
            empresaService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/by-location/{location}")
    public List<EmpresaModel> getEmpresasByLocation(@PathVariable String location) {
        return empresaService.getEmpresasByLocation(location);
    }



}
