package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Departement;
import com.api.gestnotesapi.entities.TypeCours;
import com.api.gestnotesapi.repository.TypeCoursRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/typeCours")
public class TypeCoursController {

    @Autowired
    private TypeCoursRepo typeCoursRepo;

    //  Ajouter un typeCours
    @PostMapping("/addTypeCours")
    public ResponseEntity<TypeCours> saveTypeCours(@RequestBody TypeCours typeCours){

        if (typeCours == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(typeCoursRepo.save(typeCours), HttpStatus.OK);
    }

    //    Liste des typeCours
    @GetMapping("/findAllTypeCours")
    public ResponseEntity<List<TypeCours>> getAllCours() {

        List<TypeCours> list = typeCoursRepo.findAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Un typeCours par id
    @GetMapping("/findTypeCoursById/{id}")
    public ResponseEntity<TypeCours> getTypeCoursById(@PathVariable("id") Long id) {

        TypeCours typeCours = typeCoursRepo.findById(id).orElse(null);
        if (typeCours == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(typeCours, HttpStatus.OK);
    }

    //    Modifier un typeCours
    @PutMapping("/updateTypeCours/{id}")
    public ResponseEntity<TypeCours> updateTypeCours(@PathVariable("id") Long id, @RequestBody TypeCours typeCours){

        TypeCours typeCoursFromDb = typeCoursRepo.findById(id).orElse(null);
        if (typeCoursFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        typeCoursFromDb.setNom(typeCours.getNom());

        return new ResponseEntity<>(typeCoursRepo.save(typeCoursFromDb), HttpStatus.OK);
    }

    //    Supprimer un typeCours
    @DeleteMapping("/deleteTypeCours/{id}")
    public String deleteTypeCours(@PathVariable("id") Long id){
        typeCoursRepo.deleteById(id);
        return "Deleted with Successfully from database";
    }
}
