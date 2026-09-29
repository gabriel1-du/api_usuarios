package com.example.api_usuarios.Service;

import java.util.List;

import com.example.api_usuarios.DTO.usuarioDTO.getUsuarioDTO;
import com.example.api_usuarios.DTO.usuarioDTO.getUsuarioDTOAdmin;
import com.example.api_usuarios.DTO.usuarioDTO.postUsuarioDTO;
import com.example.api_usuarios.DTO.usuarioDTO.putUsuarioDTO;
import com.example.api_usuarios.DTO.usuarioDTO.putUsuarioDTOAdmin;

public interface UsuarioService {

    // Metodos GET
    public List<getUsuarioDTO> getAllUsuarios(); // Listado para usuarios normales (sin contrasena)

    public List<getUsuarioDTOAdmin> getAllUsuariosAdmin(); // Listado para administradores (con contrasena)

    public getUsuarioDTO getUsuarioById(Long id_usuario); // Por id (sin contrasena)

    public getUsuarioDTOAdmin getUsuarioByIdAdmin(Long id_usuario); // Por id (con contrasena)
    // --- FIN GET

    // Metodos POST
    public getUsuarioDTO saveUsuario(postUsuarioDTO postUsuarioDTO); // Guardar usuario

    // Metodos PUT
    public getUsuarioDTO putUsuario(putUsuarioDTO putUsuarioDTO, Long id_usuario); // Actualizar usuario normal

    public getUsuarioDTOAdmin putUsuarioAdmin(putUsuarioDTOAdmin putUsuarioDTOAdmin, Long id_usuario); // Actualizar admin

    // Metodos DELETE
    public void deleteUsuario(Long id_usuario); // Eliminar usuario

}
