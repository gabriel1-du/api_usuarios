package com.example.api_usuarios.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "COMUNAS")
@NoArgsConstructor 
@Data 
public class Comuna {


    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_comuna;

    @Column(name = "nombre_comuna" , nullable = false)
    private String nombre_comuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_region", nullable = false)
    private Region region_comuna;
}
