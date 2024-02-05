package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Departement;
import com.api.gestnotesapi.entities.Option;
import com.api.gestnotesapi.repository.DepartementRepo;
import com.api.gestnotesapi.repository.OptionRepo;
import com.api.gestnotesapi.services.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/option")
public class OptionController {


    private OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/addOption")
    public ResponseEntity<Option> saveOption(@RequestBody Option option){

        Option update = optionService.addOption(option);
        if (update == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    //    Liste des options
    @GetMapping("/findAllOptions")
    public ResponseEntity<List<Option>> getAllOptions() {

        List<Option> list = optionService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Un option par id
    @GetMapping("/findOptionById/{id}")
    public ResponseEntity<Option> getOptionById(@PathVariable("id") Long id) {

        Option option = optionService.getById(id);
        if (option == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(option, HttpStatus.OK);
    }

    //    Liste des options d'un departement
    @GetMapping("/findAllOptionByDepart")
    public ResponseEntity<List<Option>> getAllOptionByDepart(@RequestParam("code") String codeDepart){

        List<Option> optionList = optionService.getListOptionByDepartement(codeDepart);
        if (optionList == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionList, HttpStatus.OK);
    }

    //    Modifier une option
    @PutMapping("/updateOption/{id}")
    public ResponseEntity<Option> updateOption(@PathVariable("id") Long id, @RequestBody Option option){

        Option optionFromDb = optionService.update(id, option);
        if (optionFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionFromDb, HttpStatus.OK);
    }

    //    Supprimer une option
    @DeleteMapping("/deleteOption/{id}")
    public ResponseEntity<String> deleteOption(@PathVariable("id") Long id){
        optionService.delete(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
