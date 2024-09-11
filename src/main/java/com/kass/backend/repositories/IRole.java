package com.kass.backend.repositories;

import com.kass.backend.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")
public interface IRole extends JpaRepository<RoleModel, Integer> {

    //busac segun el nombre
    Optional<RoleModel> findByName(String name);
}

