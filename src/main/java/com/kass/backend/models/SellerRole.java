package com.kass.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "seller_roles")
public class SellerRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "comunidad_id", nullable = false)
    private ComunidadModel comunidad;


    public SellerRole(UserModel user){
        this.user = user;
    }

    public SellerRole(UserModel user , ComunidadModel comunidad) {
        this.user = user;
        this.comunidad = comunidad;

    }

}