package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Credit;
import com.api.gestnotesapi.entities.Departement;
import com.api.gestnotesapi.repository.CreditRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/credit")
public class CreditController {

    @Autowired
    private CreditRepo creditRepo;

    //  Ajouter un credit
    @PostMapping("/addCredit")
    public ResponseEntity<Credit> saveCredit(@RequestBody Credit credit){

        if (credit == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(creditRepo.save(credit), HttpStatus.OK);
    }

    //    Liste des credits
    @GetMapping("/findAllCredit")
    public ResponseEntity<List<Credit>> getAllCredit() {

        List<Credit> list = creditRepo.findAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Un credit par id
    @GetMapping("/findCreditById/{id}")
    public ResponseEntity<Credit> getCreditById(@PathVariable("id") Long id) {

        Credit credit = creditRepo.findById(id).orElse(null);
        if (credit == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(credit, HttpStatus.OK);
    }

    //    Modifier la valeur d'un credit
    @PutMapping("/updateCredit/{id}")
    public ResponseEntity<Credit> updateCredit(@PathVariable("id") Long id, @RequestBody Credit credit){

        Credit creditFromDb = creditRepo.findById(id).orElse(null);
        if (creditFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        creditFromDb.setValeur(credit.getValeur());
        return new ResponseEntity<>(creditRepo.save(creditFromDb), HttpStatus.OK);
    }

    //    Supprimer un credit
    @DeleteMapping("/deleteCredit/{id}")
    public ResponseEntity<String> deleteCredit(@PathVariable("id") Long id){
        creditRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
