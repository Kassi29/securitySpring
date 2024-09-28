package com.kass.backend.services;


import com.kass.backend.models.AlmacenModel;
import com.kass.backend.repositories.IAlmacen;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlmacenService {

    private final IAlmacen iAlmacen;

    public AlmacenService(IAlmacen iAlmacen) {
        this.iAlmacen = iAlmacen;
    }

    @Transactional
    public List<AlmacenModel> getAllAlmacen() {
        return iAlmacen.findAll();
    }

    public AlmacenModel save(AlmacenModel almacenModel) {
        return iAlmacen.save(almacenModel);
    }


    public Optional<AlmacenModel> findById(int id) {
        return iAlmacen.findById(id);
    }

    @Transactional
    public AlmacenModel update(int id,AlmacenModel almacenModel) {
        Optional<AlmacenModel> almacenModelOptional = iAlmacen.findById(id);
        if (almacenModelOptional.isPresent()) {
            AlmacenModel almacenModel1 = almacenModelOptional.get();
            almacenModel1.setId(id);
            almacenModel1.setName(almacenModel.getName());
            almacenModel1.setUbicacion(almacenModel.getUbicacion());
            almacenModel1.setDepartamento(almacenModel.getDepartamento());
            almacenModel1.setLatitud(almacenModel.getLatitud());
            almacenModel1.setLongitud(almacenModel.getLongitud());
            return iAlmacen.save(almacenModel1);

        }
        throw new EntityNotFoundException("Almacen con ID: " + id+ " no encontrado");
    }

    @Transactional
    public Optional<AlmacenModel> delete(int id) {
        Optional<AlmacenModel> almacenModelOptional = iAlmacen.findById(id);
        almacenModelOptional.ifPresent(iAlmacen::delete);
        return almacenModelOptional;
    }

}
