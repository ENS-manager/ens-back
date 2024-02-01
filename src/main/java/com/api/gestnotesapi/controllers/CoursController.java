
package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
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

//     @Autowired
//     private CoursService coursService;
     @Autowired
     private CoursRepo coursRepo;
     @Autowired
     private DepartementRepo departementRepo;
     @Autowired
     private ParcoursRepo parcoursRepo;
     @Autowired
     private OptionRepo optionRepo;
     @Autowired
     private NiveauRepo niveauRepo;
     @Autowired
     private SemestreRepo semestreRepo;


//  Ajouter un cours
    @PostMapping("/addCours")
    public ResponseEntity<Cours> saveCours(@RequestBody Cours cours){

        if (cours == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Semestre semestre = semestreRepo.findById(cours.getSemestre().getId()).orElse(null);
        Departement departement = departementRepo.findById(cours.getDepartement().getId()).orElse(null);
        if (semestre == null || departement == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Niveau niveau = niveauRepo.findById(semestre.getNiveau().getId()).orElse(null);
//        if (niveau == null){
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
        String codeDepart = departement.getCode();
        int valeurNiveau = niveau.getValeur();
        int valeurSemestre = semestre.getValeur();
        Cours updateCours = coursRepo.save(cours);
        String code = codeDepart+valeurNiveau+updateCours.getCoursId()+valeurSemestre;
        cours.setCode(code);
        return new ResponseEntity<>(coursRepo.save(updateCours), HttpStatus.OK);
    }

//    Liste des cours
    @GetMapping("/findAllCours")
    public ResponseEntity<List<Cours>> getAllCours() {

        List<Cours> list = coursRepo.findAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

//    Un cours par id
    @GetMapping("/findCoursById/{id}")
    public ResponseEntity<Cours> getCoursById(@PathVariable("id") Long id) {

        Cours cours = coursRepo.findById(id).orElse(null);
        if (cours == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cours, HttpStatus.OK);
    }

//    Un cours par le code du cours
    @GetMapping("/findCoursByCode")
    public ResponseEntity<Optional<Cours>> getCoursByCode(@RequestParam String code){

//        CoursServiceImpl coursService = new CoursServiceImpl();
        Optional<Cours> cours = coursRepo.findByCode(code);
        if (!cours.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cours, HttpStatus.OK);
    }

//    Liste des cours d'un departement
    @GetMapping("/findAllCoursByDepart")
    public ResponseEntity<List<Cours>> getAllCoursByDepart(@RequestParam("code") String codeDepart){

        List<Cours> coursList = new ArrayList<>();
        Optional<Departement> departement = departementRepo.findByCode(codeDepart);
        if (!departement.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        for(Cours cours : coursRepo.findAll()){
            Departement depart = departementRepo.findById(cours.getDepartement().getId()).get();
            if (depart.getCode().equals(departement.get().getCode())){
                coursList.add(cours);
            }
        }
        return new ResponseEntity<>(coursList, HttpStatus.OK);
    }

//    Liste de cours pour un parcours
    @GetMapping("/findCoursByParcours")
    public ResponseEntity<List<Cours>> getCoursByParcours(@RequestParam String label){

        List<Cours> coursList = new ArrayList<>();
        Optional<Parcours> parcours = parcoursRepo.findByLabel(label);
        if (!parcours.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        for (Cours cours : coursRepo.findAll()){
            Semestre semestre = semestreRepo.findById(cours.getSemestre().getId()).get();
            Niveau niveau = niveauRepo.findById(semestre.getNiveau().getId()).get();
            for (Option option : optionRepo.findAll()){
                Parcours par = parcoursRepo.findByOptionAndNiveau(option, niveau);
                if (par.getLabel().equals(parcours.get().getLabel())){
                    coursList.add(cours);
                }
            }
        }
        return new ResponseEntity<>(coursList, HttpStatus.OK);
    }

//    Modifier un cours
    @PutMapping("/updateCours/{id}")
    public ResponseEntity<Cours> updateCours(@PathVariable("id") Long id, @RequestBody Cours cours){

        Cours coursFromDb = coursRepo.findById(id).orElse(null);
        if (coursFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        coursFromDb.setCode(cours.getCode());
        coursFromDb.setCredit(cours.getCredit());
        coursFromDb.setDepartement(cours.getDepartement());
        coursFromDb.setIntitule(cours.getIntitule());
        coursFromDb.setNatureUE(cours.getNatureUE());
        coursFromDb.setTypecours(cours.getTypecours());
        coursFromDb.setSemestre(cours.getSemestre());

        return new ResponseEntity<>(coursRepo.save(coursFromDb), HttpStatus.OK);
    }

//    Supprimer un cours
    @DeleteMapping("/deleteCours/{id}")
    public ResponseEntity<String> deleteCours(@PathVariable("id") Long id){
        coursRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
