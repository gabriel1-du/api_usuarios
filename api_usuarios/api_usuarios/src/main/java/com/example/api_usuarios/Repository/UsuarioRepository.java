package com.example.api_usuarios.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api_usuarios.Model.Usuario;

public interface UsuarioRepository extends JpaRepository< Usuario, Long > {

}
