package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.Departement;
import com.api.gestnotesapi.entities.Option;
import com.api.gestnotesapi.entities.Parcours;
import com.api.gestnotesapi.repository.DepartementRepo;
import com.api.gestnotesapi.repository.OptionRepo;
import com.api.gestnotesapi.repository.ParcoursRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartementService {

    private DepartementRepo departementRepo;
    private ParcoursRepo parcoursRepo;
    private OptionRepo optionRepo;

    @Autowired
    public DepartementService(DepartementRepo departementRepo, ParcoursRepo parcoursRepo, OptionRepo optionRepo) {
        this.departementRepo = departementRepo;
        this.parcoursRepo = parcoursRepo;
        this.optionRepo = optionRepo;
    }

    public Departement addDepartement(Departement departement) {
        if (departement == null){
            return null;
        }
        return departementRepo.save(departement);
    }

    public List<Departement> getAll() {
        List<Departement> list = departementRepo.findAll();
        if (list == null){
            return null;
        }
        return list;
    }

    public Departement getByCode(String code) {
        Departement departement = departementRepo.findByCode(code).orElse(null);
        if (departement == null){
            return null;
        }
        return departement;
    }

    public Departement getById(Long id) {
        Departement departement = departementRepo.findById(id).orElse(null);
        if (departement == null){
            return null;
        }
        return departement;
    }

    public Departement updateById(Long id, Departement departement) {
        Departement update = getById(id);
        if (update == null){
            return null;
        }
        update.setActive(departement.getActive());
        update.setCode(departement.getCode());
        update.setEnglishDescription(departement.getEnglishDescription());
        update.setFrenchDescription(departement.getFrenchDescription());

        return departementRepo.save(update);
    }

    public String delete(Long id) {
        Departement departement = getById(id);
        if (departement == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        departement.setActive(false);
        departementRepo.save(departement);

        return "Operation reussi avec succes";
    }

    public Departement getByCodeAndActive(String code, boolean b) {
        Departement departement = departementRepo.findByCodeAndActive(code, b);
        if (departement == null){
            return null;
        }
        return departement;
    }

    public List<Departement> getAllActif() {
        List<Departement> departementList = getAll();
        List<Departement> departements = new ArrayList<>();
        for (Departement departement : departementList){
            if (departement.getActive().equals(true)){
                departements.add(departement);
            }
        }
        return departements;
    }

    public Departement getByParcours(String label) {
        Parcours parcours = parcoursRepo.findByLabel(label).orElse(null);
        if (parcours == null){
            return null;
        }
        Option option = optionRepo.findById(parcours.getOption().getId()).orElse(null);
        if (option == null){
            return null;
        }
        return option.getDepartement();
    }
}
