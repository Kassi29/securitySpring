package com.kass.backend.models;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class ProductItem {
    private int id_producto; // ID del producto
    private int cantidad; // Cantidad
}