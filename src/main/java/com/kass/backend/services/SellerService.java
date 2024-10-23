package com.kass.backend.services;


import com.kass.backend.dto.SellerDTO;
import com.kass.backend.models.ComunidadModel;
import com.kass.backend.models.SellerRole;
import com.kass.backend.models.UserModel;
import com.kass.backend.repositories.IComunidad;
import com.kass.backend.repositories.ISeller;
import com.kass.backend.repositories.IUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerService {
    private final ISeller sellerRoleRepository; // Tu repositorio para SellerRole
    private final IUser userRepository; // Repositorio para obtener usuarios
    private final IComunidad comunidadRepository; // Repositorio para obtener comunidades

    public SellerService(ISeller sellerRoleRepository, IUser userRepository, IComunidad comunidadRepository) {
        this.sellerRoleRepository = sellerRoleRepository;
        this.userRepository = userRepository;
        this.comunidadRepository = comunidadRepository;
    }

    public List<SellerDTO> getAllSellerRoles() {
        return sellerRoleRepository.findAll().stream()
                .map(this::mapToDetails)
                .collect(Collectors.toList());
    }

    private SellerDTO mapToDetails(SellerRole sellerRole) {
        UserModel user = userRepository.findById(sellerRole.getUser().getId()).orElse(null);
        ComunidadModel comunidad = comunidadRepository.findById(sellerRole.getComunidad().getId()).orElse(null);

        return new SellerDTO(
                sellerRole.getId(),
                user,
                comunidad
        );
    }

   /* public void deleteSellerById(int id) {
        sellerRoleRepository.deleteById(id);
    }

    */

    @Transactional
    public void deleteSellersByUserId(int userId) {
        sellerRoleRepository.deleteByUserId(userId); // Implementa este método en tu repositorio
    }

    @Transactional
    public void deleteSellerById(int userId) {
        // Primero eliminamos el SellerRole asociado
        sellerRoleRepository.deleteByUserId(userId);

        // Luego eliminamos el usuario
        userRepository.deleteById(userId);
    }
}
