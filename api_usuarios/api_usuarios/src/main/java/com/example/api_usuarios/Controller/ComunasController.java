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

import com.example.api_usuarios.Model.Comuna;
import com.example.api_usuarios.Service.ComunaService;

@RestController
@RequestMapping("/api/comunasApi") // url de acceso
public class ComunasController {

    // inyeccion del servicio
    @Autowired
    private ComunaService comunaService;

    // metodos get
    @GetMapping("/")
    public ResponseEntity<List<Comuna>> getAllComunas() {

        List<Comuna> comunas = comunaService.getAllComunas();

        return new ResponseEntity<>(comunas, HttpStatus.OK);
    }

    @GetMapping("/{id_comuna}")
    public ResponseEntity<?> getComunabyId(@PathVariable Long id_comuna) {
        try {
            Comuna comuna = comunaService.getComunaById(id_comuna);
            return ResponseEntity.ok(comuna);
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    };
    // -----fin metodos get

    // metodos POST
    @PostMapping("/")
    public ResponseEntity<?> saveComuna(@RequestBody Comuna comuna) {
        try {
            // comuna queda guardado en el save
            Comuna save = comunaService.saveComuna(comuna);
            return ResponseEntity.ok(save);

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }

    };

    // metodos PUT
    @PutMapping("/{id_comuna}")
    public ResponseEntity<?> putComuna(@RequestBody Comuna comuna, @PathVariable Long id_comuna) {
        try {

            Comuna comuna_actualizada = comunaService.putComuna(comuna, id_comuna);
            return ResponseEntity.ok(comuna_actualizada);

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    };

    @DeleteMapping("/{id_comuna}")
    public ResponseEntity<?> deleteComuna(@PathVariable Long id_comuna) {
        try {
            comunaService.deleteComuna(id_comuna);
            return ResponseEntity.ok("Registro eliminado exitosamente");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    };

}
