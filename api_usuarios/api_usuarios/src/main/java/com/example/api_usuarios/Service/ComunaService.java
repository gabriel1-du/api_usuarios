package com.example.api_usuarios.Service;

import java.util.List;

import com.example.api_usuarios.DTO.comunaDTO.getComunaDTO;
import com.example.api_usuarios.DTO.comunaDTO.putComunaDTO;
import com.example.api_usuarios.DTO.comunaDTO.saveComunaDTO;


public interface ComunaService {

    //Metodos Crud
    public List<getComunaDTO> getAllComunas(); //trae todos las comunas

    public getComunaDTO getComunaById(Long id_comuna); //por id

    public getComunaDTO saveComuna(saveComunaDTO comuna); // guardar comuna

    public getComunaDTO putComuna(putComunaDTO comuna, Long id_comuna); //actualizar comuna

    public void deleteComuna(Long id);

}
