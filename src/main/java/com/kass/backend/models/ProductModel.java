package com.kass.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    String name;

    @Column( nullable = false)
    String description;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"),
            uniqueConstraints = { @UniqueConstraint(columnNames = {"product_id", "category_id"}) }
    )
    private List<CategoryModel> categories;



    @ManyToOne
    @JoinColumn(name = "almacen_id", nullable = false)
    private AlmacenModel almacen;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private double price;

    @ManyToOne // Relación con SellerRole
    @JoinColumn(name = "seller_role_id", nullable = false)
    private SellerRole seller;


    @Column(nullable = true)
    private String imageUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductModel that = (ProductModel) o;
        return id == that.id && stock == that.stock && Double.compare(price, that.price) == 0 && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(categories, that.categories) && Objects.equals(almacen, that.almacen) && Objects.equals(seller, that.seller) && Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, categories, almacen, stock, price, seller, imageUrl);
    }
}
