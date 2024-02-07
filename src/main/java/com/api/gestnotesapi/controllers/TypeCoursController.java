package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.TypeCours;
import com.api.gestnotesapi.services.TypeCoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/typeCours")
public class TypeCoursController {

    private TypeCoursService typeCoursService;

    @Autowired
    public TypeCoursController(TypeCoursService typeCoursService) {
        this.typeCoursService = typeCoursService;
    }

    //  Ajouter un typeCours
    @PostMapping("/addTypeCours")
    public ResponseEntity<TypeCours> saveTypeCours(@RequestBody TypeCours typeCours){

        TypeCours update = typeCoursService.addTypeCours(typeCours);
        if (update == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(typeCours, HttpStatus.OK);
    }

    //    Liste des typeCours
    @GetMapping("/findAllTypeCours")
    public ResponseEntity<List<TypeCours>> getAllCours() {

        List<TypeCours> list = typeCoursService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Un typeCours par id
    @GetMapping("/findTypeCoursById/{id}")
    public ResponseEntity<TypeCours> getTypeCoursById(@PathVariable("id") Long id) {

        TypeCours typeCours = typeCoursService.getById(id);
        if (typeCours == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(typeCours, HttpStatus.OK);
    }

    //    Modifier un typeCours
    @PutMapping("/updateTypeCours/{id}")
    public ResponseEntity<TypeCours> updateTypeCours(@PathVariable("id") Long id, @RequestBody TypeCours typeCours){

        TypeCours typeCoursFromDb = typeCoursService.update(id, typeCours);
        if (typeCoursFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(typeCoursFromDb, HttpStatus.OK);
    }

    //    Supprimer un typeCours
    @PutMapping("/deleteTypeCours/{id}")
    public ResponseEntity<String> deleteTypeCours(@PathVariable("id") Long id){
        return new ResponseEntity<>(typeCoursService.delete(id), HttpStatus.OK);
    }
}
