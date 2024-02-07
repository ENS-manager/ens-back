
package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Semestre;
import com.api.gestnotesapi.repository.SemestreRepo;
import java.util.List;
import java.util.Optional;

import com.api.gestnotesapi.services.SemestreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/semestre")
public class SemestreController {

    private SemestreService semestreService;

    @Autowired
    public SemestreController(SemestreService semestreService) {
        this.semestreService = semestreService;
    }

    @PostMapping("/addSemestre")
    public ResponseEntity<Semestre> saveSemestre(@RequestBody Semestre semestre){
        Semestre update = semestreService.addService(semestre);
        if (update == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(update, HttpStatus.OK);
    }
    
    
    @GetMapping("/findAllSemestre")
    public ResponseEntity<List<Semestre>> getSemestre() {

        List<Semestre> list = semestreService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/findSemestreById/{id}")
    public ResponseEntity<Semestre> getSemestreById(@PathVariable("id") Long id){

        Semestre semestre = semestreService.getById(id);
        if (semestre == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(semestre, HttpStatus.OK);
    }
    
    @PutMapping("/uptadeSemestre/{id}")
    public ResponseEntity<Semestre> updateSemestre(@PathVariable("id") Long id, @RequestBody Semestre semestre) {

        Semestre semestreFromDb = semestreService.update(id, semestre);
        if (semestreFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(semestreFromDb, HttpStatus.OK);
    }
    
    @PutMapping("/deleteSemestre/{id}")
    public ResponseEntity<String> deleteSemestre(@RequestBody @PathVariable Long id){
        return new ResponseEntity<>(semestreService.delete(id), HttpStatus.OK);
    }
}
