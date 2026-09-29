package com.example.api_usuarios.Service;

import java.util.List;


import com.example.api_usuarios.Model.Region;

public interface RegionService {

    //Metodos Crud
    public List<Region> getAllRegiones(); //trae todos las comunas

    public Region getRegionById(Long id_region); //por id

    public Region saveRegion(Region region); // guardar region

    public Region putRegion(Region region, Long id_region); //actualizar region   

    void deleteRegion(Long id_region);



}
