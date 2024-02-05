package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/module")
public class ModuleController {

    private ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @PostMapping("/addModule")
    public ResponseEntity<Module> saveModule(@RequestBody Module module){
        Module mod = moduleService.addModule(module);
        if (mod == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(mod, HttpStatus.OK);
    }

    //    Liste des modules
    @GetMapping("/findAllModule")
    public ResponseEntity<List<Module>> getAllModule() {

        List<Module> list = moduleService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Un module par id
    @GetMapping("/findModuleById/{id}")
    public ResponseEntity<Module> getModuleById(@PathVariable("id") Long id) {

        Module module = moduleService.getById(id);
        if (module == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(module, HttpStatus.OK);
    }

    //    Liste des modules d'un cours
    @GetMapping("/findModuleByCode")
    public ResponseEntity<List<Module>> getModuleByCode(@RequestParam("code") String codeCours){

        List<Module> moduleList = moduleService.getListModuleByCours(codeCours);
        if (moduleList == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(moduleList, HttpStatus.OK);
    }

    //    Modifier un module
    @PutMapping("/updateModule/{id}")
    public ResponseEntity<Module> updateModule(@PathVariable("id") Long id, @RequestBody Module module){

        Module moduleFromDb = moduleService.update(id, module);
        if (moduleFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(moduleFromDb, HttpStatus.OK);
    }

    //    Supprimer un module
    @DeleteMapping("/deleteModule/{id}")
    public ResponseEntity<String> deleteModule(@PathVariable("id") Long id){
        moduleService.delete(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
