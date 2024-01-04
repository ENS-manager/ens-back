
package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Cycle;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api.gestnotesapi.repository.CycleRepo;


@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/cycle")
public class CycleController {
     @Autowired
    private CycleRepo cyclesRepo;

    @GetMapping("/findAllCycle")
    public ResponseEntity<List<Cycle>> getCycle() {

        List<Cycle> list = cyclesRepo.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/addCycle")
    public ResponseEntity<Cycle> saveCycle(@RequestBody Cycle cycle) {

        Cycle update = cyclesRepo.save(cycle);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @GetMapping("/findCycleById/{id}")
    public ResponseEntity<Cycle> getCycleById(@PathVariable("id") Long id){
        Cycle cycle = cyclesRepo.findById(id).orElse(null);
        if (cycle == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cycle, HttpStatus.OK);
    }

    @PutMapping("/updateCycle/{id}")
    public ResponseEntity<Cycle> updateCycle(@PathVariable("id") Long id, @RequestBody Cycle cycle){

        Cycle cycleFromDb = cyclesRepo.findById(id).orElse(null);
        if (cycleFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        cycleFromDb.setCode(cycle.getCode());
        cycleFromDb.setDiplomeEn(cycle.getDiplomeEn());
        cycleFromDb.setDiplomeFr(cycle.getDiplomeFr());
        cycleFromDb.setEstAffichable(cycle.getEstAffichable());

        return new ResponseEntity<>(cyclesRepo.save(cycleFromDb), HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteCycle/{id}")
    public String deleteCycle(@RequestBody @PathVariable Long id){
        cyclesRepo.deleteById(id);
        return "Deleted with Successfully from database";
    }
}
