package com.example.api_usuarios.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api_usuarios.DTO.usuarioDTO.getUsuarioDTO;
import com.example.api_usuarios.DTO.usuarioDTO.getUsuarioDTOAdmin;
import com.example.api_usuarios.DTO.usuarioDTO.postUsuarioDTO;
import com.example.api_usuarios.DTO.usuarioDTO.putUsuarioDTO;
import com.example.api_usuarios.DTO.usuarioDTO.putUsuarioDTOAdmin;
import com.example.api_usuarios.Service.UsuarioService;

@RestController
@RequestMapping("/api/usuariosApi") // url de acceso
public class UsuarioController {

    // inyeccion del servicio
    @Autowired
    private UsuarioService usuarioService;


    // metodos get
    @GetMapping("/")
    public ResponseEntity<List<getUsuarioDTO>> getAllUsuarios() {
        List<getUsuarioDTO> usuarios = usuarioService.getAllUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<getUsuarioDTOAdmin>> getAllUsuariosAdmin() {
        List<getUsuarioDTOAdmin> usuariosAdmin = usuarioService.getAllUsuariosAdmin();
        return new ResponseEntity<>(usuariosAdmin, HttpStatus.OK);
    }

    @GetMapping("/{id_usuario}")
    public ResponseEntity<?> getUsuarioById(@PathVariable Long id_usuario) {
        try {
            getUsuarioDTO usuario = usuarioService.getUsuarioById(id_usuario);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/admin/{id_usuario}")
    public ResponseEntity<?> getUsuarioByIdAdmin(@PathVariable Long id_usuario) {
        try {
            getUsuarioDTOAdmin usuarioAdmin = usuarioService.getUsuarioByIdAdmin(id_usuario);
            return ResponseEntity.ok(usuarioAdmin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    // ----- fin metodos get


    // metodos POST
    @PostMapping("/")
    public ResponseEntity<?> saveUsuario(@RequestBody postUsuarioDTO postUsuarioDTO) {
        try {
            // guarda y retorna getUsuarioDTO (sin contrasena)
            getUsuarioDTO save = usuarioService.saveUsuario(postUsuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(save);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // metodos PUT
    @PutMapping("/{id_usuario}")
    public ResponseEntity<?> putUsuario(@RequestBody putUsuarioDTO putUsuarioDTO, @PathVariable Long id_usuario) {
        try {
            getUsuarioDTO usuario_actualizado = usuarioService.putUsuario(putUsuarioDTO, id_usuario);
            return ResponseEntity.ok(usuario_actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/admin/{id_usuario}")
    public ResponseEntity<?> putUsuarioAdmin(@RequestBody putUsuarioDTOAdmin putUsuarioDTOAdmin, @PathVariable Long id_usuario) {
        try {
            getUsuarioDTOAdmin usuario_actualizado = usuarioService.putUsuarioAdmin(putUsuarioDTOAdmin, id_usuario);
            return ResponseEntity.ok(usuario_actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // metodos DELETE
    @DeleteMapping("/{id_usuario}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id_usuario) {
        try {
            usuarioService.deleteUsuario(id_usuario);
            return ResponseEntity.ok("Registro eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
