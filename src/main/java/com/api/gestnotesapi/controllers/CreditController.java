package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Credit;
import com.api.gestnotesapi.services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/credit")
public class CreditController {

    private CreditService creditService;

    @Autowired
    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    //  Ajouter un credit
    @PostMapping("/addCredit")
    public ResponseEntity<Credit> saveCredit(@RequestBody Credit credit){

        Credit update = creditService.addCredit(credit);
        if (update == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    //    Liste des credits
    @GetMapping("/findAllCredit")
    public ResponseEntity<List<Credit>> getAllCredit() {

        List<Credit> list = creditService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Un credit par id
    @GetMapping("/findCreditById/{id}")
    public ResponseEntity<Credit> getCreditById(@PathVariable("id") Long id) {

        Credit credit = creditService.getById(id);
        if (credit == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(credit, HttpStatus.OK);
    }

    //    Modifier la valeur d'un credit
    @PutMapping("/updateCredit/{id}")
    public ResponseEntity<Credit> updateCredit(@PathVariable("id") Long id, @RequestBody Credit credit){

        Credit creditFromDb = creditService.updateById(id, credit);
        if (creditFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(creditFromDb, HttpStatus.OK);
    }

    //    Supprimer un credit
    @DeleteMapping("/deleteCredit/{id}")
    public ResponseEntity<String> deleteCredit(@PathVariable("id") Long id){
        creditService.delete(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
