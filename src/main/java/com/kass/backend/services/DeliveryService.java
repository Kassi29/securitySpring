package com.kass.backend.services;

import com.kass.backend.dto.DeliveryDTO;
import com.kass.backend.models.DeliveryRole;

import com.kass.backend.models.EmpresaModel;
import com.kass.backend.models.UserModel;
import com.kass.backend.repositories.IDelivery;
import com.kass.backend.repositories.IUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service

public class DeliveryService {

    private final IDelivery deliveryRoleRepository;
    private EmpresaService empresaService;
    private IUser userRepository;

    public DeliveryService(IDelivery roleRepository , EmpresaService empresaService, IUser userRepository) {
        this.deliveryRoleRepository = roleRepository;
        this.empresaService = empresaService;
        this.userRepository = userRepository;
    }

    public List<DeliveryDTO> getAllDeliveryRoles() {
        return deliveryRoleRepository.findAll().stream()
                .map(this::mapToDetails)
                .collect(Collectors.toList());
    }

    private DeliveryDTO mapToDetails(DeliveryRole deliveryRole) {
        Optional<UserModel> userModelOptional = userRepository.findById(deliveryRole.getUser().getId()); // Asegúrate de que DeliveryRole tenga el método getUser
        Optional<EmpresaModel> empresaModelOptional = empresaService.getEmpresabyId(deliveryRole.getEmpresa().getId()); // Asegúrate de que DeliveryRole tenga el método getEmpresa

        return new DeliveryDTO(
                deliveryRole.getId(),
                userModelOptional.orElse(null),
                empresaModelOptional.orElse(null)
        );
    }

    public void deleteDeliveryById(int id) {
        deliveryRoleRepository.deleteById(id);
    }

    @Transactional
    public void deleteDeliveriesByUserId(int userId) {
        // Aquí, usa tu repositorio para eliminar todos los DeliveryRoles asociados
        deliveryRoleRepository.deleteByUserId(userId);
    }

    public List<UserModel> getUsersByEmpresaId(int empresaId) {
        List<DeliveryRole> deliveryRoles = deliveryRoleRepository.findByEmpresaId(empresaId);
        return deliveryRoles.stream()
                .map(deliveryRole -> deliveryRole.getUser()) // Obtiene el usuario de cada DeliveryRole
                .distinct() // Asegúrate de que los usuarios sean únicos
                .collect(Collectors.toList());
    }

}
