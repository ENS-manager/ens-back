package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.repository.AnneeAcademiqueRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/anneeAcademique")
public class AnneeAcademiqueController {

    @Autowired
    private AnneeAcademiqueRepo anneeAcademiqueRepo;

//    Liste des annees academiques
    @GetMapping("/findAllAnneeAcademique")
    public ResponseEntity<List<AnneeAcademique>> getAnnee() {

        List<AnneeAcademique> list = anneeAcademiqueRepo.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

//    ajouter une annee academique
    @PostMapping("/addAnneeAcademique")
    public ResponseEntity<AnneeAcademique> saveAnnee(@RequestBody AnneeAcademique anneeAcademique) {

        Integer numero = anneeAcademique.getDebut().getYear();
        anneeAcademique.setNumeroDebut(numero);
        AnneeAcademique update = anneeAcademiqueRepo.save(anneeAcademique);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

//    Une annee par son id
    @GetMapping("/findAnneeAcaById/{id}")
    public ResponseEntity<AnneeAcademique> getAnneeAcaById(@PathVariable Long id){
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findById(id).orElse(null);
        if (anneeAcademique == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(anneeAcademique, HttpStatus.OK);
    }

//    Modifier une annee academique
    @PutMapping("/uptadeAnneeAca/{id}")
    public ResponseEntity<AnneeAcademique> updateAnnee(@PathVariable("id") Long id, @RequestBody AnneeAcademique anneeAcademique) {

        AnneeAcademique anneeAcaFromDb = anneeAcademiqueRepo.findById(id).orElse(null);
        if (anneeAcaFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        anneeAcaFromDb.setDebut(anneeAcademique.getDebut());
        anneeAcaFromDb.setNumeroDebut(anneeAcademique.getDebut().getYear());
        anneeAcaFromDb.setFin(anneeAcademique.getFin());

        return new ResponseEntity<>(anneeAcademiqueRepo.save(anneeAcaFromDb), HttpStatus.OK);
    }

//    Supprimer une annee academique
    @DeleteMapping("/deleteAnneeAca/{id}")
    public String deleteAnnee(@PathVariable("id") Long id){
            anneeAcademiqueRepo.deleteById(id);
            return "Deleted with Successfully from database";
    }
}
