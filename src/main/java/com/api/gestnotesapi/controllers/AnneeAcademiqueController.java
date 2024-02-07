package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.AnneeAcademique;

import java.util.List;

import com.api.gestnotesapi.services.AnneeAcademiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/anneeAcademique")
public class AnneeAcademiqueController {

    private AnneeAcademiqueService anneeAcademiqueService;

    @Autowired
    public AnneeAcademiqueController(AnneeAcademiqueService anneeAcademiqueService) {
        this.anneeAcademiqueService = anneeAcademiqueService;
    }

    //    Liste des annees academiques
    @GetMapping("/findAllAnneeAcademique")
    public ResponseEntity<List<AnneeAcademique>> getAnnee() {

        List<AnneeAcademique> list = anneeAcademiqueService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

//    ajouter une annee academique
    @PostMapping("/addAnneeAcademique")
    public ResponseEntity<AnneeAcademique> saveAnnee(@RequestBody AnneeAcademique anneeAcademique) {

        AnneeAcademique update = anneeAcademiqueService.addAnneeAcademique(anneeAcademique);
        if (update == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

//    Une annee par son id
    @GetMapping("/findAnneeAcaById/{id}")
    public ResponseEntity<AnneeAcademique> getAnneeAcaById(@PathVariable Long id){
        AnneeAcademique anneeAcademique = anneeAcademiqueService.getById(id);
        if (anneeAcademique == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(anneeAcademique, HttpStatus.OK);
    }

//    Modifier une annee academique
    @PutMapping("/uptadeAnneeAca/{id}")
    public ResponseEntity<AnneeAcademique> updateAnnee(@PathVariable("id") Long id, @RequestBody AnneeAcademique anneeAcademique) {

        AnneeAcademique anneeAcaFromDb = anneeAcademiqueService.updateById(id, anneeAcademique);
        if (anneeAcaFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(anneeAcaFromDb, HttpStatus.OK);
    }

//    Supprimer une annee academique
    @PutMapping("/deleteAnneeAca/{id}")
    public ResponseEntity<String> deleteAnnee(@PathVariable("id") Long id){
        return new ResponseEntity<>(anneeAcademiqueService.delete(id), HttpStatus.OK);
    }
}
