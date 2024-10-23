package com.kass.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "empresa")
public class EmpresaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El campo nombre no puede estar vacío.")
    private String name;

    @NotBlank(message = "El campo nombre no puede estar vacío.")
    private String location;

    @JsonIgnore
    @OneToMany(mappedBy = "empresa")
    private List<DeliveryRole> deliveries; // Relación inversa
}
