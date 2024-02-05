package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.AnneeAcademiqueRepo;
import com.api.gestnotesapi.repository.EtudiantRepo;
import com.api.gestnotesapi.repository.InscriptionRepo;
import com.api.gestnotesapi.repository.ParcoursRepo;
import com.api.gestnotesapi.services.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/inscription")
public class InscriptionController {

    private InscriptionService inscriptionService;

    @Autowired
    public InscriptionController(InscriptionService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }


    //  Ajouter un Inscription
    @PostMapping("/addInscription")
    public ResponseEntity<Inscription> saveInscription(@RequestBody Inscription inscription){
        Inscription inscrit = inscriptionService.addInscription(inscription);
        if (inscrit == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(inscrit, HttpStatus.OK);
    }

    //    Un inscription par id
    @GetMapping("/findInscriptionById/{id}")
    public ResponseEntity<Inscription> getInscriptionById(@PathVariable("id") Long id) {

        Inscription inscription = inscriptionService.getById(id);
        if (inscription == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(inscription, HttpStatus.OK);
    }

    @PutMapping("/updateInscription/{id}")
    public ResponseEntity<Inscription> editInscription(@PathVariable Long id, @RequestBody Inscription inscription){

        Inscription inscriptionFromDb = inscriptionService.update(id, inscription);
        if (inscriptionFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return new ResponseEntity<>(inscriptionFromDb, HttpStatus.OK);
    }

    //    Supprimer une Inscription
    @DeleteMapping("/deleteInscription/{id}")
    public ResponseEntity<String> deleteInscription(@PathVariable("id") Long id){
        inscriptionService.delete(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
