package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.AnneeAcademiqueRepo;
import com.api.gestnotesapi.repository.AnonymatRepo;
import com.api.gestnotesapi.repository.CoursRepo;
import com.api.gestnotesapi.repository.ParcoursRepo;
import com.api.gestnotesapi.servicesImpl.AnonymatServiceImpl;
import com.api.gestnotesapi.servicesImpl.MatriculeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/anonymat")
public class AnonymatController {

    @Autowired
    private AnonymatRepo anonymatRepo;
    @Autowired
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    @Autowired
    private CoursRepo coursRepo;
    @Autowired
    private ParcoursRepo parcoursRepo;

    //  Ajouter un anonymat
    @PostMapping("/addAnonymat/variable/{n}")
    public ResponseEntity<Anonymat> saveAnonymat(@RequestBody Anonymat anonymat, @PathVariable int n){

        AnonymatServiceImpl anonymatService = new AnonymatServiceImpl();

        if (anonymat == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        int session = anonymat.getSessions();
        AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(anonymat.getAnneeAcademique().getId()).get();
        Cours cours = coursRepo.findByCoursId(anonymat.getCours().getCoursId());

        String valeur = anonymatService.anonymatGenerator(cours.getCode(), session, n);
        anonymat.setValeur(valeur);

        return new ResponseEntity<>(anonymatRepo.save(anonymat), HttpStatus.OK);
    }

//    Liste des anonymats
    @GetMapping("/findAllAnonymat")
    public ResponseEntity<List<Anonymat>> getAllAnonymat(){
        return new ResponseEntity<>(anonymatRepo.findAll(), HttpStatus.OK);
    }

//    Liste des anonymats pour un cours, une session et une annee academique
    @GetMapping("/findListAnonymatByCourse/session/{session}/anneeAca/{year}/cours")
    public ResponseEntity<List<Anonymat>> getListAnonymatByParcours(@PathVariable int session, @PathVariable int year, @RequestParam("code") String code){

        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Cours cours = coursRepo.findByCode(code).get();
        List<Anonymat> anonymatList = new ArrayList<>();

        for (Anonymat anonymat : anonymatRepo.findAll()){
            AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(anonymat.getAnneeAcademique().getId()).get();
            Cours cour = coursRepo.findByCoursId(anonymat.getCours().getCoursId());
            if ((anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                    && (cour.getCode().equals(cours.getCode()))
                    && (anonymat.getSessions().equals(session))){
                anonymatList.add(anonymat);
            }
        }
        return new ResponseEntity<>(anonymatList, HttpStatus.OK);
    }

    //    Un anonymat par id
    @GetMapping("/findAnonymatById/{id}")
    public ResponseEntity<Anonymat> getAnonymatById(@PathVariable("id") Long id) {

        Anonymat anonymat = anonymatRepo.findById(id).orElse(null);
        if (anonymat == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(anonymat, HttpStatus.OK);
    }

    //    Supprimer un anonymat
    @DeleteMapping("/deleteAnonymat/{id}")
    public ResponseEntity<String> deleteAnonymat(@PathVariable("id") Long id){
        anonymatRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
