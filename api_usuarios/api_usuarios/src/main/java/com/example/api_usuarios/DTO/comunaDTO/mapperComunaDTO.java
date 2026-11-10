package com.example.api_usuarios.DTO.comunaDTO;

import org.springframework.stereotype.Component;

import com.example.api_usuarios.Model.Comuna;
import com.example.api_usuarios.Model.Region;


@Component 
public class mapperComunaDTO {


    // Convertir de Entidad Comuna a GetComunaDTO
    public getComunaDTO EntityToGetDTO(Comuna comuna) {
        if (comuna == null) return null;

        getComunaDTO dto = new getComunaDTO();
        dto.setId_comuna(comuna.getId_comuna());
        dto.setNombre_comuna(comuna.getNombre_comuna());

        // region_comuna es de tipo Region: se navega la relacion y se extrae su id (Long)
        if (comuna.getRegion_comuna() != null) {
            dto.setId_region(comuna.getRegion_comuna().getId_region());
            dto.setNombre_region(comuna.getRegion_comuna().getNombre_region());
        }

        return dto;
    }

    // Convertir de SaveComunaDTO a Entidad Comuna (la Region se resuelve en el service)
    public Comuna SaveDtoToEntity(saveComunaDTO dto, Region region) {
        if (dto == null) return null;

        Comuna comuna = new Comuna();
        comuna.setNombre_comuna(dto.getComuna());
        comuna.setRegion_comuna(region);

        return comuna;
    }

    // Convertir de PutComunaDTO a Entidad Comuna (la Region se resuelve en el service)
    public Comuna PutDtoToEntity(putComunaDTO dto, Region region) {
        if (dto == null) return null;

        Comuna comuna = new Comuna();
        comuna.setNombre_comuna(dto.getComuna());
        comuna.setRegion_comuna(region);

        return comuna;
    }
}

