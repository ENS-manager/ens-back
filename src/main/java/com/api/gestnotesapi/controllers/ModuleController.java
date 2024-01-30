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

    private ModuleRepo moduleRepo;
    private CoursRepo coursRepo;
    private DepartementRepo departementRepo;
    private ParcoursRepo parcoursRepo;
    private OptionRepo optionRepo;
    private NiveauRepo niveauRepo;
    private SemestreRepo semestreRepo;
    private CreditRepo creditRepo;

    public ModuleController(ModuleRepo moduleRepo, CoursRepo coursRepo, DepartementRepo departementRepo, ParcoursRepo parcoursRepo, OptionRepo optionRepo, NiveauRepo niveauRepo, SemestreRepo semestreRepo, CreditRepo creditRepo) {
        this.moduleRepo = moduleRepo;
        this.coursRepo = coursRepo;
        this.departementRepo = departementRepo;
        this.parcoursRepo = parcoursRepo;
        this.optionRepo = optionRepo;
        this.niveauRepo = niveauRepo;
        this.semestreRepo = semestreRepo;
        this.creditRepo = creditRepo;
    }

    @PostMapping("/addModule")
    public ResponseEntity<Module> saveModule(@RequestBody Module module){

        if (module == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Cours cours = coursRepo.findByCoursId(module.getCours().getCoursId());
        int creditCours = cours.getCredit().getValeur();
        int creditModule = creditRepo.findById(module.getCredit().getId()).get().getValeur();
        int sommeCreditModule = 0;
        int reste = 0;
        int n = 0;
        int size = moduleRepo.findAll().size();
        if (size != 0){
            for (Module mod : moduleRepo.findAll()){
                Cours cour = coursRepo.findByCoursId(mod.getCours().getCoursId());
                if (cour.getCode().equals(cours.getCode())){
                    ++n;
                    sommeCreditModule = sommeCreditModule + mod.getCredit().getValeur();
                }
            }
            reste = creditCours - sommeCreditModule;
            n = n+1;
            if (creditModule > reste){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            String code = cours.getCode()+"_"+n;
            module.setCode(code);
            return new ResponseEntity<>(moduleRepo.save(module), HttpStatus.OK);
        }
        String code = cours.getCode()+"_"+1;
        module.setCode(code);
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

        moduleFromDb.setIntitule(module.getIntitule());
        moduleFromDb.setCode(module.getCode());
        moduleFromDb.setCours(module.getCours());
        moduleFromDb.setCredit(module.getCredit());
        return new ResponseEntity<>(moduleRepo.save(moduleFromDb), HttpStatus.OK);
    }

    //    Supprimer un module
    @DeleteMapping("/deleteModule/{id}")
    public ResponseEntity<String> deleteModule(@PathVariable("id") Long id){
        moduleRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}
