package com.kass.backend.services;

import com.kass.backend.models.RoleModel;
import com.kass.backend.models.UserModel;
import com.kass.backend.repositories.IRole;
import com.kass.backend.repositories.IUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final IUser iuser;
    private final IRole iRole;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUser iuser, PasswordEncoder passwordEncoder, IRole iRole) {
        this.iuser = iuser;
        this.passwordEncoder = passwordEncoder;
        this.iRole = iRole;
    }

    //lista todos los usuarios
    @Transactional
    public List<UserModel> getAllUsers() {
        return iuser.findAll();
    }

    //guardar usuario
    public UserModel save(UserModel user) {
        Set<RoleModel> roles = user.getRoles().stream()
                .map(role -> iRole.findByName(role.getName()).orElse(null))
                .filter(role -> role != null)
                .collect(Collectors.toSet());

        if (user.getAdmin()) {
            Optional<RoleModel> adminRole = iRole.findByName("ROLE_ADMIN");
            adminRole.ifPresent(roles::add);
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return iuser.save(user);
    }

    //existe ya el email?
    public boolean existsByEmail(String email){
        return iuser.existsByEmail(email);

    }


    @Transactional
    public Optional<UserModel> delete(int id) {
        Optional<UserModel> existingUser = iuser.findById(id);
        existingUser.ifPresent(iuser::delete);
        return existingUser;
    }





}
