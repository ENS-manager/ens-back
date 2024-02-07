package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleService {

    private ModuleRepo moduleRepo;
    private CoursService coursService;
    private DepartementRepo departementRepo;
    private ParcoursRepo parcoursRepo;
    private OptionRepo optionRepo;
    private NiveauRepo niveauRepo;
    private SemestreRepo semestreRepo;
    private CreditRepo creditRepo;

    @Autowired
    public ModuleService(ModuleRepo moduleRepo, CoursService coursService, DepartementRepo departementRepo, ParcoursRepo parcoursRepo, OptionRepo optionRepo, NiveauRepo niveauRepo, SemestreRepo semestreRepo, CreditRepo creditRepo) {
        this.moduleRepo = moduleRepo;
        this.coursService = coursService;
        this.departementRepo = departementRepo;
        this.parcoursRepo = parcoursRepo;
        this.optionRepo = optionRepo;
        this.niveauRepo = niveauRepo;
        this.semestreRepo = semestreRepo;
        this.creditRepo = creditRepo;
    }

    public Module addModule(Module module) {
        if (module == null){
            return null;
        }
        Cours cours = coursService.getById(module.getCours().getCoursId());
        int creditCours = cours.getCredit().getValeur();
        int creditModule = creditRepo.findById(module.getCredit().getId()).get().getValeur();
        int sommeCreditModule = 0;
        int reste = 0;
        int n = 0;
        int size = moduleRepo.findAll().size();
        System.out.println("La taille: " + size);
        if (size != 0){
            for (Module mod : moduleRepo.findAll()){
                Cours cour = coursService.getById(mod.getCours().getCoursId());
                if (cour.getCode().equals(cours.getCode())){
                    ++n;
                    sommeCreditModule = sommeCreditModule + mod.getCredit().getValeur();
                }
            }
            reste = creditCours - sommeCreditModule;
            n = n+1;
            if (creditModule > reste){
                return null;
            }
            String code = cours.getCode()+"_"+n;
            module.setCode(code);
            return moduleRepo.save(module);
        }
        String code = cours.getCode()+"_"+1;
        module.setCode(code);
        return moduleRepo.save(module);
    }

    public List<Module> getAll() {
        List<Module> list = moduleRepo.findAll();
        if (list == null){
            return null;
        }
        return list;
    }

    public Module getById(Long id) {
        Module module = moduleRepo.findById(id).orElse(null);
        if (module == null){
            return null;
        }
        return module;
    }

    public List<Module> getListModuleByCours(String codeCours) {
        List<Module> list = new ArrayList<>();
        Cours cours = coursService.getByCode(codeCours);
        if (cours == null){
            return null;
        }
        for(Module module : moduleRepo.findAll()){
            Cours cour = coursService.getById(module.getCours().getCoursId());
            if (cour.getCode().equals(cours.getCode())){
                list.add(module);
            }
        }
        if (list == null){
            return null;
        }
        return list;
    }

    public Module update(Long id, Module module) {
        Module mod = getById(id);
        if (mod == null){
            return null;
        }
        mod.setIntitule(module.getIntitule());
        mod.setCode(module.getCode());
        mod.setCours(module.getCours());
        mod.setCredit(module.getCredit());

        return moduleRepo.save(mod);
    }

    public String delete(Long id) {
        Module module = getById(id);
        if (module == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        module.setActive(false);
        moduleRepo.save(module);

        return "Operation reussi avec succes";
    }

    public Module getCode(String code) {
        Module module = moduleRepo.findByCode(code).orElse(null);
        if (module == null){
            return null;
        }
        return module;
    }
}
