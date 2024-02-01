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

    private ParcoursRepo parcoursRepo;
    private NiveauRepo niveauRepo;
    private OptionRepo optionRepo;
    private DepartementRepo departementRepo;
    private ParcoursService parcoursService;
    private EtudiantRepo etudiantRepo;
    private AnneeAcademiqueRepo anneeAcademiqueRepo;

    @Autowired
    public ParcoursController(ParcoursRepo parcoursRepo, NiveauRepo niveauRepo, OptionRepo optionRepo, DepartementRepo departementRepo, ParcoursService parcoursService, EtudiantRepo etudiantRepo, AnneeAcademiqueRepo anneeAcademiqueRepo) {
        this.parcoursRepo = parcoursRepo;
        this.niveauRepo = niveauRepo;
        this.optionRepo = optionRepo;
        this.departementRepo = departementRepo;
        this.parcoursService = parcoursService;
        this.etudiantRepo = etudiantRepo;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
    }

    //  Ajouter un parcours
    @PostMapping("/addParcours")
    public ResponseEntity<Parcours> saveParcours(@RequestBody Parcours parcours){

        if (parcours == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Niveau niveau = niveauRepo.findById(parcours.getNiveau().getId()).get();
        Option option = optionRepo.findById(parcours.getOption().getId()).get();
        String code = option.getCode();
        int valeur = niveau.getValeur();
        String label = code+" "+valeur;
        parcours.setLabel(label);
        return new ResponseEntity<>(parcoursRepo.save(parcours), HttpStatus.OK);
    }

    //    Liste des parcours
    @GetMapping("/findAllParcours")
    public ResponseEntity<List<Parcours>> getAllParcours() {

        List<Parcours> list = parcoursRepo.findAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Un parours par id
    @GetMapping("/findParcoursById/{id}")
    public ResponseEntity<Parcours> getParcoursById(@PathVariable("id") Long id) {

        Parcours parcours = parcoursRepo.findById(id).orElse(null);
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

        List<Parcours> parcoursList = new ArrayList<>();
        Optional<Departement> departement = departementRepo.findByCode(code);
        if (!departement.isPresent()){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        for (Parcours parcours : parcoursRepo.findAll()){
            Option option = optionRepo.findById(parcours.getOption().getId()).get();
            Departement depart = departementRepo.findById(option.getDepartement().getId()).get();
            if (depart.getCode().equals(departement.get().getCode())){
                parcoursList.add(parcours);
            }
        }

        return new ResponseEntity<>(parcoursList, HttpStatus.OK);
    }

    //    Supprimer un parcours
    @DeleteMapping("/deleteParcours/{id}")
    public ResponseEntity<String> deleteParcours(@PathVariable("id") Long id){
        parcoursRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
