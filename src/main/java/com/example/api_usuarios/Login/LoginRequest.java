package com.example.api_usuarios.Login;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class LoginRequest {

    // Nombres del gateway. Se aceptan tambien los del DTO loginBody ya existente.
    @JsonAlias("correo_elec")
    private String email;

    @JsonAlias("contrasena")
    private String password;
}
