package com.kass.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "almacenes")

public class AlmacenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    String name;

    @Column(nullable = false)
    String ubicacion;

    @Column(nullable = false)
    String departamento;

    @Column(nullable = false)
    double latitud;

    @Column(nullable = false)
    double longitud;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlmacenModel that = (AlmacenModel) o;
        return id == that.id && Double.compare(latitud, that.latitud) == 0 && Double.compare(longitud, that.longitud) == 0 && Objects.equals(name, that.name) && Objects.equals(ubicacion, that.ubicacion) && Objects.equals(departamento, that.departamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ubicacion, departamento, latitud, longitud);
    }
}
