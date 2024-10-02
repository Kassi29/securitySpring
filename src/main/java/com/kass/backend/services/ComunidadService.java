package com.kass.backend.services;

import com.kass.backend.models.ComunidadModel;
import com.kass.backend.repositories.IComunidad;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ComunidadService {
    private final IComunidad iComunidad;

    public ComunidadService(IComunidad iComunidad){
        this.iComunidad = iComunidad;
    }
    // Listar todas las comunidades
    @Transactional
    public List<ComunidadModel> getAllComunidades() {
        return iComunidad.findAll();
    }

    // Guardar una nueva comunidad
    public ComunidadModel save(ComunidadModel comunidad) {
        return iComunidad.save(comunidad);
    }

    // Ver comunidad por ID
    public Optional<ComunidadModel> getComunidadById(int id) {
        return iComunidad.findById(id);
    }

    // Actualizar comunidad
    @Transactional
    public ComunidadModel update(int id, ComunidadModel comunidadModel) {
        Optional<ComunidadModel> existingComunidadOptional = getComunidadById(id);
        if (existingComunidadOptional.isPresent()) {
            ComunidadModel existingComunidad = existingComunidadOptional.get();
            existingComunidad.setName(comunidadModel.getName());
            existingComunidad.setLocation(comunidadModel.getLocation());
            // Asegúrate de que haya otros campos que quieras actualizar
            return iComunidad.save(existingComunidad);
        }
        throw new EntityNotFoundException("Comunidad con ID: " + id + " no encontrada");
    }

    // Eliminar comunidad
    @Transactional
    public Optional<ComunidadModel> delete(int id) {
        Optional<ComunidadModel> comunidadModelOptional = iComunidad.findById(id);
        comunidadModelOptional.ifPresent(iComunidad::delete);
        return comunidadModelOptional;
    }
}
