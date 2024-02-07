
package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Cycle;
import java.util.List;

import com.api.gestnotesapi.services.CycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/cycle")
public class CycleController {

    private CycleService cycleService;

    @Autowired
    public CycleController(CycleService cycleService) {
        this.cycleService = cycleService;
    }

    @GetMapping("/findAllCycle")
    public ResponseEntity<List<Cycle>> getCycle() {

        List<Cycle> list = cycleService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/addCycle")
    public ResponseEntity<Cycle> saveCycle(@RequestBody Cycle cycle) {

        Cycle update = cycleService.addCycle(cycle);
        if (update == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @GetMapping("/findCycleById/{id}")
    public ResponseEntity<Cycle> getCycleById(@PathVariable("id") Long id){
        Cycle cycle = cycleService.getById(id);
        if (cycle == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cycle, HttpStatus.OK);
    }

    @PutMapping("/updateCycle/{id}")
    public ResponseEntity<Cycle> updateCycle(@PathVariable("id") Long id, @RequestBody Cycle cycle){

        Cycle cycleFromDb = cycleService.updateById(id, cycle);
        if (cycleFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cycleFromDb, HttpStatus.OK);
    }
    
    @PutMapping("/deleteCycle/{id}")
    public ResponseEntity<String> deleteCycle(@RequestBody @PathVariable Long id){
        return new ResponseEntity<>(cycleService.delete(id), HttpStatus.OK);
    }
}
