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

    private EtudiantRepo etudiantRepo;
    private DepartementRepo departementRepo;
    private OptionRepo optionRepo;
    private InscriptionRepo inscriptionRepo;
    private ParcoursRepo parcoursRepo;
    private EtudiantService etudiantService;
    @Autowired
    public EtudiantController(EtudiantRepo etudiantRepo, DepartementRepo departementRepo, OptionRepo optionRepo, InscriptionRepo inscriptionRepo, ParcoursRepo parcoursRepo, EtudiantService etudiantService) {
        this.etudiantRepo = etudiantRepo;
        this.departementRepo = departementRepo;
        this.optionRepo = optionRepo;
        this.inscriptionRepo = inscriptionRepo;
        this.parcoursRepo = parcoursRepo;
        this.etudiantService = etudiantService;
    }

    //  Ajouter un etudiant
    @PostMapping("/addEtudiant")
    public ResponseEntity<Etudiant> saveEtudiant(@RequestBody Etudiant etudiant){

        MatriculeService matriculeService = new MatriculeService();
        if (etudiant == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Etudiant updateEtudiant = etudiantRepo.save(etudiant);
        String matricule = "";
        if (etudiant.getMatricule() == null){
            matricule = matriculeService.matriculeGenerator(updateEtudiant.getId());
            updateEtudiant.setMatricule(matricule);
            return new ResponseEntity<>(etudiantRepo.save(updateEtudiant), HttpStatus.OK);
        }
        return new ResponseEntity<>(updateEtudiant, HttpStatus.OK);
    }

    //    Liste des etudiant
    @GetMapping("/findAllEtudiant")
    public ResponseEntity<List<Etudiant>> getAllEtudiant() {

        List<Etudiant> list = etudiantRepo.findAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Un etudiant par id
    @GetMapping("/findEtudiantById/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable("id") Long id) {

        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        if (etudiant == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(etudiant, HttpStatus.OK);
    }

    //    Un etudiant par le matricule de l'etudiant
    @GetMapping("/findEtudiantByMatricule")
    public ResponseEntity<Etudiant> getEtudiantByMatricule(@RequestParam String matricule){

        Etudiant etudiant = etudiantRepo.findByMatricule(matricule).orElse(null);
        if (etudiant == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(etudiant, HttpStatus.OK);
    }

    //    Liste des Etudiants d'un departement
    @GetMapping("/findAllEtudiantByDepart")
    public ResponseEntity<List<Etudiant>> getAllEtudiantByDepart(@RequestParam("code") String code){

        List<Etudiant> etudiantList = new ArrayList<>();
        Departement departement = departementRepo.findByCode(code).orElse(null);
        if (departement == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        for (Inscription inscription: inscriptionRepo.findAll()){
            Parcours parcours = parcoursRepo.findById(inscription.getParcours().getId()).get();
            Option option = optionRepo.findById(parcours.getOption().getId()).get();
            Departement depart = departementRepo.findById(option.getDepartement().getId()).get();

            if (depart.getCode().equals(departement.getCode())){
                Etudiant etudiant = etudiantRepo.findById(inscription.getEtudiant().getId()).get();
                etudiantList.add(etudiant);
            }
        }

        return new ResponseEntity<>(etudiantList, HttpStatus.OK);
    }

//    liste des etudiants d'un parcours
    @GetMapping("/findAllEtudiantByParcours/annee/{year}/typeEtudiant/{type}/parcours")
    public ResponseEntity<List<Etudiant>> getAllEtudiantByParcours(@PathVariable int year, @PathVariable TYPE type, @RequestParam String label){

        List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(label, year, type);
        if (etudiantList.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(etudiantList, HttpStatus.OK);
    }

    //    Modifier un Etudiant
    @PutMapping("/updateEtudiant/{id}")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable("id") Long id, @RequestBody Etudiant etudiant){

        Etudiant etudiantFromDb = etudiantRepo.findById(id).orElse(null);
        if (etudiantFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        etudiantFromDb.setEmail(etudiant.getEmail());
        etudiantFromDb.setGenre(etudiant.getGenre());
        etudiantFromDb.setDateDeNaissance(etudiant.getDateDeNaissance());
        etudiantFromDb.setLieuDeNaissance(etudiant.getLieuDeNaissance());
        etudiantFromDb.setNom(etudiant.getNom());
        etudiantFromDb.setNumeroTelephone(etudiant.getNumeroTelephone());
        etudiantFromDb.setRegion(etudiant.getRegion());

        return new ResponseEntity<>(etudiantRepo.save(etudiantFromDb), HttpStatus.OK);
    }

    //    Supprimer un Etudiant
    @DeleteMapping("/deleteEtudiant/{id}")
    public ResponseEntity<String> deleteEtudiant(@PathVariable("id") Long id){
        etudiantRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
