package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Departement;
import com.api.gestnotesapi.entities.Option;
import com.api.gestnotesapi.repository.DepartementRepo;
import com.api.gestnotesapi.repository.OptionRepo;
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

    @Autowired
    private OptionRepo optionRepo;
    @Autowired
    private DepartementRepo departementRepo;

    @PostMapping("/addOption")
    public ResponseEntity<Option> saveOption(@RequestBody Option option){

        if (option == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionRepo.save(option), HttpStatus.OK);
    }

    //    Liste des options
    @GetMapping("/findAllOptions")
    public ResponseEntity<List<Option>> getAllOptions() {

        List<Option> list = optionRepo.findAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Un option par id
    @GetMapping("/findOptionById/{id}")
    public ResponseEntity<Option> getOptionById(@PathVariable("id") Long id) {

        Option option = optionRepo.findById(id).orElse(null);
        if (option == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(option, HttpStatus.OK);
    }

    //    Liste des options d'un departement
    @GetMapping("/findAllOptionByDepart")
    public ResponseEntity<List<Option>> getAllOptionByDepart(@RequestParam("code") String codeDepart){

        List<Option> optionList = new ArrayList<>();
        Optional<Departement> departement = departementRepo.findByCode(codeDepart);
        if (!departement.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        for(Option option : optionRepo.findAll()){
            Departement depart = departementRepo.findById(option.getDepartement().getId()).get();
            if (depart.getCode().equals(departement.get().getCode())){
                optionList.add(option);
            }
        }
        return new ResponseEntity<>(optionList, HttpStatus.OK);
    }

    //    Modifier une option
    @PutMapping("/updateOption/{id}")
    public ResponseEntity<Option> updateOption(@PathVariable("id") Long id, @RequestBody Option option){

        Option optionFromDb = optionRepo.findById(id).orElse(null);
        if (optionFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        optionFromDb.setCode(option.getCode());
        optionFromDb.setDescriptionFrench(option.getDescriptionFrench());
        optionFromDb.setDescriptionEnglish(option.getDescriptionEnglish());

        return new ResponseEntity<>(optionRepo.save(optionFromDb), HttpStatus.OK);
    }

    //    Supprimer une option
    @DeleteMapping("/deleteOption/{id}")
    public String deleteOption(@PathVariable("id") Long id){
        optionRepo.deleteById(id);
        return "Deleted with Successfully from database";
    }
}
