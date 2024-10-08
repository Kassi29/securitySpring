package com.kass.backend.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kass.backend.validation.user.ExistsByEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El campo nombre no puede estar vacío.")
    private String name;

    @NotBlank(message = "El campo apellido no puede estar vacío.")
    private String lastname;

    @ExistsByEmail(message = "El correo electrónico ya existe.")
    @NotBlank(message = "El campo correo electrónico no puede estar vacío.")
    @Email(message = "El correo electrónico debe ser válido.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "El campo contraseña no puede estar vacío.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Boolean enabled = true;

    //boolena si es admin o no
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean admin = false;

    @JsonIgnoreProperties({"users","handler","hibernateLazyInitializer"})
    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"),
            uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "rol_id"})}
    )
    private Set<RoleModel> roles;


    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<ProductModel> products;


    public UserModel(){
        this.roles = new HashSet<RoleModel>();
    }
    public UserModel(String name, String lastname, String email, String password, Boolean enabled, Boolean admin) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return id == userModel.id && Objects.equals(name, userModel.name) && Objects.equals(lastname, userModel.lastname) && Objects.equals(email, userModel.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastname, email, password, enabled, admin, roles, products);
    }
}
