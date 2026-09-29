package com.example.api_usuarios.Service;

import java.util.List;

import com.example.api_usuarios.Model.Comuna;


public interface ComunaService {

    //Metodos Crud
    public List<Comuna> getAllComunas(); //trae todos las comunas

    public Comuna getComunaById(Long id_comuna); //por id

    public Comuna saveComuna(Comuna comuna); // guardar comuna

    public Comuna putComuna(Comuna comuna, Long id_comuna); //actualizar comuna

    public void deleteComuna(Long id);

}
