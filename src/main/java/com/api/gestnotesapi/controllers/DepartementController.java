
package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Departement;
import com.api.gestnotesapi.repository.DepartementRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/departement")
public class DepartementController {
    @Autowired
    private DepartementRepo departementRepo;

    @PostMapping("/addDepartement")
    public ResponseEntity<Departement> saveDepartement(@RequestBody Departement departement) {
        Departement update = departementRepo.save(departement);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @GetMapping("/findAllDepartement")
    public ResponseEntity<List<Departement>> getDepartement() {

        List<Departement> list = departementRepo.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

//    Departement par son code
    @GetMapping("/findDepartByCode/departement")
    public ResponseEntity<Departement> getDepartementByCode(@RequestParam String code){
        Departement departement = departementRepo.findByCode(code).get();
        if (departement == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(departement, HttpStatus.OK);
    }

    @GetMapping("/findDepartById/{id}")
    public ResponseEntity<Departement> getDepartById(@PathVariable("id") Long id){

        Departement departement = departementRepo.findById(id).orElse(null);
        if (departement == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(departement, HttpStatus.OK);
    }

    @PutMapping("/updateDepart/{id}")
    public ResponseEntity<Departement> updateDepart(@PathVariable("id") Long id, @RequestBody Departement departement){

        Departement departFromDb = departementRepo.findById(id).orElse(null);
        if (departement == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        departFromDb.setCode(departement.getCode());
        departFromDb.setFrenchDescription(departement.getFrenchDescription());
        departFromDb.setEnglishDescription(departement.getEnglishDescription());

        return new ResponseEntity<>(departementRepo.save(departFromDb), HttpStatus.OK);
    }

    @DeleteMapping("/deleteDepart/{id}")
    public ResponseEntity<String> deleteNiveau(@RequestBody @PathVariable Long id) {
        departementRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
