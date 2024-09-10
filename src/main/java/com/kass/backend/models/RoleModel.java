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
@Table(name = "roles")
public class RoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    String name;

    @JsonIgnoreProperties({"roles","handler","hibernateLazyInitializer"})
    @ManyToMany(mappedBy = "roles")
    private List<UserModel> users;


    public RoleModel(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleModel roleModel = (RoleModel) o;
        return id == roleModel.id && Objects.equals(name, roleModel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
