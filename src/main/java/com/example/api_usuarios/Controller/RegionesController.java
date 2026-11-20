package com.example.api_usuarios.Controller;


import java.util.List;

import com.example.api_usuarios.Model.Region;

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
import com.example.api_usuarios.Service.RegionService;


@RestController
@RequestMapping ("/api/regionesApi") //url de acceso
public class RegionesController {

    //inyeccion del servicio
    @Autowired 
    private RegionService regionService;



    //metodos get
    @GetMapping("/")
    public ResponseEntity<List<Region>> getAllRegiones(){

        List<Region> regiones = regionService.getAllRegiones();

        return new ResponseEntity<>(regiones, HttpStatus.OK);
    }


    @GetMapping("/{id_region}")
    public ResponseEntity<?> getRegionbyId(@PathVariable Long id_region){
        try{
            Region region = regionService.getRegionById(id_region);
            return ResponseEntity.ok(region);
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    };

    //-----fin metodos get

    //metodos POST
    @PostMapping("/")
    public ResponseEntity<?> saveRegion(@RequestBody Region region){
        try{
            //region queda guardado en el save
            Region save = regionService.saveRegion(region);
            return ResponseEntity.ok(save);

        } catch (RuntimeException e){
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }

    };
   
    
    //metodos PUT
    @PutMapping("/{id_region}")
    public ResponseEntity<?> putRegion(@RequestBody Region region, @PathVariable Long id_region){
        try{

        Region region_actualizada = regionService.putRegion(region, id_region);
        return ResponseEntity.ok(region_actualizada);

        } catch (RuntimeException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); 
        }

    };


    @DeleteMapping("/{id_region}")
    public ResponseEntity<?> deleteREgion(@PathVariable Long id_region){
        try{
            regionService.deleteRegion(id_region);
            return ResponseEntity.ok("Registro eliminado exitosamente");

        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            
        }
    };


};
