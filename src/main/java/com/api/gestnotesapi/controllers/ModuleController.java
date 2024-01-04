package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.repository.*;
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

    @Autowired
    private ModuleRepo moduleRepo;
    @Autowired
    private CoursRepo coursRepo;
    @Autowired
    private DepartementRepo departementRepo;
    @Autowired
    private ParcoursRepo parcoursRepo;
    @Autowired
    private OptionRepo optionRepo;
    @Autowired
    private NiveauRepo niveauRepo;
    @Autowired
    private SemestreRepo semestreRepo;

    @PostMapping("/addModule")
    public ResponseEntity<Module> saveModule(@RequestBody Module module){

        if (module == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Cours cours = coursRepo.findByCoursId(module.getCours().getCoursId());
        Parcours parcours = parcoursRepo.findById(cours.getParcours().getId()).get();
        Option option = optionRepo.findById(parcours.getOption().getId()).get();
        Niveau niveau = niveauRepo.findById(parcours.getNiveau().getId()).get();
        Semestre semestre = semestreRepo.findById(cours.getSemestre().getId()).get();
        Departement departement = departementRepo.findById(option.getDepartement().getId()).get();
        String codeDepart = departement.getCode();
        NatureUE natureUE = cours.getNatureUE();
        int valeurNiveau = niveau.getValeur();
        int valeurSemestre = semestre.getValeur();
        Module updateModule = moduleRepo.save(module);
        String code = cours.getCode()+"_"+updateModule.getId();
        module.setCode(code);
        module.setNatureUE(natureUE);
        module.setSemestre(semestre);
        return new ResponseEntity<>(moduleRepo.save(module), HttpStatus.OK);
    }

    //    Liste des modules
    @GetMapping("/findAllModule")
    public ResponseEntity<List<Module>> getAllModule() {

        List<Module> list = moduleRepo.findAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Un module par id
    @GetMapping("/findModuleById/{id}")
    public ResponseEntity<Module> getModuleById(@PathVariable("id") Long id) {

        Module module = moduleRepo.findById(id).orElse(null);
        if (module == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(module, HttpStatus.OK);
    }

    //    Liste des modules d'un cours
    @GetMapping("/findModuleByCode")
    public ResponseEntity<List<Module>> getModuleByCode(@RequestParam("code") String codeCours){

        List<Module> moduleList = new ArrayList<>();
        Optional<Cours> cours = coursRepo.findByCode(codeCours);
        if (!cours.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        for(Module module : moduleRepo.findAll()){
            Cours cour = coursRepo.findById(module.getCours().getCoursId()).get();
            if (cour.getCode().equals(cours.get().getCode())){
                moduleList.add(module);
            }
        }
        return new ResponseEntity<>(moduleList, HttpStatus.OK);
    }

    //    Modifier un module
    @PutMapping("/updateModule/{id}")
    public ResponseEntity<Module> updateModule(@PathVariable("id") Long id, @RequestBody Module module){

        Module moduleFromDb = moduleRepo.findById(id).orElse(null);
        if (moduleFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        moduleFromDb.setCode(module.getCode());
        moduleFromDb.setIntitule(module.getIntitule());
        moduleFromDb.setNatureUE(module.getNatureUE());
        return new ResponseEntity<>(moduleRepo.save(moduleFromDb), HttpStatus.OK);
    }

    //    Supprimer un module
    @DeleteMapping("/deleteModule/{id}")
    public String deleteModule(@PathVariable("id") Long id){
        moduleRepo.deleteById(id);
        return "Deleted with Successfully from database";
    }
}
