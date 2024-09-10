package com.kass.backend.security.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// convertir datos JSON que incluyen una propiedad llamada "authority"
// en instancias de SimpleGrantedAuthority,
// facilitando la deserialización de roles o permisos desde JSON.
public abstract class SimpleGrantedAuthorityJsonCreator {
    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role){

    }
}

