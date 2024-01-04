
package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Semestre;
import com.api.gestnotesapi.repository.SemestreRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/semestre")
public class SemestreController {
        @Autowired
    private SemestreRepo semestreRepo;


    @PostMapping("/addSemestre")
    public ResponseEntity<Semestre> saveSemestre(@RequestBody Semestre semestre){
        Semestre update = semestreRepo.save(semestre);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }
    
    
    @GetMapping("/findAllSemestre")
    public ResponseEntity<List<Semestre>> getSemestre() {

        List<Semestre> list = semestreRepo.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/findSemestreById/{id}")
    public ResponseEntity<Semestre> getSemestreById(@PathVariable("id") Long id){

        Semestre semestre = semestreRepo.findById(id).orElse(null);
        if (semestre == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(semestre, HttpStatus.OK);
    }
    
    @PutMapping("/uptadeSemestre/{id}")
    public ResponseEntity<Semestre> updateSemestre(@PathVariable("id") Long id, @RequestBody Semestre semestre) {

        Semestre semestreFromDb = semestreRepo.findById(id).orElse(null);
        if (semestreFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        semestreFromDb.setValeur(semestre.getValeur());

        return new ResponseEntity<>(semestreRepo.save(semestreFromDb), HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteSemestre/{id}")
    public String deleteSemestre(@RequestBody @PathVariable Long id){
        semestreRepo.deleteById(id);
        return "Deleted with Successfully from database";
    }
}
