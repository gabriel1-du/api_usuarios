package com.example.api_usuarios.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api_usuarios.Model.Comuna;

public interface ComunaRepository extends JpaRepository<Comuna, Long> {

}