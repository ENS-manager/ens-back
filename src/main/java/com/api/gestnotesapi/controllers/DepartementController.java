
package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Departement;

import java.util.List;

import com.api.gestnotesapi.services.DepartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/departement")
public class DepartementController {

    private DepartementService departementService;

    @Autowired
    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    @PostMapping("/addDepartement")
    public ResponseEntity<Departement> saveDepartement(@RequestBody Departement departement) {
        Departement update = departementService.addDepartement(departement);
        if (update == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @GetMapping("/findAllDepartement")
    public ResponseEntity<List<Departement>> getDepartement() {

        List<Departement> list = departementService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

//    Departement par son code
    @GetMapping("/findDepartByCode/departement")
    public ResponseEntity<Departement> getDepartementByCode(@RequestParam String code){
        Departement departement = departementService.getByCode(code);
        if (departement == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(departement, HttpStatus.OK);
    }

    @GetMapping("/findDepartById/{id}")
    public ResponseEntity<Departement> getDepartById(@PathVariable("id") Long id){

        Departement departement = departementService.getById(id);
        if (departement == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(departement, HttpStatus.OK);
    }

    @PutMapping("/updateDepart/{id}")
    public ResponseEntity<Departement> updateDepart(@PathVariable("id") Long id, @RequestBody Departement departement){

        Departement departFromDb = departementService.updateById(id, departement);
        if (departFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(departFromDb, HttpStatus.OK);
    }

    @DeleteMapping("/deleteDepart/{id}")
    public ResponseEntity<String> deleteNiveau(@RequestBody @PathVariable Long id) {
        departementService.delete(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
