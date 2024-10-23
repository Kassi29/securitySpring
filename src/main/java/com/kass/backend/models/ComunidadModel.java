package com.kass.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comunidades")
public class ComunidadModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El campo nombre no puede estar vacío.")
    private String name;

    @NotBlank(message = "El campo nombre no puede estar vacío.")
    private String location;


}
