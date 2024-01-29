package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.AnneeAcademiqueRepo;
import com.api.gestnotesapi.repository.EtudiantRepo;
import com.api.gestnotesapi.repository.InscriptionRepo;
import com.api.gestnotesapi.repository.ParcoursRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/inscription")
public class InscriptionController {

    @Autowired
    private InscriptionRepo inscriptionRepo;
    @Autowired
    private EtudiantRepo etudiantRepo;
    @Autowired
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    @Autowired
    private ParcoursRepo parcoursRepo;

    @Autowired
    public InscriptionController(InscriptionRepo inscriptionRepo, EtudiantRepo etudiantRepo, AnneeAcademiqueRepo anneeAcademiqueRepo, ParcoursRepo parcoursRepo) {
        this.inscriptionRepo = inscriptionRepo;
        this.etudiantRepo = etudiantRepo;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
        this.parcoursRepo = parcoursRepo;
    }


    //  Ajouter un Inscription
    @PostMapping("/addInscription")
    public ResponseEntity<Inscription> saveInscription(@RequestBody Inscription inscription){

        if (inscription == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(inscriptionRepo.save(inscription), HttpStatus.OK);
    }

    //    Un inscription par id
    @GetMapping("/findInscriptionById/{id}")
    public ResponseEntity<Inscription> getInscriptionById(@PathVariable("id") Long id) {

        Inscription inscription = inscriptionRepo.findById(id).orElse(null);
        if (inscription == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(inscription, HttpStatus.OK);
    }

    @PutMapping("/updateInscription/{id}")
    public ResponseEntity<Inscription> editInscription(@PathVariable Long id, @RequestBody Inscription inscription){

        Inscription inscriptionFromDb = inscriptionRepo.findById(id).orElse(null);
        if (inscriptionFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        inscriptionFromDb.setEtudiant(inscription.getEtudiant());
        inscriptionFromDb.setAnneeAcademique(inscription.getAnneeAcademique());
        inscriptionFromDb.setParcours(inscription.getParcours());

        return new ResponseEntity<>(inscriptionRepo.save(inscriptionFromDb), HttpStatus.OK);
    }

    //    Supprimer une Inscription
    @DeleteMapping("/deleteInscription/{id}")
    public ResponseEntity<String> deleteInscription(@PathVariable("id") Long id){
        inscriptionRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
