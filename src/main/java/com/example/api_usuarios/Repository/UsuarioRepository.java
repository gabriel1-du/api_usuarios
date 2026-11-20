package com.example.api_usuarios.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.api_usuarios.Model.Usuario;

public interface UsuarioRepository extends JpaRepository< Usuario, Long > {

    // Consulta JPQL explicita: se usa el nombre del campo Java (correo_elec),
    // no el de la columna SQL. Evita el parser de nombres de Spring Data 4.x.
    @Query("SELECT u FROM Usuario u WHERE u.correo_elec = :correo_elec")
    Optional<Usuario> findByCorreo_elec(@Param("correo_elec") String correo_elec);

}
