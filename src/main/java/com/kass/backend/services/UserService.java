package com.kass.backend.services;

import com.kass.backend.dto.UserDto;
import com.kass.backend.models.*;
import com.kass.backend.repositories.IDelivery;
import com.kass.backend.repositories.IEmpresa;
import com.kass.backend.repositories.IRole;
import com.kass.backend.repositories.IUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final IUser iuser;
    private final IRole iRole;
    private final PasswordEncoder passwordEncoder;
    private final IEmpresa iEmpresa;
    private final IDelivery iDelivery;

    public UserService(IUser iuser, PasswordEncoder passwordEncoder, IRole iRole ,IEmpresa iEmpresa
    , IDelivery iDelivery) {
        this.iuser = iuser;
        this.passwordEncoder = passwordEncoder;
        this.iRole = iRole;
        this.iEmpresa = iEmpresa;
        this.iDelivery = iDelivery;
    }

    //lista todos los usuarios
    @Transactional
    public List<UserModel> getAllUsers() {
        return iuser.findAll();
    }



    public UserModel save(UserModel user) {
        Set<RoleModel> roles = user.getRoles().stream()
                .map(role -> iRole.findByName(role.getName()).orElse(null))
                .filter(Objects::nonNull)
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

    @Transactional
    public Optional<UserModel> findByEmail(String email) {
        return iuser.findByEmail(email);
    }


    public UserModel saveDelivery(UserModel user, int empresaId) {
        // Verifica que la empresa existe
        EmpresaModel empresa = iEmpresa.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        // Busca el rol DELIVERY existente
        RoleModel existingRole = iRole.findByName("ROLE_DELIVERY")
                .orElseThrow(() -> new IllegalArgumentException("Rol DELIVERY no encontrado"));

        // Guardar el usuario
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserModel savedUser = iuser.save(user);

        // Crear y guardar la relación DeliveryRole
        DeliveryRole deliveryRole = new DeliveryRole(savedUser, empresa);
        iDelivery.save(deliveryRole); // Asegúrate de tener un repositorio para DeliveryRole

        // Agregar el rol al usuario y guardarlo
        Set<RoleModel> roles = new HashSet<>();
        roles.add(existingRole);
        savedUser.setRoles(roles); // Esto crea un Set mutable

        return iuser.save(savedUser); // Guarda el usuario nuevamente para persistir los roles
    }


    @Transactional
    public UserModel update(int id, UserDto userDto) {
        // Verificar si el email ya está en uso
        Optional<UserModel> existingUserWithEmail = iuser.findByEmail(userDto.getEmail());
        if (existingUserWithEmail.isPresent() && existingUserWithEmail.get().getId() != id) {
            throw new IllegalArgumentException("El email ya está en uso por otro usuario.");
        }

        // Encontrar el usuario existente
        Optional<UserModel> existingUser = iuser.findById(id);
        if (existingUser.isPresent()) {
            UserModel existingUserModel = existingUser.get();

            // Actualizar los campos
            existingUserModel.setName(userDto.getName());
            existingUserModel.setLastname(userDto.getLastname());
            existingUserModel.setEmail(userDto.getEmail());

            return iuser.save(existingUserModel);
        }

        throw new EntityNotFoundException("Usuario con ID: " + id + " no encontrado");
    }


    //cambio
    public boolean checkCurrentPassword(String email, String currentPassword) {
        Optional<UserModel> user = iuser.findByEmail(email);
        return user.isPresent() && passwordEncoder.matches(currentPassword, user.get().getPassword());
    }

    public void changePassword(Integer userId, String newPassword) {
        Optional<UserModel> userOptional = iuser.findById(userId);
        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            iuser.save(user);
        }
    }


    //USUARIO X ROL
    public List<UserModel> getUsersByRole(String roleName) {
        return iuser.findAll((root, query, criteriaBuilder) -> {
            // Unir la tabla de roles con la de usuarios
            Join<UserModel, RoleModel> roles = root.join("roles");
            // Filtrar por el nombre del rol
            return criteriaBuilder.equal(roles.get("name"), roleName);
        });
    }








}
