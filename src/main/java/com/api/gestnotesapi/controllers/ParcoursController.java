package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
import com.api.gestnotesapi.services.ParcoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/parcours")
public class ParcoursController {

   private ParcoursService parcoursService;

    @Autowired
    public ParcoursController(ParcoursService parcoursService) {
        this.parcoursService = parcoursService;
    }

    //  Ajouter un parcours
    @PostMapping("/addParcours")
    public ResponseEntity<Parcours> saveParcours(@RequestBody Parcours parcours){
        Parcours update = parcoursService.addParcours(parcours);
        if (update == null){
            return null;
        }
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    //    Liste des parcours
    @GetMapping("/findAllParcours")
    public ResponseEntity<List<Parcours>> getAllParcours() {

        List<Parcours> list = parcoursService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Un parours par id
    @GetMapping("/findParcoursById/{id}")
    public ResponseEntity<Parcours> getParcoursById(@PathVariable("id") Long id) {

        Parcours parcours = parcoursService.getById(id);
        if (parcours == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(parcours, HttpStatus.OK);
    }

//    Parcours par niveau et option
    @GetMapping("/findParcoursByNiveauAndOption/niveau/{value}/option")
    public ResponseEntity<Parcours> getParcoursByNiveauAndOption(@PathVariable int value, @RequestParam("code") String code){

        Parcours parcours = parcoursService.getParcoursByOptionAndNiveau(value, code);

        if (parcours == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(parcours, HttpStatus.OK);
    }

//    Liste des parcours pour un departement
    @GetMapping("/findParcoursByDepart")
    public ResponseEntity<List<Parcours>> getParcoursByDepart(@RequestParam String code){

        List<Parcours> parcoursList = parcoursService.getAllByDepartement(code);
        if (parcoursList == null){
            return null;
        }
        return new ResponseEntity<>(parcoursList, HttpStatus.OK);
    }

    //    Supprimer un parcours
    @DeleteMapping("/deleteParcours/{id}")
    public ResponseEntity<String> deleteParcours(@PathVariable("id") Long id){
        parcoursService.delete(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
