package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/enseignant")
public class EnseigantController {

    @Autowired
    private EnseignantRepo enseignantRepo;
    @Autowired
    private CoursRepo coursRepo;
    @Autowired
    private DepartementRepo departementRepo;
    @Autowired
    private ParcoursRepo parcoursRepo;
    @Autowired
    private OptionRepo optionRepo;

//    Ajouter un enseignant avec la liste de ses cours
    @PostMapping("/addEnseignant")
    public ResponseEntity<Enseignant> saveEnseignant(@RequestBody Enseignant enseignant) {
        Enseignant newEnseignant = new Enseignant();
        newEnseignant.setNom(enseignant.getNom());
        newEnseignant.getCours()
                .addAll(enseignant
                        .getCours()
                        .stream()
                        .map(cours -> {
                            Cours newCours = coursRepo.findByCoursId(cours.getCoursId());
                            newCours.getEnseignant().add(newEnseignant);
                            return newCours;
                        }).collect(Collectors.toList()));
        return new ResponseEntity<>(enseignantRepo.save(newEnseignant), HttpStatus.OK);
        }
    
    @GetMapping("/findAllEnseignant")
    public ResponseEntity<List<Enseignant>> getEnseignant() {
        
        List<Enseignant> list = enseignantRepo.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/findEnseignantById/{id}")
    public ResponseEntity<Enseignant> getEnseignantById(@PathVariable("id") Long id){
        Enseignant enseignant = enseignantRepo.findById(id).orElse(null);
        if (enseignant == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(enseignant, HttpStatus.OK);
    }

//    Liste des enseignants d'un departement
    @GetMapping("/findListEnseignantByDepartement")
    public ResponseEntity<List<Enseignant>> getEnseignantListByDepart(@RequestParam String code){
        Departement departement = departementRepo.findByCode(code).get();
        List<Enseignant> enseignantList = new ArrayList<>();
        if (departement == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        for (Enseignant enseignant : enseignantRepo.findAll()){
            for (Cours cours : enseignant.getCours()){
                Departement depart = departementRepo.findById(cours.getDepartement().getId()).get();
                if (depart.getCode().equals(departement.getCode())){
                    enseignantList.add(enseignant);
                }
                break;
            }
        }
        return new ResponseEntity<>(enseignantList, HttpStatus.OK);
    }

//    Liste des cours enseigner par un enseignant
    @GetMapping("/findCoursTeachByEnseignant/{id}")
    public ResponseEntity<List<Cours>> getCoursTeachByEnseignant(@PathVariable("id") Long id){
        Enseignant enseignant = enseignantRepo.findById(id).orElse(null);
        if (enseignant == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(enseignant.getCours(), HttpStatus.OK);
    }

    @PutMapping("/uptadeEnseignant/{id}")
    public ResponseEntity<Enseignant> updateNiveau(@PathVariable("id") Long id, @RequestBody Enseignant enseignant) {

        Enseignant enseignantFromDb = enseignantRepo.findById(id).orElse(null);
        if (enseignantFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        enseignantFromDb.setNom(enseignant.getNom());
        return new ResponseEntity<>(enseignantRepo.save(enseignantFromDb), HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteEnseignant/{id}")
    public ResponseEntity<String> deleteEnseignant(@PathVariable("id") Long id) {
        enseignantRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
