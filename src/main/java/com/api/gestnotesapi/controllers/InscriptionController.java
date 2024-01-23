package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.AnneeAcademiqueRepo;
import com.api.gestnotesapi.repository.EtudiantRepo;
import com.api.gestnotesapi.repository.InscriptionRepo;
import com.api.gestnotesapi.repository.ParcoursRepo;
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
//@RequestMapping("/api/v1/admin/inscription")
public class InscriptionController {

    @Autowired
    private InscriptionRepo inscriptionRepo;
    @Autowired
    private EtudiantRepo etudiantRepo;
//    @Autowired
//    private MatriculeServiceImpl matriculeService;
    @Autowired
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    @Autowired
    private ParcoursRepo parcoursRepo;

    //  Ajouter un Inscription
    @PostMapping("/addInscription")
    public ResponseEntity<Inscription> saveInscription(@RequestBody Inscription inscription){

        if (inscription == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(inscriptionRepo.save(inscription), HttpStatus.OK);
    }

    //    Liste des etudiants Inscrit a une annee academique, pour un parcours. Exemple: Liste d'etudiant d'INFOTEL 5 annee 2023
    @GetMapping("/findAllEtudiantInscrit/anneeAca/{numeroDebut}/parcours")
    public ResponseEntity<List<Etudiant>> getAllEtudiantInscrit(@PathVariable("numeroDebut") int numeroDebut, @RequestParam("label") String label) {

        List<Etudiant> etudiantList = new ArrayList<>();
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(numeroDebut);
        Optional<Parcours> parcours = parcoursRepo.findByLabel(label);
        if (!parcours.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        for (Inscription inscription : inscriptionRepo.findAll()){
            AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(inscription.getAnneeAcademique().getId()).get();
            Parcours parc = parcoursRepo.findById(inscription.getParcours().getId()).get();
            if ((anneeAcademique.getNumeroDebut().equals(anneeAca.getNumeroDebut()))
                    && (parcours.get().getLabel().equals(parc.getLabel()))){
                Etudiant etudiant = etudiantRepo.findById(inscription.getEtudiant().getId()).get();
                etudiantList.add(etudiant);
            }
        }
        return new ResponseEntity<>(etudiantList, HttpStatus.OK);
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

    //    Supprimer une Inscription
    @DeleteMapping("/deleteInscription/{id}")
    public ResponseEntity<String> deleteInscription(@PathVariable("id") Long id){
        inscriptionRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
