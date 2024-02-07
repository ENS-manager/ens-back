
package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.services.CoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/cours")
public class CoursController {

    private CoursService coursService;

    @Autowired
    public CoursController(CoursService coursService) {
        this.coursService = coursService;
    }

    //  Ajouter un cours
    @PostMapping("/addCours")
    public ResponseEntity<Cours> saveCours(@RequestBody Cours cours){
        Cours updateCours = coursService.addCours(cours);
        if (updateCours == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updateCours, HttpStatus.OK);
    }

//    Liste des cours
    @GetMapping("/findAllCours")
    public ResponseEntity<List<Cours>> getAllCours() {

        List<Cours> list = coursService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

//    Un cours par id
    @GetMapping("/findCoursById/{id}")
    public ResponseEntity<Cours> getCoursById(@PathVariable("id") Long id) {

        Cours cours = coursService.getById(id);
        if (cours == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cours, HttpStatus.OK);
    }

//    Un cours par le code du cours
    @GetMapping("/findCoursByCode")
    public ResponseEntity<Cours> getCoursByCode(@RequestParam String code){

        Cours cours = coursService.getByCode(code);
        if (cours == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cours, HttpStatus.OK);
    }

//    Liste des cours d'un departement
    @GetMapping("/findAllCoursByDepart")
    public ResponseEntity<List<Cours>> getAllCoursByDepart(@RequestParam("code") String codeDepart){

        List<Cours> coursList = coursService.getAllCoursByDepartement(codeDepart);
        if (coursList == null){
            return null;
        }
        return new ResponseEntity<>(coursList, HttpStatus.OK);
    }

//    Liste de cours pour un parcours
    @GetMapping("/findCoursByParcours")
    public ResponseEntity<List<Cours>> getCoursByParcours(@RequestParam String label){

        List<Cours> coursList = coursService.getListCoursByParcours(label);
        if (coursList == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(coursList, HttpStatus.OK);
    }

//    Modifier un cours
    @PutMapping("/updateCours/{id}")
    public ResponseEntity<Cours> updateCours(@PathVariable("id") Long id, @RequestBody Cours cours){

        Cours coursFromDb = coursService.update(id, cours);
        if (coursFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(coursFromDb, HttpStatus.OK);
    }

//    Supprimer un cours
    @PutMapping("/deleteCours/{id}")
    public ResponseEntity<String> deleteCours(@PathVariable("id") Long id){
        return new ResponseEntity<>(coursService.delete(id), HttpStatus.OK);
    }
}
