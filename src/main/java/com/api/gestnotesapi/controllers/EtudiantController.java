package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
import com.api.gestnotesapi.services.EtudiantService;
import com.api.gestnotesapi.services.MatriculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/etudiant")
public class EtudiantController {

    private EtudiantService etudiantService;
    @Autowired
    public EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    //  Ajouter un etudiant
    @PostMapping("/addEtudiant")
    public ResponseEntity<Etudiant> saveEtudiant(@RequestBody Etudiant etudiant){

        Etudiant updateEtudiant = etudiantService.addEtudiant(etudiant);
        if (updateEtudiant == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updateEtudiant, HttpStatus.OK);
    }

    //    Liste des etudiant
    @GetMapping("/findAllEtudiant")
    public ResponseEntity<List<Etudiant>> getAllEtudiant() {

        List<Etudiant> list = etudiantService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Un etudiant par id
    @GetMapping("/findEtudiantById/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable("id") Long id) {

        Etudiant etudiant = etudiantService.getById(id);
        if (etudiant == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(etudiant, HttpStatus.OK);
    }

    //    Un etudiant par le matricule de l'etudiant
    @GetMapping("/findEtudiantByMatricule")
    public ResponseEntity<Etudiant> getEtudiantByMatricule(@RequestParam String matricule){

        Etudiant etudiant = etudiantService.getByMatricule(matricule);
        if (etudiant == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(etudiant, HttpStatus.OK);
    }

    //    Liste des Etudiants d'un departement
    @GetMapping("/findAllEtudiantByDepart")
    public ResponseEntity<List<Etudiant>> getAllEtudiantByDepart(@RequestParam("code") String code){

        List<Etudiant> etudiantList = etudiantService.getEtudiantByDepartement(code);
        if (etudiantList == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(etudiantList, HttpStatus.OK);
    }

//    liste des etudiants d'un parcours
    @GetMapping("/findAllEtudiantByParcours/annee/{year}/parcours")
    public ResponseEntity<List<Etudiant>> getAllEtudiantByParcours(@PathVariable int year, @RequestParam String label){

        List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(label, year);
        if (etudiantList == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(etudiantList, HttpStatus.OK);
    }

    //    Modifier un Etudiant
    @PutMapping("/updateEtudiant/{id}")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable("id") Long id, @RequestBody Etudiant etudiant){

        Etudiant etudiantFromDb = etudiantService.updateById(id, etudiant);
        if (etudiantFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(etudiantFromDb, HttpStatus.OK);
    }

    //    Supprimer un Etudiant
    @DeleteMapping("/deleteEtudiant/{id}")
    public ResponseEntity<String> deleteEtudiant(@PathVariable("id") Long id){
        return new ResponseEntity<>(etudiantService.delete(id), HttpStatus.OK);
    }
}
