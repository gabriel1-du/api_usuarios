package com.example.api_usuarios.DTO.login;

import org.springframework.stereotype.Component;

import com.example.api_usuarios.Model.Usuario;

@Component
public class LoginMaper {

    // Convierte el DTO del cuerpo de la peticion en una entidad Usuario
    // que se usa unicamente como criterio de busqueda por correo electronico.
    public Usuario toEntity(loginBody dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setCorreo_elec(dto.getCorreo_elec());

        // La contrasena viaja en el DTO para el futuro login, pero no se usa
        // en esta consulta ni se guarda en la entidad (evita persistir texto plano).
        return usuario;
    }

    // Convertir de Entidad a loginBody: entrega el correo y el hash de la
    // contrasena para que el API Gateway realice la verificacion.
    public loginBody toLoginBody(Usuario usuario) {
        if (usuario == null) return null;

        loginBody dto = new loginBody();
        dto.setCorreo_elec(usuario.getCorreo_elec());
        dto.setContrasena(usuario.getContrasena()); // hash BCrypt almacenado
        return dto;
    }
}
