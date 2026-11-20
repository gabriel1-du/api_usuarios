package com.example.api_usuarios.DTO.usuarioDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
public class putUsuarioDTO {

    private String p_nombre;
    private String s_nombre;
    private String p_apellido;
    private String s_apellido;
    private String correo_elec;
    private String num_telefono;
    private String contrasena;
    private Long id_region;

}
