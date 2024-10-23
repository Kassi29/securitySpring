package com.kass.backend.dto;

import com.kass.backend.models.ComunidadModel;
import com.kass.backend.models.UserModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SellerDTO {
    private int sellerRoleId; // ID del rol de vendedor
    private UserModel user; // Información del usuario
    private ComunidadModel comunidad;

    public SellerDTO(int sellerRoleId, UserModel user, ComunidadModel comunidad) {
        this.sellerRoleId = sellerRoleId;
        this.user = user;
        this.comunidad = comunidad;
    }
}
