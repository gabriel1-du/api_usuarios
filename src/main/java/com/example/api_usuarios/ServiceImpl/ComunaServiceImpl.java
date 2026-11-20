package com.example.api_usuarios.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api_usuarios.DTO.comunaDTO.getComunaDTO;
import com.example.api_usuarios.DTO.comunaDTO.mapperComunaDTO;
import com.example.api_usuarios.DTO.comunaDTO.putComunaDTO;
import com.example.api_usuarios.DTO.comunaDTO.saveComunaDTO;
import com.example.api_usuarios.Model.Comuna;
import com.example.api_usuarios.Model.Region;
import com.example.api_usuarios.Repository.ComunaRepository;
import com.example.api_usuarios.Repository.RegionRepository;
import com.example.api_usuarios.Service.ComunaService;

@Service
public class ComunaServiceImpl implements ComunaService {

    // Inyeccion de repositorios
    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private RegionRepository regionRepository;

    // Inyeccion del mapper de DTOs
    @Autowired
    private mapperComunaDTO comunaDTOMapper;

    // metodos GET
    @Override
    @Transactional(readOnly = true)
    public List<getComunaDTO> getAllComunas() {
        return comunaRepository.findAll()
                .stream()
                .map(comunaDTOMapper::EntityToGetDTO)
                .collect(Collectors.toList());
    };

    @Override
    @Transactional(readOnly = true)
    public getComunaDTO getComunaById(Long id_comuna) {
        Comuna comuna = comunaRepository.findById(id_comuna)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada con id: " + id_comuna));

        return comunaDTOMapper.EntityToGetDTO(comuna);
    };
    // ---FIN GET

    // metodos POST
    @Override
    public getComunaDTO saveComuna(saveComunaDTO comuna) {
        Region region = obtenerRegion(comuna.getId_region());
        Comuna nueva_comuna = comunaDTOMapper.SaveDtoToEntity(comuna, region);

        return comunaDTOMapper.EntityToGetDTO(comunaRepository.save(nueva_comuna));
    };

    // metodos PUT
    @Override
    public getComunaDTO putComuna(putComunaDTO comuna, Long id_comuna) {

        Comuna comuna_existente = comunaRepository.findById(id_comuna)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada con el id: " + id_comuna));

        Region region = obtenerRegion(comuna.getId_region());
        Comuna datos_nuevos = comunaDTOMapper.PutDtoToEntity(comuna, region);

        comuna_existente.setNombre_comuna(datos_nuevos.getNombre_comuna());
        comuna_existente.setRegion_comuna(datos_nuevos.getRegion_comuna());

        return comunaDTOMapper.EntityToGetDTO(comunaRepository.save(comuna_existente));
    }

    @Override
    public void deleteComuna(Long id_comuna) {

        Comuna comuna_eliminada = comunaRepository.findById(id_comuna)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada con id: " + id_comuna));

        comunaRepository.delete(comuna_eliminada);
    };

    // Metodo auxiliar para resolver la Region (entidad) a partir del id_region del DTO
    private Region obtenerRegion(Long id_region) {
        if (id_region == null) {
            throw new RuntimeException("El id_region es obligatorio");
        }

        return regionRepository.findById(id_region)
                .orElseThrow(() -> new RuntimeException("Region no encontrada con id: " + id_region));
    }
}
