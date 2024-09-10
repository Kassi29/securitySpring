package com.kass.backend.services;

import com.kass.backend.models.RoleModel;
import com.kass.backend.models.UserModel;
import com.kass.backend.repositories.IRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    private final IRole iRole;

    public RoleService(IRole iRole) {
        this.iRole = iRole;
    }

    //lista todos los roles
    @Transactional
    public List<RoleModel> getAllRoles() {
        return iRole.findAll();
    }
}
