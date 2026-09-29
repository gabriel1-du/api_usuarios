package com.example.api_usuarios.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.api_usuarios.Repository.RegionRepository;
import com.example.api_usuarios.Model.Region;
import com.example.api_usuarios.Service.RegionService;
@Service 
public class RegionServiceImpl implements RegionService{

    //Inyeccion de repositorio
    @Autowired 
    private RegionRepository regionRepository;



    //metodos GET
    public List<Region> getAllRegiones(){
        return regionRepository.findAll();
    };

    public Region getRegionById(Long id_region){

        return regionRepository.findById(id_region)
        .orElseThrow(()-> new RuntimeException("Region no encontrada con id: " + id_region));
    };
    //---FIN GET


    //metodos POST
    public Region saveRegion(Region region){
        return regionRepository.save(region);
    };


    //metodos PUT
    public Region putRegion(Region region, Long id_region){

        Region region_existente = regionRepository.findById(id_region)
            .orElseThrow(()-> new RuntimeException("Region no escontrada con el id: "+id_region));

        region_existente.setNombre_region(region.getNombre_region());
        return regionRepository.save(region_existente);

    };

    public void deleteRegion(Long id_region){

        Region region_eliminada = regionRepository.findById(id_region)
        .orElseThrow(()-> new RuntimeException("Region no escontrada con el id: "+id_region));

        regionRepository.delete(region_eliminada);
    };
    

};
