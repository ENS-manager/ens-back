
package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Niveau;
import com.api.gestnotesapi.repository.NiveauRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/niveau")
public class NiveauController {

    @Autowired
    private NiveauRepo niveauRepo;


    @PostMapping("/addNiveau")
    public ResponseEntity<Niveau> saveNiveau(@RequestBody Niveau niveau){
        Niveau update = niveauRepo.save(niveau);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }
    
    
    @GetMapping("/findAllNiveau")
    public ResponseEntity<List<Niveau>> getNiveau() {

        List<Niveau> list = niveauRepo.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/findNiveauById/{id}")
    public ResponseEntity<Niveau> getNiveauById(@PathVariable("id") Long id){

        Niveau niveauFromDb = niveauRepo.findById(id).orElse(null);
        if (niveauFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(niveauFromDb, HttpStatus.OK);
    }
    
    @PutMapping("/uptadeNiveau/{id}")
    public ResponseEntity<Niveau> updateNiveau(@PathVariable("id") Long id, @RequestBody Niveau niveau) {

          Niveau niveauFromDb = niveauRepo.findById(id).orElse(null);
          if (niveauFromDb == null){
              return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
          }

          niveauFromDb.setTerminal(niveau.getTerminal());
          niveauFromDb.setValeur(niveau.getValeur());
          niveauFromDb.setCycle(niveau.getCycle());

          return new ResponseEntity<>(niveauRepo.save(niveauFromDb), HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteNiveau/{id}")
    public ResponseEntity<String> deleteNiveau(@RequestBody @PathVariable Long id){
        niveauRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}

