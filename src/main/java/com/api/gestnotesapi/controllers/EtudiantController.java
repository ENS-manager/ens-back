package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
import com.api.gestnotesapi.servicesImpl.EtudiantServiceImpl;
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
//@RequestMapping("/api/v1/admin/etudiant")
public class EtudiantController {

    @Autowired
    private EtudiantRepo etudiantRepo;
    @Autowired
    private DepartementRepo departementRepo;
    @Autowired
    private OptionRepo optionRepo;
    @Autowired
    private InscriptionRepo inscriptionRepo;
    @Autowired
    private ParcoursRepo parcoursRepo;
//    @Autowired
//    private EtudiantServiceImpl etudiantService;

    //  Ajouter un etudiant
    @PostMapping("/addEtudiant")
    public ResponseEntity<Etudiant> saveEtudiant(@RequestBody Etudiant etudiant){

        MatriculeServiceImpl matriculeService = new MatriculeServiceImpl();
        if (etudiant == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Etudiant updateEtudiant = etudiantRepo.save(etudiant);
        String matricule = matriculeService.matriculeGenerator(etudiant.getId());
        updateEtudiant.setMatricule(matricule);
        return new ResponseEntity<>(etudiantRepo.save(updateEtudiant), HttpStatus.OK);
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
    public ResponseEntity<Optional<Etudiant>> getEtudiantByMatricule(@RequestParam String matricule){

        Optional<Etudiant> etudiant = etudiantRepo.findByMatricule(matricule);
        if (!etudiant.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(etudiant, HttpStatus.OK);
    }

    //    Liste des Etudiants d'un departement
    @GetMapping("/findAllEtudiantByDepart")
    public ResponseEntity<List<Etudiant>> getAllEtudiantByDepart(@RequestParam("code") String code){

        List<Etudiant> etudiantList = new ArrayList<>();
        Optional<Departement> departement = departementRepo.findByCode(code);
        if (!departement.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        for (Inscription inscription: inscriptionRepo.findAll()){
            Parcours parcours = parcoursRepo.findById(inscription.getParcours().getId()).get();
            Option option = optionRepo.findById(parcours.getOption().getId()).get();
            Departement depart = departementRepo.findById(option.getDepartement().getId()).get();

            if (depart.getCode().equals(departement.get().getCode())){
                Etudiant etudiant = etudiantRepo.findById(inscription.getEtudiant().getId()).get();
                etudiantList.add(etudiant);
            }
        }

        return new ResponseEntity<>(etudiantList, HttpStatus.OK);
    }

//    liste des etudiants d'un parcours
    @GetMapping("/findAllEtudiantByParcours/{id}")
    public ResponseEntity<List<Etudiant>> getAllEtudiantByParcours(@PathVariable("id") Long id){

        List<Etudiant> etudiantList = new ArrayList<>();
        Parcours parcours = parcoursRepo.findById(id).get();
        if (parcours == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        for (Inscription inscription: inscriptionRepo.findAll()){
            Parcours parc = parcoursRepo.findById(inscription.getParcours().getId()).get();
            if (parc.getLabel().equals(parcours.getLabel())){
                Etudiant etudiant = etudiantRepo.findById(inscription.getEtudiant().getId()).get();
                etudiantList.add(etudiant);
            }
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
    public String deleteEtudiant(@PathVariable("id") Long id){
        etudiantRepo.deleteById(id);
        return "Deleted with Successfully from database";
    }
}
