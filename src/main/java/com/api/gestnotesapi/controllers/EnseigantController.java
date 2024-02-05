package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;

import java.util.ArrayList;
import java.util.List;

import com.api.gestnotesapi.services.DepartementService;
import com.api.gestnotesapi.services.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/enseignant")
public class EnseigantController {

    private EnseignantService enseignantService;
    private DepartementService departementService;

    @Autowired
    public EnseigantController(EnseignantService enseignantService, DepartementService departementService) {
        this.enseignantService = enseignantService;
        this.departementService = departementService;
    }

    //    Ajouter un enseignant avec la liste de ses cours
    @PostMapping("/addEnseignant")
    public ResponseEntity<Enseignant> saveEnseignant(@RequestBody Enseignant enseignant) {
        Enseignant save = enseignantService.addEnseignant(enseignant);
        if (save == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(save, HttpStatus.OK);
    }
    
    @GetMapping("/findAllEnseignant")
    public ResponseEntity<List<Enseignant>> getEnseignant() {
        
        List<Enseignant> list = enseignantService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/findEnseignantById/{id}")
    public ResponseEntity<Enseignant> getEnseignantById(@PathVariable("id") Long id){
        Enseignant enseignant = enseignantService.getById(id);
        if (enseignant == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(enseignant, HttpStatus.OK);
    }

//    Liste des enseignants d'un departement
    @GetMapping("/findListEnseignantByDepartement")
    public ResponseEntity<List<Enseignant>> getEnseignantListByDepart(@RequestParam String code){
        Departement departement = departementService.getByCode(code);
        List<Enseignant> enseignantList = new ArrayList<>();
        if (departement == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        for (Enseignant enseignant : enseignantService.getAll()){
            for (Cours cours : enseignant.getCours()){
                Departement depart = departementService.getById(cours.getDepartement().getId());
                if (depart.getCode().equals(departement.getCode())){
                    enseignantList.add(enseignant);
                }
                break;
            }
        }
        return new ResponseEntity<>(enseignantList, HttpStatus.OK);
    }

//    Liste des cours enseigner par un enseignant
    @GetMapping("/findCoursTeachByEnseignant/{id}")
    public ResponseEntity<List<Cours>> getCoursTeachByEnseignant(@PathVariable("id") Long id){
        Enseignant enseignant = enseignantService.getById(id);
        if (enseignant == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(enseignant.getCours(), HttpStatus.OK);
    }

    @PutMapping("/uptadeEnseignant/{id}")
    public ResponseEntity<Enseignant> updateNiveau(@PathVariable("id") Long id, @RequestBody Enseignant enseignant) {

        Enseignant enseignantFromDb = enseignantService.update(id, enseignant);
        if (enseignantFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(enseignantFromDb, HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteEnseignant/{id}")
    public ResponseEntity<String> deleteEnseignant(@PathVariable("id") Long id) {
        enseignantService.delete(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
