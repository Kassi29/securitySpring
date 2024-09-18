package com.kass.backend.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private int id;
    private String name;
    private String lastname;
    private String email;
    private Boolean enabled;
    private Boolean admin;

    // Constructor for convenience
    public UserDto(int id, String name, String lastname, String email, Boolean enabled, Boolean admin) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.enabled = enabled;
        this.admin = admin;
    }


    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", id=" + id +
                ", enabled=" + enabled +
                ", email='" + email + '\'' +
                ", admin=" + admin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id == userDto.id && Objects.equals(name, userDto.name) && Objects.equals(lastname, userDto.lastname) && Objects.equals(email, userDto.email) && Objects.equals(enabled, userDto.enabled) && Objects.equals(admin, userDto.admin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastname, email, enabled, admin);
    }
}
