package com.kass.backend.services;


import com.kass.backend.models.EmpresaModel;
import com.kass.backend.repositories.IEmpresa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    private final IEmpresa iEmpresa;

    public EmpresaService(IEmpresa iEmpresa) {
        this.iEmpresa = iEmpresa;
    }

    @Transactional
    public List<EmpresaModel> getAllEmpresas() {
        return iEmpresa.findAll();
    }


    public EmpresaModel save(EmpresaModel empresa) {
        return iEmpresa.save(empresa);
    }

    public Optional<EmpresaModel> getEmpresabyId(int id) {
        return iEmpresa.findById(id);
    }

    @Transactional
    public EmpresaModel update(int id, EmpresaModel empresa) {
        Optional<EmpresaModel> empresaOptional = getEmpresabyId(id);
        if (empresaOptional.isPresent()) {
            EmpresaModel empresaToUpdate = empresaOptional.get();
            empresaToUpdate.setName(empresa.getName());
            empresaToUpdate.setLocation(empresa.getLocation());
            return iEmpresa.save(empresaToUpdate);
        }
        throw new RuntimeException("No se encontro el id de la empresa");
    }


    @Transactional
    public Optional<EmpresaModel> delete(int id) {
        Optional<EmpresaModel> empresaOptional = iEmpresa.findById(id);
        empresaOptional.ifPresent(iEmpresa::delete);
        return empresaOptional;

    }
    public List<EmpresaModel> getEmpresasByLocation(String location) {
        return iEmpresa.findByLocation(location);
    }


}
