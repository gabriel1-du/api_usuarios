package com.example.api_usuarios.DTO.usuarioDTO.comunaDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
public class saveComunaDTO {

    private String comuna;
    private Long id_region;
}
