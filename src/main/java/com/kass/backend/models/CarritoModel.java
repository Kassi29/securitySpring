package com.kass.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "carritos")
@Getter
@Setter
@NoArgsConstructor
public class CarritoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_user")
    private int idUser;

    @ElementCollection
    private List<ProductItem> productos;

    @Column(name = "estado")
    private String estado;

    @Column(name = "departamento")
    private String departamento;

    @Column(name = "latitud")
    private String latitud;

    @Column(name = "longitud")
    private String longitud;

    @Column(name = "delivery_email")
    private String deliveryEmail;
    @Column(name = "fecha_entrega") // Agrega esta línea
    private String fechaEntrega; // Agrega este campo

    @Column(name = "horario") // Agrega esta línea
    private String horario; // Agrega este campo

    @Column(name= "totalEnvio")
    private String totalEnvio;
}
