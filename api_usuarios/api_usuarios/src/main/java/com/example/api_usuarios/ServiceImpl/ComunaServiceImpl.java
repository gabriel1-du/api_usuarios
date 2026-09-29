package com.example.api_usuarios.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api_usuarios.Model.Comuna;
import com.example.api_usuarios.Repository.ComunaRepository;
import com.example.api_usuarios.Service.ComunaService;

@Service
public class ComunaServiceImpl implements ComunaService {

    // Inyeccion de repositorio
    @Autowired
    private ComunaRepository comunaRepository;

    // metodos GET
    public List<Comuna> getAllComunas() {
        return comunaRepository.findAll();
    };

    public Comuna getComunaById(Long id_comuna) {
        return comunaRepository.findById(id_comuna)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada con id: " + id_comuna));
    };
    // ---FIN GET

    // metodos POST
    public Comuna saveComuna(Comuna comuna) {
        return comunaRepository.save(comuna);
    };

    // metodos PUT
    public Comuna putComuna(Comuna comuna, Long id_comuna) {

        Comuna comuna_existente = comunaRepository.findById(id_comuna)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada con el id: " + id_comuna));

        comuna_existente.setNombre_comuna(comuna.getNombre_comuna());
        comuna_existente.setRegion_comuna(comuna.getRegion_comuna()); // O setRegion / setId_region según tengas mapeada la FK en la entidad
        return comunaRepository.save(comuna_existente);

    }

    public void deleteComuna(Long id_comuna) {

        Comuna comuna_eliminada = comunaRepository.findById(id_comuna)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada con el id: " + id_comuna));

        comunaRepository.delete(comuna_eliminada);
    };

}