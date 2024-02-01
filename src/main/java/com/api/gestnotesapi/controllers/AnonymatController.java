package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.dto.AnonymatResponse;
import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.services.AnonymatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/anonymat")
public class AnonymatController {

    private AnonymatService anonymatService;

    @Autowired
    public AnonymatController(AnonymatService anonymatService) {
        this.anonymatService = anonymatService;
    }

    //  Ajouter un anonymat
    @PostMapping("/addAnonymat/variable/{n}")
    public ResponseEntity<Anonymat> saveAnonymat(@RequestBody Anonymat anonymat, @PathVariable int n){

        Anonymat save = anonymatService.addAnonymat(anonymat, n);
        if (save == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(save, HttpStatus.OK);
    }

//    Liste des anonymats pour un cours, une session et une annee academique
    @GetMapping("/findListAnonymatByCours/session/{session}/anneeAca/{year}/cours")
    public ResponseEntity<List<AnonymatResponse>> getListAnonymatByCours(@PathVariable int session, @PathVariable int year, @RequestParam("code") String code){

        List<AnonymatResponse> anonymatList = anonymatService.getAnonymatCours(session, year, code);
        if (anonymatList == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(anonymatList, HttpStatus.OK);
    }

    //    Un anonymat par id
    @GetMapping("/findAnonymatById/{id}")
    public ResponseEntity<Anonymat> getAnonymatById(@PathVariable("id") Long id) {

        Anonymat anonymat = anonymatService.getById(id);
        if (anonymat == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(anonymat, HttpStatus.OK);
    }

    //    Supprimer un anonymat
    @DeleteMapping("/deleteAnonymat/{id}")
    public ResponseEntity<String> deleteAnonymat(@PathVariable("id") Long id){
        anonymatService.delete(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
