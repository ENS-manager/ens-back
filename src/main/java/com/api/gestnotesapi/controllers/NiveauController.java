
package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Niveau;
import java.util.List;

import com.api.gestnotesapi.services.NiveauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/niveau")
public class NiveauController {

    private NiveauService niveauService;

    @Autowired
    public NiveauController(NiveauService niveauService) {
        this.niveauService = niveauService;
    }


    @PostMapping("/addNiveau")
    public ResponseEntity<Niveau> saveNiveau(@RequestBody Niveau niveau){
        Niveau update = niveauService.addNiveau(niveau);
        if (update == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(update, HttpStatus.OK);
    }
    
    
    @GetMapping("/findAllNiveau")
    public ResponseEntity<List<Niveau>> getNiveau() {

        List<Niveau> list = niveauService.getAll();
        if (list == null){
            return null;
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/findNiveauById/{id}")
    public ResponseEntity<Niveau> getNiveauById(@PathVariable("id") Long id){

        Niveau niveauFromDb = niveauService.getById(id);
        if (niveauFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(niveauFromDb, HttpStatus.OK);
    }
    
    @PutMapping("/uptadeNiveau/{id}")
    public ResponseEntity<Niveau> updateNiveau(@PathVariable("id") Long id, @RequestBody Niveau niveau) {

          Niveau niveauFromDb = niveauService.update(id, niveau);
          if (niveauFromDb == null){
              return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
          }

          return new ResponseEntity<>(niveauFromDb, HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteNiveau/{id}")
    public ResponseEntity<String> deleteNiveau(@RequestBody @PathVariable Long id){
        niveauService.delete(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}

