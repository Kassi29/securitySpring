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
    String location;

    @Column(nullable = false)
    double lat;

    @Column(nullable = false)
    double lng;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlmacenModel that = (AlmacenModel) o;
        return id == that.id && Double.compare(lat, that.lat) == 0 && Double.compare(lng, that.lng) == 0 && Objects.equals(name, that.name) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, lat, lng);
    }
}
