package com.example.api_usuarios.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;


@Entity
@Data
@NoArgsConstructor
@Table(name = "REGIONES")
public class Region {


    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_region;

    @Column(name = "nombre_region", nullable = false)
    private String nombre_region;


}
