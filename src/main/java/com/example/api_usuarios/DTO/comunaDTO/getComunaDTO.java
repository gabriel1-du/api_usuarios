package com.example.api_usuarios.DTO.comunaDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
public class getComunaDTO {

    //Atributos de region
    private Long id_region;
    private String nombre_region;

    //Atributos de comuna
    private Long id_comuna;
    private String nombre_comuna;
}
