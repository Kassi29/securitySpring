package com.kass.backend.repositories;

import com.kass.backend.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRole extends JpaRepository<RoleModel, Integer> {

    //busac segun el nombre
    Optional<RoleModel> findByName(String name);
}

